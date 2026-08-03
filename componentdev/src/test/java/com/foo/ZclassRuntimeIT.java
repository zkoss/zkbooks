/* ZclassRuntimeIT.java

	The RUNTIME half of zk_component_dev_essentials/zclass_and_scoped_class_names.md and
	addressing_sub_elements.md that nothing in the suite asserted.

	Each @DisplayName is one sentence of those pages:

	  - "setSclass() only rewrites the root node's className, through updateDomClass_().
	     Nothing else in your subtree is touched." (versus setZclass, which re-runs the mold)
	  - "The result is XML-encoded and memoized per sub-id."
	  - "domClass_() re-appends any z-flex* class it finds on the live node once the widget
	     is bound, so its output is not identical between first paint and a later rerender."
	  - "the entry is discarded only when the widget's cache is cleared on detach, not on
	     the next rerender."
	  - "a uuid may not [contain a hyphen], which is why ZK refuses to change a uuid once
	     the widget is bound: the sub-nodes already carry it."
	  - "zk.loadCSS(href, id, media, callback) injects a <link> from the client."

	Why *IT: all six are client-engine behaviour on a rendered widget. ZATS runs no
	JavaScript, so none of it is visible there.

	Runs under failsafe in `mvn verify`. Needs a local Chrome; runs headless.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;

public class ZclassRuntimeIT extends WebDriverTestCase {

	private static final String PAGE = "/styling-contract.zul";

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	// ------------------------------------------------------------------------------- F5

	@Test
	@DisplayName("setSclass rewrites only the root node's className and leaves the subtree alone, while setZclass re-runs the mold and replaces it")
	public void setSclassPatchesTheRootWhileSetZclassRebuildsTheSubtree() {
		connect(PAGE);
		waitResponse();

		String uuid = uuidOf("sub");

		// A marker on the live sub-element is how "the same node" is told from "a new node
		// that happens to look the same". Reading it from document.getElementById rather
		// than $n() on purpose: $n() memoizes, so it could hand back a detached node and
		// hide the very difference this test is about.
		assertEquals("z-simplelabel|z-simplelabel-inner|M1", getEval("(function(){"
				+ "var w = zk.Widget.$('$sub');"
				+ "document.getElementById('" + uuid + "-inner').__marker = 'M1';"
				+ "return document.getElementById('" + uuid + "').className + '|'"
				+ "     + document.getElementById('" + uuid + "-inner').className + '|'"
				+ "     + String(document.getElementById('" + uuid + "-inner').__marker);"
				+ "})()"), "precondition: default classes plus the marker just written");

		// setSclass: the ROOT's className is rewritten (sclass first, zclass second) and
		// NOTHING in the subtree is touched - the marked node is still the same object.
		assertEquals("bar z-simplelabel|z-simplelabel-inner|M1", getEval("(function(){"
				+ "var w = zk.Widget.$('$sub');"
				+ "w.setSclass('bar');"
				+ "return document.getElementById('" + uuid + "').className + '|'"
				+ "     + document.getElementById('" + uuid + "-inner').className + '|'"
				+ "     + String(document.getElementById('" + uuid + "-inner').__marker);"
				+ "})()"), "setSclass must patch the root node only");

		// setZclass: the memo is cleared and the mold re-runs, so the sub-element is a NEW
		// node (the marker is gone) carrying the new scoped class. If setSclass had also
		// rerendered, the assertion above would already have lost the marker - which is
		// what makes this pair able to fail.
		getEval("(function(){zk.Widget.$('$sub').setZclass('my-label');return '';})()");
		waitResponse();

		assertEquals("bar my-label|my-label-inner|undefined", getEval("(function(){"
				+ "return document.getElementById('" + uuid + "').className + '|'"
				+ "     + document.getElementById('" + uuid + "-inner').className + '|'"
				+ "     + String(document.getElementById('" + uuid + "-inner').__marker);"
				+ "})()"), "setZclass must clear the $s() memo and re-run the mold");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- F1

	@Test
	@DisplayName("$s(subId) XML-encodes the zclass, so a zclass containing markup cannot break out of the class attribute")
	public void theScopedClassIsXmlEncoded() {
		connect(PAGE);
		waitResponse();

		// A DETACHED widget, so the page the other tests read stays untouched: setZclass on
		// an unbound widget only resets the memo (rerender short-circuits without a desktop).
		// Control first - without it, an encoder that returned '' for everything would pass.
		assertEquals("z-simplelabel-inner", getEval(
				"(function(){var w = new com.foo.SimpleLabel(); return w.$s('inner');})()"),
				"control: a fresh widget must derive the documented default scoped class");

		assertEquals("a&lt;b-inner", getEval("(function(){"
				+ "var w = new com.foo.SimpleLabel();"
				+ "w.setZclass('a<b');"
				+ "return w.$s('inner');"
				+ "})()"), "$s() did not XML-encode the zclass, so a mold could emit unbalanced markup");

		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- F6

	@Test
	@DisplayName("domClass_() re-appends a z-flex class found on the live node, so its output is not the same at first paint as after binding")
	public void domClassReAppendsFlexClassesOnceBound() {
		connect(PAGE);
		waitResponse();

		// The rendered class attribute at FIRST paint carries no z-flex class: that is the
		// "before" the book contrasts with.
		assertEquals("z-simplelabel", jq("$bare").attr("class"), "precondition: only the zclass is rendered");

		// One probe, both halves of the claim: what domClass_() answers with no flex class on
		// the live node, and what it answers with one. The class is removed again so the rest
		// of the page is left as it was found.
		//
		// The count is deliberately NOT asserted. Measured on 10.3.0.1 the append happens
		// TWICE ("z-simplelabel z-flex z-flex") because the engine applies the same loop in
		// two places - zk.Widget.prototype.domClass_ and a zk.augment() override of it that
		// calls the original first (zk-10.3.0.1.jar!web/js/zk/index.src.js:31191-31209 and
		// :25317-25333). Duplicating a class in a class attribute is idempotent in CSS, and
		// the book claims only that the output DIFFERS once bound - which is what is asserted.
		String observed = getEval("(function(){"
				+ "var w = zk.Widget.$('$bare'), n = w.$n();"
				+ "var before = w.domClass_();"
				+ "jq(n).addClass('z-flex');"
				+ "var after = w.domClass_();"
				+ "jq(n).removeClass('z-flex');"
				+ "return before + '|' + after;"
				+ "})()");
		String before = observed.substring(0, observed.indexOf('|'));
		String after = observed.substring(observed.indexOf('|') + 1);

		assertEquals("z-simplelabel", before,
				"with no flex class on the node, domClass_() must be exactly the zclass: " + observed);
		assertTrue(after.contains("z-flex"),
				"domClass_() did not re-append the z-flex class found on the live node: " + observed);
		assertTrue(after.startsWith("z-simplelabel"),
				"domClass_() dropped the zclass while re-appending the flex class: " + observed);
		assertFalse(after.equals(before),
				"domClass_() returned the same string before and after the flex class appeared,"
						+ " so it IS a pure function of the property values - contradicting the book");

		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ F11

	@Test
	@DisplayName("a missed $n(subId) is cached as 'n/a' for the lifetime of the binding, and a rerender starts a new binding with an empty cache (the book says a rerender does NOT clear it)")
	public void aMissedSubNodeLookupIsCachedUntilTheWidgetIsRebound() {
		connect(PAGE);
		waitResponse();

		// The hazard itself: one miss poisons every later call within the same binding.
		assertEquals("true|n/a|true", getEval("(function(){"
				+ "var w = zk.Widget.$('$bare');"
				+ "var first = (w.$n('nosuch') === undefined);"
				+ "var memo = w._subnodes['nosuch'];"
				+ "var second = (w.$n('nosuch') === undefined);"
				+ "return String(first) + '|' + memo + '|' + String(second);"
				+ "})()"), "a missed $n(subId) lookup is not negatively cached as documented");

		// The BOUNDARY of that hazard, and a place where the book is wrong.
		//
		// addressing_sub_elements.md says the entry "is discarded only when the widget's
		// cache is cleared on detach, NOT on the next rerender". Measured here on 10.3.0.1,
		// a rerender DOES discard it: rerender(-1) replaces the widget's HTML, the old DOM
		// goes through Widget.removeHTML_, and that calls clearCache()
		// (zk-10.3.0.1.jar!web/js/zk/index.src.js:31569-31572). This assertion pins the
		// observed behaviour; the failing form ("n/a") is reported to the doc writer.
		//
		// The page's practical rule is unaffected either way: a miss poisons the lookup for
		// the lifetime of the binding, so make the first $n(subId) call in bind_() or later.
		getEval("(function(){zk.Widget.$('$bare').rerender(-1);return '';})()");
		waitResponse();

		assertEquals("undefined", getEval("String(zk.Widget.$('$bare')._subnodes['nosuch'])"),
				"the negative cache entry survived a rerender; if this is now 'n/a' the book's"
						+ " 'not on the next rerender' has become true and this test should say so");
		// ...and the sub-element is resolvable again after the rebind, i.e. the widget is
		// not permanently broken by the earlier miss.
		assertEquals(uuidOf("bare") + "-inner", getEval("zk.Widget.$('$bare').$n('inner').id"),
				"$n('inner') no longer resolves after the rerender");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ F13

	@Test
	@DisplayName("a bound widget's uuid cannot be changed, because its sub-elements already carry it")
	public void theUuidIsImmutableOnceTheWidgetIsBound() {
		connect(PAGE);
		waitResponse();

		String uuid = uuidOf("bare");

		// The sub-element really does carry the uuid - that is the REASON the book gives,
		// and without this the assertion below would be about an arbitrary restriction.
		assertTrue(getEval("document.getElementById('" + uuid + "-inner').id").startsWith(uuid),
				"the sub-element's id does not start with the widget's uuid");

		assertEquals("threw:id immutable after bound", getEval("(function(){"
				+ "var w = zk.Widget.$('$bare');"
				+ "try { zk._wgtutl.setUuid(w, 'zzz1'); return 'no-throw:' + w.uuid; }"
				+ "catch (e) { return 'threw:' + String(e); }"
				+ "})()"), "a bound widget accepted a new uuid, which would orphan every sub-element");

		// Nothing partially happened: the widget and its DOM still agree.
		assertEquals(uuid, uuidOf("bare"), "the uuid changed although the call threw");
		assertTrue(jq("$bare").exists(), "the widget's root element is no longer resolvable");

		// Control: the guard is about being BOUND, not about setUuid never working. An
		// unattached widget has no sub-nodes to orphan and accepts the change.
		assertEquals("true", getEval("(function(){"
				+ "var w = new com.foo.SimpleLabel();"
				+ "zk._wgtutl.setUuid(w, 'zzz1');"
				+ "return String(w.uuid === 'zzz1');"
				+ "})()"), "control: an unbound widget must accept a uuid change, or the test above proves nothing");

		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ F24

	@Test
	@DisplayName("zk.loadCSS(href, id, media, callback) injects a <link rel=stylesheet> into the head from the client")
	public void loadCssInjectsALinkFromTheClient() {
		connect(PAGE);
		waitResponse();

		// Control: the id must not already exist, or "the link appeared" proves nothing.
		assertEquals("true", getEval("String(document.getElementById('probecss') == null)"),
				"control: the probe id is already taken");

		assertEquals("LINK|stylesheet|text/css|print|true", getEval("(function(){"
				+ "zk.loadCSS('data:text/css,.probe-loadcss{color:rgb(1,2,3)}', 'probecss', 'print');"
				+ "var l = document.getElementById('probecss');"
				+ "if (!l) return 'no-link';"
				+ "return [l.tagName, l.rel, l.type, l.media, String(l.parentNode === document.head)].join('|');"
				+ "})()"), "zk.loadCSS did not inject the documented <link> into the head");

		// It is an escape hatch, not the normal path: the component's own stylesheet still
		// arrives through the language's ONE aggregated response (see StylingIT), so this
		// injected link is the only extra one on the page.
		assertEquals(1, jq("link[href*=\"zk.wcs\"]").length(),
				"the aggregated stylesheet link is no longer exactly one");

		assertNoJSError();
	}

	// ---------------------------------------------------------------------- sanity guard

	@Test
	@DisplayName("the fixture renders every widget these tests read, with the documented classes")
	public void theFixtureRendersWhatTheOtherTestsAssume() {
		connect(PAGE);
		waitResponse();

		// A cheap guard so that a broken fixture reports itself rather than making one of
		// the probes above fail with an unrelated message.
		for (String id : new String[] { "bare", "full", "attrs", "markup", "sub" }) {
			assertTrue(jq("$" + id).exists(), id + ": the fixture rendered no DOM for this component");
			assertTrue(jq("$" + id).hasClass("z-simplelabel"), id + ": the root element lost the zclass");
			assertEquals("true", getEval("String(!!zk.Widget.$('$" + id + "').$n('inner'))"),
					id + ": the mold emitted no <uuid>-inner sub-element");
		}
		assertFalse(jq("$bare").hasClass("foo"), "the fixture's #bare must have no sclass");

		assertNoZKError();
		assertNoJSError();
	}
}
