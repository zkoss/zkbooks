/* ScopedDomIT.java

	getZclass(), $s(sub) and $n(subId) - the three calls every mold and every widget
	method in the book uses - asserted against the real DOM in a real browser.

	Written from tasks/research/component-dev/styling-dom.md (F1, F2, F3, F11, F12, F13).
	Each @DisplayName is one documented claim.

	Why an *IT: every claim here is either a rendered class attribute, a computed client
	string, or a DOM lookup. ZATS renders no HTML and runs no JavaScript, so all of it is
	invisible there - the classic trap is a class that the mold never emits yet every
	server-side test is green.

	Runs under failsafe in `mvn verify`. Needs a local Chrome; runs headless.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;
import org.zkoss.test.webdriver.ztl.JQuery;

public class ScopedDomIT extends WebDriverTestCase {

	private static final String PAGE = "/moldforms.zul";

	private JQuery sub(String zkId, String subId) {
		return jq("#" + uuidOf(zkId) + "-" + subId);
	}

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	// ------------------------------------------------------------------------ F1 / F3

	@Test
	@DisplayName("$s() with no argument returns getZclass(), $s(sub) is getZclass() + '-' + sub, and that exact string is the class on the rendered sub-element")
	public void scopedClassPairsWithGetZclassAndLandsOnTheElement() {
		connect(PAGE);
		waitResponse();

		assertTrue(jq("$named").exists(), "the widget rendered no DOM at all");

		// The server's own default, read from the class under test in this JVM. The client
		// computes its default INDEPENDENTLY (F3: renderProperties sends the _zclass FIELD,
		// which is null), so this equality is the documented convention, not a mechanism.
		String serverZclass = new SimpleLabel().getZclass();
		assertEquals("z-simplelabel", serverZclass,
				"the server-side default changed; the client's 'z-' + widgetName no longer matches it");
		assertEquals(serverZclass, getEval("zk.Widget.$('$named').getZclass()"),
				"the client and the server disagree about the zclass, which no ZATS test can see");

		// $s() with no argument IS getZclass().
		assertEquals("true", getEval("String(zk.Widget.$('$named').$s() === zk.Widget.$('$named').getZclass())"),
				"$s() with no argument must return the zclass itself");

		// $s(sub) = getZclass() + '-' + sub. Asserted as the RULE and as the literal, so a
		// changed prefix and a changed join both show up.
		String scoped = getEval("zk.Widget.$('$named').$s('inner')");
		assertEquals("z-simplelabel-inner", scoped, "$s('inner') is documented as getZclass() + '-inner'");
		assertEquals("true",
				getEval("String(zk.Widget.$('$named').$s('inner') === zk.Widget.$('$named').$s() + '-inner')"),
				"$s(sub) must append '-' + sub to the zclass");

		// ...and the string the live widget computes is the one really ON the element the
		// mold wrote - read from the DOM, not from the mold source. With the default mold
		// (no extra classes) it is the WHOLE class attribute.
		assertEquals(scoped, sub("named", "inner").attr("class"),
				"the sub-element's class is not the string $s('inner') returns");

		// The scoped class belongs to the sub-element only: without this the assertion
		// above could be satisfied by a mold that puts every class everywhere.
		assertEquals("z-simplelabel", jq("$named").attr("class"),
				"the root element's class attribute must be exactly the zclass");
		assertFalse(jq("$named").hasClass("z-simplelabel-inner"),
				"the root element carries the sub-element's scoped class");

		assertNoZKError();
		assertNoJSError();
	}

	@Test
	@DisplayName("$s() memoizes per sub-id and ONLY setZclass clears the memo: poking _zclass directly leaves the stale scoped class in place forever")
	public void theScopedClassMemoIsClearedOnlyBySetZclass() {
		connect(PAGE);
		waitResponse();

		// One round trip through the client engine:
		//   1. $s('inner') with the default zclass          -> memoized in _subzcls
		//   2. _zclass poked directly (NOT setZclass)       -> memo untouched, stale value
		//   3. setZclass('my-label')                        -> _subzcls = {}, then rerender
		// The middle step is the documented hazard; it is also what makes this test able to
		// fail. If $s() did not memoize, step 2 would already read "sneaky-inner".
		String observed = getEval("(function(){"
				+ "var w = zk.Widget.$('$named');"
				+ "var before = w.$s('inner');"
				+ "w._zclass = 'sneaky';"
				+ "var stale = w.$s('inner');"
				+ "w.setZclass('my-label');"
				+ "return before + '|' + stale + '|' + w.$s('inner');"
				+ "})()");
		assertEquals("z-simplelabel-inner|z-simplelabel-inner|my-label-inner", observed,
				"the $s() memo does not behave as documented (before|after-direct-poke|after-setZclass)");

		waitResponse();

		// setZclass also re-ran the mold, so the sub-element carries the new scoped class.
		assertEquals("my-label-inner", sub("named", "inner").attr("class"),
				"setZclass did not rebuild the sub-element's scoped class");

		assertNoJSError();
	}

	// ------------------------------------------------------------------------ F2 / F3

	@Test
	@DisplayName("the server's getZclass() default is never transmitted: the client's _zclass is null and it derives 'z-' + widgetName by itself")
	public void theClientDerivesTheZclassDefaultItself() {
		connect(PAGE);
		waitResponse();

		// Nothing about zclass crossed the wire: renderProperties() rendered the FIELD,
		// which is null. If someone "fixes" the component to send getZclass() instead, this
		// is the assertion that notices.
		assertEquals("true", getEval("String(zk.Widget.$('$named')._zclass == null)"),
				"the client received a zclass property although the server field is null");

		// Yet the client answers the same string, because it recomputes it from widgetName,
		// which is the LOWERCASED LAST SEGMENT of the registered class name.
		assertEquals("simplelabel", getEval("zk.Widget.$('$named').widgetName"),
				"widgetName is not the lowercased last segment of the widget class name");
		assertEquals("com.foo.SimpleLabel", getEval("zk.Widget.$('$named').className"),
				"the widget was registered under an unexpected class name");
		assertEquals("z-simplelabel", getEval("zk.Widget.$('$named').getZclass()"),
				"the client default is not 'z-' + widgetName");

		assertNoJSError();
	}

	// ----------------------------------------------------------------------- F11 / F12

	@Test
	@DisplayName("$n(subId) resolves the <uuid>-<subId> element, and jq(\"$id-sub\") can never reach it")
	public void subElementIsReachableOnlyByItsRealDomId() {
		connect(PAGE);
		waitResponse();

		String uuid = uuidOf("named");

		// Positive controls first: without them the negative below would also pass on a
		// page where nothing rendered.
		assertTrue(jq("$named").exists(),
				"positive control: a component-id selector must resolve the widget's ROOT node");
		assertTrue(jq("#" + uuid + "-inner").exists(),
				"positive control: the sub-element exists under the DOM id <uuid>-inner");

		// The load-bearing negative: ZK's $id selector resolves a COMPONENT id to a widget
		// ROOT node, so "$named-inner" looks for a component literally called
		// "named-inner" and matches nothing. Every book snippet of the form jq("$id-sub")
		// is wrong because of this.
		assertFalse(jq("$named-inner").exists(),
				"jq(\"$id-sub\") resolved something, which would make the jq(\"$id-sub\") snippet form legal");

		// $n(subId) does the lookup the mold's id makes possible.
		assertEquals(uuid + "-inner", getEval("zk.Widget.$('$named').$n('inner').id"),
				"$n('inner') did not resolve the node the mold emitted");
		assertEquals("true", getEval("String(zk.Widget.$('$named').$n('inner') === document.getElementById('"
				+ uuid + "-inner'))"),
				"$n(subId) returned something other than the document element with that id");

		assertNoJSError();
	}

	@Test
	@DisplayName("a $n(subId) MISS is memoized as 'n/a' and never retried, and $n_(subId) is the throwing variant")
	public void aMissedSubNodeLookupIsNegativelyCached() {
		connect(PAGE);
		waitResponse();

		// The documented hazard: ask for a sub-node before the mold's HTML is in the
		// document and the miss is cached forever (until clearCache() on detach). The
		// observable form of "cached" is the literal string 'n/a' in _subnodes.
		assertEquals("true|n/a|true", getEval("(function(){"
				+ "var w = zk.Widget.$('$named');"
				+ "var first = (w.$n('nosuch') === undefined);"
				+ "var memo = w._subnodes['nosuch'];"
				+ "var second = (w.$n('nosuch') === undefined);"
				+ "return String(first) + '|' + memo + '|' + String(second);"
				+ "})()"),
				"a missed $n(subId) lookup is not negatively cached as documented");

		// $n_ is the throwing variant. The typeof guard comes first on purpose: without it
		// a missing $n_ would ALSO make the try/catch report 'threw', and the test would
		// pass for the wrong reason.
		assertEquals("function", getEval("typeof zk.Widget.$('$named').$n_"),
				"$n_ does not exist on this client engine");
		assertEquals("resolved/threw:not-found", getEval("(function(){"
				+ "var w = zk.Widget.$('$named'), ok;"
				+ "try { ok = (w.$n_('inner').id === w.uuid + '-inner') ? 'resolved' : 'wrong-node'; }"
				+ "catch (e) { return 'threw-for-inner'; }"
				+ "try { w.$n_('nosuch'); return ok + '/no-throw'; }"
				+ "catch (e) { return ok + '/threw:' + (String(e).indexOf('not found') >= 0 ? 'not-found' : String(e)); }"
				+ "})()"),
				"$n_ must resolve an existing sub-id and throw for a missing one");

		assertNoJSError();
	}

	// ---------------------------------------------------------------------------- F13

	@Test
	@DisplayName("zk.Widget.uuid() maps a sub-element back to its widget by truncating at the FIRST hyphen, so a sub-id may contain hyphens but a uuid may not")
	public void subElementIdsMapBackToTheirWidget() {
		connect(PAGE);
		waitResponse();

		String uuid = uuidOf("named");

		// A uuid never contains a hyphen - which is exactly why truncating at the first
		// one is a safe reverse mapping.
		assertEquals("-1", getEval("String(zk.Widget.$('$named').uuid.indexOf('-'))"),
				"this uuid contains a hyphen, so the documented reverse mapping cannot work");

		assertEquals(uuid, getEval("zk.Widget.uuid('" + uuid + "-inner')"),
				"zk.Widget.uuid(<uuid>-inner) did not return the widget's uuid");
		// The element form of the same call, i.e. from a DOM node back to its widget.
		assertEquals(uuid, getEval("zk.Widget.uuid(zk.Widget.$('$named').$n('inner'))"),
				"zk.Widget.uuid(node) did not return the widget's uuid");

		// A MULTI-hyphen sub-id still round-trips, because the truncation is at the FIRST
		// hyphen and not the last. This is the assertion that would fail if the rule were
		// lastIndexOf - the two are indistinguishable on a single-hyphen sub-id.
		assertEquals("z_aa", getEval("zk.Widget.uuid('z_aa-tool-button-label')"),
				"the truncation is not at the FIRST hyphen, so multi-hyphen sub-ids break");

		assertNoJSError();
	}

	// ----------------------------------------------------------------------------- F2

	@Test
	@DisplayName("both authoring tracks derive the SAME zclass and the same $s() strings although their widget packages differ, because widgetName ignores the package")
	public void bothTracksDeriveTheSameScopedClasses() {
		connect(PAGE);
		waitResponse();

		// Two different widget classes...
		assertEquals("com.foo.SimpleLabel", getEval("zk.Widget.$('$named').className"));
		assertEquals("labts.SimpleLabel", getEval("zk.Widget.$('$tsnamed').className"),
				"the TypeScript widget is not registered under labts.SimpleLabel");
		assertNotEquals(getEval("zk.Widget.$('$named').className"), getEval("zk.Widget.$('$tsnamed').className"));

		// ...one widgetName, because only the lowercased LAST SEGMENT is used.
		assertEquals("simplelabel", getEval("zk.Widget.$('$tsnamed').widgetName"),
				"the package is not being ignored when deriving widgetName");

		// Therefore both share every $s() string and every CSS rule.
		assertEquals("z-simplelabel", getEval("zk.Widget.$('$tsnamed').getZclass()"));
		assertEquals(getEval("zk.Widget.$('$named').$s('inner')"), getEval("zk.Widget.$('$tsnamed').$s('inner')"),
				"the two tracks compute different scoped classes");
		assertEquals(sub("named", "inner").attr("class"), sub("tsnamed", "inner").attr("class"),
				"the two tracks rendered different scoped classes");

		assertNoZKError();
		assertNoJSError();
	}
}
