/* SimpleLabelIT.java

	The CLIENT-side contract of com.foo.SimpleLabel, asserted with ZK WebDriver.

	This is the half ZATS cannot reach: whether zk.wpd actually packaged the widget
	class, whether the mold really emitted the DOM and the scoped classes, and whether
	the widget's own fire('onClear') really arrives at the server.

	Runs under failsafe in `mvn verify`. Requires a local Chrome BROWSER; chromedriver
	itself is provisioned automatically by WebDriverManager. Chrome runs headless.

	ZK WebDriver is free and part of ZK CE.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;
import org.zkoss.test.webdriver.ztl.JQuery;

public class SimpleLabelIT extends WebDriverTestCase {

	/**
	 * Selects a widget's SUB-element.
	 *
	 * <p>ZK's {@code jq("$id")} shorthand resolves a ZK <em>component</em> id, so
	 * {@code jq("$greet-inner")} matches nothing at all - it looks for a component
	 * called "greet-inner". Sub-elements have to be addressed by their real DOM id,
	 * which by convention is {@code <uuid>-<subId>}: exactly the string the mold
	 * emitted and exactly what {@code this.$n(subId)} looks up on the client.
	 */
	private JQuery sub(String zkId, String subId) {
		String uuid = getEval("zk.Widget.$('$" + zkId + "').uuid");
		return jq("#" + uuid + "-" + subId);
	}

	@Test
	@DisplayName("zk.wpd loads the widget class and the mold renders the documented DOM")
	public void moldRendersAndScopedClassesApply() {
		// connect() MUST come first. It launches the browser, HTTP-checks the URL so a
		// missing page fails with a readable message instead of an NPE, and publishes
		// the driver to the ThreadLocal that every jq()/getEval() helper reads. Call
		// jq(...) before this line and you get an NPE from an empty ThreadLocal.
		//
		// Note the method is getWebDriver(), not getDriver() - the latter does not
		// exist in zk-webdriver 1.4.43.0.0.
		connect("/simplelabel.zul");
		waitResponse();

		// The widget package really loaded. If zk.wpd were missing or misnamed this is
		// the assertion that fails, and it fails loudly - whereas in a browser the
		// symptom is a silently empty element, because ZK catches the error, logs it
		// to the console and marks the package loaded anyway.
		assertEquals("true", getEval("!!(window.com && com.foo && com.foo.SimpleLabel)"),
				"zk.wpd did not deliver com.foo.SimpleLabel to the client");

		assertTrue(jq("$greet").exists(), "the widget rendered no DOM at all");
		// getZclass() on the server and $s() on the client must agree on the prefix.
		assertTrue(jq("$greet").hasClass("z-simplelabel"),
				"domAttrs_() did not apply the zclass to the outer element");

		// The mold's sub-element, and the scoped class $s('inner') produces.
		assertTrue(sub("greet", "inner").exists(),
				"the mold did not emit the id=\"<uuid>-inner\" sub-element that $n('inner') needs");
		assertTrue(sub("greet", "inner").hasClass("z-simplelabel-inner"),
				"$s('inner') did not produce the expected scoped class");

		// $n(subId) on the client resolves the very node the mold wrote. If the mold and
		// the widget disagree about the sub-id, the setter silently updates nothing -
		// so assert the lookup itself, not just the DOM.
		assertEquals("true",
				getEval("!!zk.Widget.$('$greet').$n('inner')"),
				"$n('inner') could not resolve the sub-element the mold emitted");

		// The value rendered at FIRST paint, i.e. renderProperties() did its job.
		// A property that is only smartUpdate-ed passes a click test and fails here.
		assertEquals("Hello ZK", sub("greet", "inner").text(),
				"renderProperties() did not render the initial value");

		assertNoZKError();
		assertNoJSError();
	}

	@Test
	@DisplayName("the second mold produces a different structure but the same sub-element ids")
	public void fancyMoldKeepsTheSubElementContract() {
		connect("/simplelabel.zul");
		waitResponse();

		// The default mold emits <span>, the fancy mold emits <div>: proof that
		// mold="fancy" in the ZUL reached the client and selected a different function.
		assertTrue(jq("$greet").is("span"), "the default mold should render a span");
		assertTrue(jq("$fancy").is("div"), "the fancy mold should render a div");

		// ...but both keep the "-inner" sub-id, which is what lets the widget's JS stay
		// mold-agnostic. This is the contract the book states; assert it.
		assertTrue(sub("fancy", "inner").exists(),
				"the fancy mold broke the sub-element id contract");
		assertTrue(sub("fancy", "inner").hasClass("z-simplelabel-inner"),
				"the fancy mold dropped the shared scoped class");
		assertTrue(sub("fancy", "inner").hasClass("z-simplelabel-fancy"),
				"the fancy mold did not add its own scoped class");

		assertNoJSError();
	}

	@Test
	@DisplayName("clicking the widget fires onClear and the server receives it")
	public void clickFiresOnClearAndReachesTheServer() {
		connect("/simplelabel.zul");
		waitResponse();

		assertEquals("untouched", jq("$msg").text());

		// This is the full round trip: a real DOM click -> the widget's domListen_
		// callback -> fire('onClear') -> AU request -> service() -> postEvent -> the
		// page's listener updates #msg. Nothing else in the suite proves the client and
		// server halves are wired to each other.
		click(jq("$greet"));
		waitResponse();

		assertEquals("cleared", jq("$msg").text(),
				"the client's fire('onClear') never reached the server-side listener");
		// The client cleared its own text too.
		assertEquals("", sub("greet", "inner").text());

		// Clicking again restores it, proving the payload is read rather than assumed.
		click(jq("$greet"));
		waitResponse();

		assertEquals("restored", jq("$msg").text());
		assertEquals("Hello ZK", sub("greet", "inner").text());

		assertNoZKError();
		assertNoJSError();
	}

	@Test
	@DisplayName("unbind_ really removes the listener bind_ registered")
	public void unbindRemovesItsListener() {
		connect("/simplelabel.zul");
		waitResponse();

		// The book used to teach a widget whose domUnlisten_ passed a different callback
		// name than domListen_ did, which leaks the listener silently. Assert the fix.
		String handlersBefore = getEval(
				"(function(){var n=zk.Widget.$('$greet').$n();"
				+ "var e=jq._data?jq._data(n,'events'):null;"
				+ "return e&&e.click?String(e.click.length):'0';})()");
		assertTrue(Integer.parseInt(handlersBefore) > 0,
				"bind_ registered no click handler at all; expected at least one");

		// Detach the widget: unbind_ runs.
		getEval("(function(){zk.Widget.$('$greet').detach();return '';})()");
		waitResponse();

		assertEquals("detached",
				getEval("(function(){var w=zk.Widget.$('$greet');return w?'still-bound':'detached';})()"),
				"the widget survived detach(); unbind_ did not run");

		assertNoJSError();
	}
}
