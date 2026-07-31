/* TrackParityIT.java

	Proves the two authoring tracks are interchangeable.

	The book tells readers to pick either the plain-JS track (zk.$extends) or the
	TypeScript track (@zk.WrapClass) and that the choice is about tooling, not
	capability. That is a claim, and this is the test that keeps it honest: the same
	server-side class, driven through two different client implementations, must produce
	equivalent DOM and deliver the same event with the same payload.

	If this fails, the book's "pick either" advice is wrong for whatever it caught.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;
import org.zkoss.test.webdriver.ztl.JQuery;

public class TrackParityIT extends WebDriverTestCase {

	private JQuery sub(String zkId, String subId) {
		String uuid = getEval("zk.Widget.$('$" + zkId + "').uuid");
		return jq("#" + uuid + "-" + subId);
	}

	@Test
	@DisplayName("both tracks deliver a widget class to the client")
	public void bothTracksLoad() {
		connect("/simplelabelts.zul");
		waitResponse();

		assertEquals("true", getEval("!!(window.com && com.foo && com.foo.SimpleLabel)"),
				"the plain-JS widget package did not load");
		// The TS class is registered by the @zk.WrapClass decorator, not by an assignment,
		// so this also proves the decorator survived compilation and ran.
		assertEquals("true", getEval("!!(window.labts && labts.SimpleLabel)"),
				"the tsc-compiled widget package did not load - check the emit for a module wrapper");

		assertNoJSError();
	}

	@Test
	@DisplayName("both tracks render the same structure and the same scoped classes")
	public void bothTracksRenderEquivalentDom() {
		connect("/simplelabelts.zul");
		waitResponse();

		// Same outer element, same zclass: the mold is shared, so this checks that the
		// TS widget honors it identically.
		assertTrue(jq("$greet").is("span"));
		assertTrue(jq("$ts").is("span"));
		assertEquals(jq("$greet").attr("class"), jq("$ts").attr("class"),
				"the two tracks disagree about the outer element's classes");

		// Same sub-element contract, so $n('inner') works the same in both.
		assertTrue(sub("ts", "inner").exists(),
				"the TS widget's mold did not emit the -inner sub-element");
		assertEquals(sub("greet", "inner").attr("class"), sub("ts", "inner").attr("class"),
				"the two tracks disagree about the sub-element's scoped classes");

		// Same initial text, i.e. both honor renderProperties() the same way.
		assertEquals("Hello ZK", sub("ts", "inner").text());
		assertEquals(sub("greet", "inner").text(), sub("ts", "inner").text());

		assertNoJSError();
	}

	@Test
	@DisplayName("both tracks fire onClear with the same payload and reach the server")
	public void bothTracksFireTheSameEvent() {
		connect("/simplelabelts.zul");
		waitResponse();

		assertEquals("untouched", jq("$msg").text());
		assertEquals("untouched", jq("$tsmsg").text());

		// Plain-JS track.
		click(jq("$greet"));
		waitResponse();
		assertEquals("cleared", jq("$msg").text());

		// TypeScript track: super.bind_() must have registered the listener, and
		// this.fire() must have reached the same service() on the same Java class.
		click(jq("$ts"));
		waitResponse();
		assertEquals("cleared", jq("$tsmsg").text(),
				"the TS widget's fire('onClear') never reached the server");

		// Both cleared their own DOM.
		assertEquals("", sub("greet", "inner").text());
		assertEquals("", sub("ts", "inner").text());

		// And both toggle back, so the payload is read in both, not hard-coded.
		click(jq("$greet"));
		click(jq("$ts"));
		waitResponse();
		assertEquals("restored", jq("$msg").text());
		assertEquals("restored", jq("$tsmsg").text());

		assertNoZKError();
		assertNoJSError();
	}

	@Test
	@DisplayName("the TS widget's super.bind_/unbind_ chain works, not just its own body")
	public void tsSuperCallsChainCorrectly() {
		connect("/simplelabelts.zul");
		waitResponse();

		// A TS widget that used $supers(Class, 'bind_', ...) instead of super.bind_()
		// recurses until the stack overflows - the widget would never finish binding.
		// Reaching a bound widget with a resolvable sub-node proves the chain ran.
		assertEquals("true", getEval("!!zk.Widget.$('$ts').$n('inner')"),
				"the TS widget never completed bind_; check for $supers(Class,...) misuse");

		getEval("(function(){zk.Widget.$('$ts').detach();return '';})()");
		waitResponse();

		assertEquals("detached",
				getEval("(function(){return zk.Widget.$('$ts')?'still-bound':'detached';})()"),
				"the TS widget survived detach(); super.unbind_() did not chain");

		assertNoJSError();
	}
}
