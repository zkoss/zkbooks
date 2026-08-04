/* SubElementContractIT.java

	THE THREE SUB-ELEMENT ANTI-PATTERNS, AND ONE RECIPE, that the book states and nothing
	in the suite exercised:

	  - the_mold_file_form.md: "When the root element needs another class, override
	    domClass_(no) and append this.$s('...') to the inherited result."             (U9)
	  - zclass_and_scoped_class_names.md: "[setSclass] is fine while sub-elements use
	    $s(), and it is a bug the day you copy a class onto a sub-element by hand - then
	    override updateDomClass_() so the copy is refreshed too."                    (U10)
	  - addressing_sub_elements.md: "Patching a sub-node this way is far cheaper than
	    this.rerender() and it preserves focus and scroll position."                 (U11)
	  - addressing_sub_elements.md: "A hard-coded id=\"inner\" fails twice over, and
	    neither failure announces itself. $n('inner') looks for <uuid>-inner, finds
	    nothing, and returns undefined; and the second instance of your component on the
	    page produces a duplicate DOM id, so whichever code does find the element finds
	    the wrong one."                                                              (U12)

	ANTI-PATTERN QUARANTINE. Every wrong mold below exists ONLY as a Java string literal
	in this file, installed at runtime through zkmld exactly as WpdExtendlet would deliver
	it - the idiom DomAttrsIT already established. None of them is a file, so a reader
	browsing the component cannot find one, and the book cannot extract one as a snippet.
	NEVER copy the strings named HARDCODED_ID_MOLD or HAND_COPIED_CLASS_MOLD: each exists
	to prove a failure mode.

	The page is its own fixture (src/test/webapp/subelements.zul) because these probes
	mutate widgets in place, and three other suites assert exact class strings and ids
	against styling.zul, moldforms.zul and styling-contract.zul. WebDriverTestCase quits
	the browser after every test method, so no probe survives into another test.

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

public class SubElementContractIT extends WebDriverTestCase {

	private static final String PAGE = "/subelements.zul";

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	/** The className of the widget's {@code -inner} sub-element, read from the live DOM. */
	private String innerClassOf(String zkId) {
		return getEval("(function(){var n=document.getElementById('" + uuidOf(zkId) + "-inner');"
				+ "return n ? n.className : 'NO-SUCH-NODE';})()");
	}

	// ------------------------------------------------------------------------------ U12

	@Test
	@DisplayName("a hard-coded id=\"inner\" fails twice over: $n('inner') returns undefined, and a second instance duplicates the DOM id so a lookup finds the wrong element - and neither failure announces itself")
	public void aHardCodedSubElementIdFailsTwiceOver() {
		// connect() MUST be first: it launches the browser and publishes the driver to the
		// ThreadLocal that jq()/getEval() read.
		connect(PAGE);
		waitResponse();

		String ua = uuidOf("dupa"), ub = uuidOf("dupb");
		assertNotEquals(ua, ub, "the two instances share a uuid, so this page cannot show a collision");

		// THE CONTROL: with the shipped mold - which writes id="<uuid>-inner" - both halves
		// are fine. Without this the assertions after the mold swap could be green on a page
		// where nothing ever worked.
		assertEquals(ua + "-inner", getEval("zk.Widget.$('$dupa').$n('inner').id"),
				"control: $n('inner') must resolve the node the shipped mold wrote");
		assertEquals(ub + "-inner", getEval("zk.Widget.$('$dupb').$n('inner').id"),
				"control: the second instance must have its OWN sub-element id");
		assertEquals("0", idInnerCount(),
				"control: nothing on the page carries the hard-coded id yet");

		// Install the mistake on BOTH instances, in the shape WpdExtendlet delivers a mold.
		assertEquals("function", getEval(installMold("hardcodedid", HARDCODED_ID_MOLD)),
				"zkmld did not accept the probe mold");
		assertEquals("true", getEval(
				"String(com.foo.SimpleLabel.molds['hardcodedid'].toString().indexOf('id=\"inner\"') >= 0)"),
				"the installed mold does not contain the hard-coded id at all, so this test proves nothing");
		assertEquals("hardcodedid|hardcodedid", switchMold("hardcodedid", "dupa", "dupb"));
		waitResponse();

		// FAILURE 1: "$n('inner') looks for <uuid>-inner, finds nothing, and returns
		// undefined" - and the miss is negatively cached, so it stays broken.
		assertEquals("true", getEval("String(zk.Widget.$('$dupb').$n('inner') === undefined)"),
				"$n('inner') resolved something although the mold wrote a hard-coded id");
		assertEquals("n/a", getEval("String(zk.Widget.$('$dupb')._subnodes.inner)"),
				"the miss was not memoized as 'n/a', so a later call could still succeed");

		// FAILURE 2: "the second instance of your component on the page produces a duplicate
		// DOM id, so whichever code does find the element finds the wrong one."
		assertEquals("2", idInnerCount(), "the two instances did not produce a duplicate DOM id");
		assertEquals("true", getEval("String(document.getElementById('" + ua + "')"
				+ ".contains(document.getElementById('inner')))"),
				"document.getElementById('inner') is not inside the FIRST instance");
		assertEquals("false", getEval("String(document.getElementById('" + ub + "')"
				+ ".contains(document.getElementById('inner')))"),
				"document.getElementById('inner') resolved the second instance's own element,"
						+ " so the ids are not colliding after all");

		// The element really rendered - the failure is not "nothing appeared".
		assertEquals("Hi", getEval("document.getElementById('inner').textContent"),
				"the probe mold rendered no text, so the DOM assertions above are about the wrong element");

		// "neither failure announces itself": nothing is logged, nothing throws.
		assertNoZKError();
		assertNoJSError();
	}

	/** How many elements on the page carry the literal id {@code inner}. */
	private String idInnerCount() {
		return getEval("String(Array.prototype.filter.call(document.getElementsByTagName('span'),"
				+ " function(n){return n.id == 'inner';}).length)");
	}

	// ------------------------------------------------------------------------------ U10

	@Test
	@DisplayName("setSclass patches only the root, so a class copied onto a sub-element by hand goes stale - overriding updateDomClass_() is what refreshes the copy")
	public void aHandCopiedClassGoesStaleUnlessUpdateDomClassIsOverridden() {
		connect(PAGE);
		waitResponse();

		// Both widgets start with sclass="one", so a stale copy is a visible VALUE rather
		// than an absence.
		assertEquals("one z-simplelabel", jq("$copybug").attr("class"),
				"precondition: the fixture must render sclass then zclass");

		// The anti-pattern: a mold that copies the sclass onto the sub-element by hand.
		assertEquals("function", getEval(installMold("handcopied", HAND_COPIED_CLASS_MOLD)),
				"zkmld did not accept the probe mold");
		assertEquals("handcopied|handcopied", switchMold("handcopied", "copybug", "copyfix"));
		waitResponse();

		assertEquals("z-simplelabel-inner one", innerClassOf("copybug"),
				"precondition: the mold must have copied the sclass onto the sub-element");
		assertEquals("z-simplelabel-inner one", innerClassOf("copyfix"),
				"precondition: the mold must have copied the sclass onto the sub-element");

		// The documented remedy, on ONE of the two only - so the pair is a controlled
		// experiment and the difference is exactly the override.
		assertEquals("function", getEval(UPDATE_DOM_CLASS_REMEDY),
				"the updateDomClass_ override was not installed");

		getEval("(function(){zk.Widget.$('$copybug').setSclass('two');"
				+ "zk.Widget.$('$copyfix').setSclass('two');return '';})()");

		// setSclass reached both ROOT nodes - that half needs no extra code...
		assertEquals("two z-simplelabel", jq("$copybug").attr("class"),
				"setSclass did not rewrite the root node's className");
		assertEquals("two z-simplelabel", jq("$copyfix").attr("class"),
				"setSclass did not rewrite the root node's className");

		// ...but "nothing else in your subtree is touched", so the hand-made copy is stale.
		assertEquals("z-simplelabel-inner one", innerClassOf("copybug"),
				"the hand-copied class followed setSclass by itself, so the book's warning is wrong");

		// ...unless updateDomClass_ refreshes it, which is the recipe the page gives.
		assertEquals("z-simplelabel-inner two", innerClassOf("copyfix"),
				"the updateDomClass_ override did not refresh the hand-made copy");

		// And the $s()-derived part of the class is untouched in both: only the copy is the
		// problem, which is why "that is fine while sub-elements use $s()".
		assertTrue(innerClassOf("copybug").startsWith("z-simplelabel-inner"),
				"the $s('inner') class was lost");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- U9

	@Test
	@DisplayName("when the root element needs another class, override domClass_(no) and append this.$s('...') to the inherited result - and that class DOES reach the DOM")
	public void domClassOverrideAppendsAScopedClassThatReachesTheDom() {
		connect(PAGE);
		waitResponse();

		// (a) The class-level recipe, on a DETACHED widget so the page is untouched. The
		// probe reports the widget name too: the appended class must follow the SUBCLASS's
		// own zclass, which is what makes $s('extra') the right way to write it.
		assertEquals("simplelabel|z-simplelabel", getEval("(function(){"
				+ "var w = new com.foo.SimpleLabel();"
				+ "return w.widgetName + '|' + w.domClass_();})()"),
				"control: the inherited domClass_() must be exactly the zclass");
		assertEquals("extra|z-extra z-extra-extra", getEval(DETACHED_SUBCLASS_PROBE),
				"the documented domClass_ override did not return the inherited result plus $s('extra')");

		// (b) The DOM half, on a BOUND widget. A runtime subclass has no molds map, so the
		// override goes on the instance - the semantics under test are the same: whatever
		// domClass_() returns is what domAttrs_() writes.
		assertEquals("z-simplelabel", jq("$extra").attr("class"),
				"precondition: the root element must start out carrying only the zclass");

		getEval("(function(){var w = zk.Widget.$('$extra'), inherited = w.domClass_;"
				+ "w.domClass_ = function (no) { return inherited.apply(this, arguments) + ' ' + this.$s('extra'); };"
				+ "w.rerender(-1);return '';})()");
		waitResponse();

		// The pointed contrast with DomAttrsIT.aSecondClassAttributeNeverReachesTheDom,
		// where a hand-written second class attribute is silently discarded: THIS route
		// really does put the extra class on the element.
		assertEquals("z-simplelabel z-simplelabel-extra", jq("$extra").attr("class"),
				"the class appended in domClass_() did not reach the root element");
		assertTrue(jq("$extra").hasClass("z-simplelabel-extra"),
				"the appended scoped class is not on the element");
		// One class attribute, one copy of the appended class: the override APPENDS to the
		// inherited result rather than writing a second attribute (which is what the same
		// page warns against, and what DomAttrsIT shows being silently discarded).
		assertEquals("1", getEval("String(document.getElementById('" + uuidOf("extra")
				+ "').getAttributeNames().filter(function(n){return n == 'class';}).length)"),
				"the root element carries more than one class attribute");

		// The sub-element contract is unaffected: the override is about the ROOT.
		assertEquals(uuidOf("extra") + "-inner", getEval("zk.Widget.$('$extra').$n('inner').id"),
				"the rerender lost the sub-element contract");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ U11

	@Test
	@DisplayName("patching a sub-node leaves the rest of the widget's DOM in place: after a value change the root is the SAME node and still document.activeElement, while rerender() replaces the subtree")
	public void patchingASubNodeKeepsTheRootNodeAndItsFocus() {
		connect(PAGE);
		waitResponse();

		// tabindex="0" is what makes the root focusable, and therefore what makes "preserves
		// focus" an observable rather than an adjective.
		assertEquals("true|M1|0", getEval("(function(){"
				+ "var n = zk.Widget.$('$focusable').$n();"
				+ "n.__marker = 'M1';"
				+ "n.focus();"
				+ "return String(document.activeElement === n) + '|' + n.__marker"
				+ "     + '|' + n.getAttribute('tabindex');})()"),
				"precondition: the fixture's root element must be focusable and take the focus");

		// THE CLAIM: the property hook patches the text node, so the root element is the same
		// object, the marker written on it survives, and the focus is still there.
		assertEquals("Changed|M1|true|true", getEval("(function(){"
				+ "var w = zk.Widget.$('$focusable'), n = w.$n();"
				+ "w.setValue('Changed');"
				+ "return w.$n('inner').innerHTML + '|' + String(n.__marker)"
				+ "     + '|' + String(w.$n() === n)"
				+ "     + '|' + String(document.activeElement === n);})()"),
				"a value change did not patch the sub-node in place: either the root node was replaced"
						+ " or the focus was lost");

		// The contrast the page draws: rerender() replaces the whole subtree, so the node the
		// marker was on is gone.
		//
		// NOT asserted here, deliberately: whether the FOCUS is gone after a rerender.
		// zk.Widget.replaceHTML backs the current focus up and restores it (_bkFocus /
		// _rsFocus in zk/widget.ts), so "the focus is gone" is not a property of rerender()
		// that this suite can claim. What rerender() demonstrably costs is the subtree.
		assertEquals("Changed|undefined|false", getEval("(function(){"
				+ "var w = zk.Widget.$('$focusable'), before = w.$n();"
				+ "w.rerender(-1);"
				+ "var after = w.$n();"
				+ "return w.$n('inner').innerHTML + '|' + String(after.__marker)"
				+ "     + '|' + String(after === before);})()"),
				"rerender() did NOT replace the root element, so patching a sub-node saves nothing");

		assertNoZKError();
		assertNoJSError();
	}

	// --------------------------------------------------------------------------- probes

	/**
	 * ANTI-PATTERN. A mold that hard-codes its sub-element id. NEVER copy this: it exists
	 * to prove the two failures the book names.
	 */
	private static final String HARDCODED_ID_MOLD = "function hardcodedid$mold$(out) {"
			+ "  out.push('<span', this.domAttrs_(), '>',"
			+ "    '<span id=\"inner\" class=\"', this.$s('inner'), '\">',"
			+ "    zUtl.encodeXML(this.getValue()),"
			+ "    '</span>',"
			+ "    '</span>');"
			+ "}";

	/**
	 * ANTI-PATTERN. A mold that copies the sclass onto its sub-element by hand instead of
	 * deriving the class from {@code $s()}. NEVER copy this.
	 */
	private static final String HAND_COPIED_CLASS_MOLD = "function handcopied$mold$(out) {"
			+ "  out.push('<span', this.domAttrs_(), '>',"
			+ "    '<span id=\"', this.uuid, '-inner\" class=\"', this.$s('inner'), ' ', this.getSclass(), '\">',"
			+ "    zUtl.encodeXML(this.getValue()),"
			+ "    '</span>',"
			+ "    '</span>');"
			+ "}";

	/** The remedy the book prescribes for the hand-made copy, on #copyfix only. */
	private static final String UPDATE_DOM_CLASS_REMEDY = "(function(){"
			+ "var w = zk.Widget.$('$copyfix'), inherited = w.updateDomClass_;"
			+ "w.updateDomClass_ = function () {"
			+ "  inherited.apply(this, arguments);"
			+ "  var n = this.$n('inner');"
			+ "  if (n) n.className = this.$s('inner') + ' ' + this.getSclass();"
			+ "};"
			+ "return typeof w.updateDomClass_;"
			+ "})()";

	/** The class-level form of the domClass_ recipe, exactly as the book spells it. */
	private static final String DETACHED_SUBCLASS_PROBE = "(function(){"
			+ "zk.$package('labprobe');"
			+ "labprobe.Extra = zk.$extends(com.foo.SimpleLabel, {"
			+ "  domClass_: function (no) {"
			+ "    return this.$supers('domClass_', arguments) + ' ' + this.$s('extra');"
			+ "  }"
			+ "});"
			// Widget.register does zk.$import(name), so the class must already exist under
			// that global name. It also sets widgetName, hence the zclass 'z-extra'.
			+ "zkreg('labprobe.Extra');"
			+ "var w = new labprobe.Extra();"
			+ "return w.widgetName + '|' + w.domClass_();"
			+ "})()";

	/**
	 * Installs one extra mold on {@code com.foo.SimpleLabel} in the exact shape
	 * {@code WpdExtendlet} emits, keeping the molds the package already delivered
	 * ({@code zkmld} REPLACES the map), and returns {@code typeof} the new entry.
	 */
	private static String installMold(String moldName, String functionExpression) {
		return "(function(){"
				+ "var cls = com.foo.SimpleLabel;"
				+ "zk._m = {};"
				+ "for (var nm in cls.molds) zk._m[nm] = cls.molds[nm];"
				+ "zk._m['" + moldName + "'] = " + functionExpression + "\n;"
				+ "zkmld(cls, zk._m);"
				+ "return typeof cls.molds['" + moldName + "'];"
				+ "})()";
	}

	/** Selects {@code moldName} on each component id and reports the mold each ended on. */
	private String switchMold(String moldName, String... zkIds) {
		StringBuilder ids = new StringBuilder();
		for (String id : zkIds)
			ids.append(ids.length() == 0 ? "'" : ",'").append(id).append("'");
		return getEval("(function(){var out = [];"
				+ "[" + ids + "].forEach(function(id){"
				+ "  var w = zk.Widget.$('$' + id);"
				+ "  w.setMold('" + moldName + "');"
				+ "  w.rerender(-1);"
				+ "  out.push(w.getMold());"
				+ "});"
				+ "return out.join('|');})()");
	}
}
