/* DomAttrsIT.java

	"Let domAttrs_() own the root element" - the claims of
	zk_component_dev_essentials/the_mold_file_form.md that nothing in the suite asserted,
	checked in a real browser.

	Each @DisplayName is one sentence of that page:

	  - "domAttrs_() emits the root element's attributes for you - in order: id, style,
	     class, title, tabindex, and then any extra attributes the server set through the
	     client attribute namespace. Each appears only when it has a value, and each is
	     preceded by a single space."
	  - "Writing out.push('<div', this.domAttrs_(), ' class="my-widget">') produces an
	     element with two class attributes; a spec-compliant HTML parser keeps the first
	     and discards the second, so my-widget never reaches the DOM and nothing reports
	     an error."
	  - "this.domAttrs_({domClass: 1}) emits everything except class."
	  - "a mold that writes its own id and class silently disables setClientAttribute for
	     every user of your component."
	  - "Text that arrived from the server has to be encoded on its way into out, or a
	     value containing markup becomes a cross-site-scripting hole."
	  - "a statement on the right-hand side of = is a syntax error."

	Why *IT and not ZATS: every one of them is either a string the client engine computes,
	an attribute of a rendered element, or what a JS parser does with the widget package
	text. ZATS renders no HTML and runs no JavaScript, so all of it is invisible there.

	Runs under failsafe in `mvn verify`. Needs a local Chrome; runs headless.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;

public class DomAttrsIT extends WebDriverTestCase {

	private static final String PAGE = "/styling-contract.zul";

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	/**
	 * Reads a client-side string with its leading and trailing spaces intact.
	 *
	 * <p>{@code domAttrs_()} is documented to START with a space, so the value must not be
	 * trimmed anywhere on the way back. The brackets make any trimming visible instead of
	 * silently satisfying the assertion.
	 */
	private String bracketed(String jsExpression) {
		String v = getEval("'[' + (" + jsExpression + ") + ']'");
		assertTrue(v.startsWith("[") && v.endsWith("]"), "unexpected probe result: " + v);
		return v.substring(1, v.length() - 1);
	}

	// ------------------------------------------------------------------------------ F7

	@Test
	@DisplayName("domAttrs_() emits id, style, class, title, tabindex in that order, each preceded by a single space, and each only when it has a value")
	public void domAttrsEmitsTheDocumentedAttributesInTheDocumentedOrder() {
		// connect() MUST be first: it launches the browser and publishes the driver to the
		// ThreadLocal that jq()/getEval() read.
		connect(PAGE);
		waitResponse();

		// (a) "each only when it has a value". #bare has nothing but a value, so the whole
		// string is id + class - asserted exactly, because an extra attribute here would
		// mean domAttrs_() emits something the book does not mention.
		String bare = bracketed("zk.Widget.$('$bare').domAttrs_()");
		assertEquals(" id=\"" + uuidOf("bare") + "\" class=\"z-simplelabel\"", bare,
				"domAttrs_() on a widget with only a value must emit exactly id and class");

		// (b) the order, on a widget that has all five. Asserted by position rather than as
		// one literal so the test says WHICH pair is out of order when it breaks.
		String full = bracketed("zk.Widget.$('$full').domAttrs_()");
		int id = full.indexOf(" id=\""), style = full.indexOf(" style=\""), cls = full.indexOf(" class=\""),
				title = full.indexOf(" title=\""), tabindex = full.indexOf(" tabindex=\"");
		assertTrue(id == 0, "domAttrs_() must start with a single space then id=, got: " + full);
		assertTrue(style > id, "style must follow id: " + full);
		assertTrue(cls > style, "class must follow style: " + full);
		assertTrue(title > cls, "title must follow class: " + full);
		assertTrue(tabindex > title, "tabindex must follow title: " + full);

		// (c) "each is preceded by a single space, so '<span', this.domAttrs_(), '>' is
		// already well-formed": no double space anywhere, and no trailing space.
		assertFalse(full.contains("  "), "an attribute is preceded by more than one space: " + full);
		assertFalse(full.endsWith(" "), "domAttrs_() ends with a space: " + full);

		// (d) the values really are the component's, so (b) cannot be green on an empty
		// string full of -1s. Note class = sclass + ' ' + zclass, the order F4 pins.
		assertTrue(full.contains(" class=\"foo z-simplelabel\""), "class must be sclass then zclass: " + full);
		assertTrue(full.contains(" title=\"tip\""), "title comes from tooltiptext: " + full);
		assertTrue(full.contains(" tabindex=\"3\""), "tabindex was not emitted: " + full);
		assertTrue(full.contains(" style=\"width:100px"), "style was not emitted: " + full);

		// (e) and the string really is what the ROOT ELEMENT carries - the mold pushed it
		// verbatim, so the browser parsed exactly these attributes.
		assertEquals("tip", getEval("document.getElementById('" + uuidOf("full") + "').getAttribute('title')"),
				"the title domAttrs_() emitted is not on the rendered element");
		assertEquals("3", getEval("document.getElementById('" + uuidOf("full") + "').getAttribute('tabindex')"),
				"the tabindex domAttrs_() emitted is not on the rendered element");
		assertEquals("foo z-simplelabel", jq("$full").attr("class"),
				"the class domAttrs_() emitted is not on the rendered element");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ F8

	@Test
	@DisplayName("the argument of domAttrs_() is an EXCLUSION map: domAttrs_({domClass: 1}) emits everything except class")
	public void theExclusionMapRemovesOnlyTheNamedAttribute() {
		connect(PAGE);
		waitResponse();

		// Control: without the argument the class IS there. Without this line the assertion
		// below would also pass on a widget that never had a class.
		assertTrue(bracketed("zk.Widget.$('$full').domAttrs_()").contains(" class=\""),
				"control: domAttrs_() with no argument must emit class");

		String excluded = bracketed("zk.Widget.$('$full').domAttrs_({domClass: 1})");
		assertFalse(excluded.contains(" class=\""),
				"domAttrs_({domClass:1}) still emitted class, so the map is not an exclusion map: " + excluded);
		// Everything ELSE survives - that is what "emits everything except class" means.
		assertTrue(excluded.contains(" id=\""), "the exclusion map dropped id as well: " + excluded);
		assertTrue(excluded.contains(" style=\""), "the exclusion map dropped style as well: " + excluded);
		assertTrue(excluded.contains(" title=\""), "the exclusion map dropped title as well: " + excluded);
		assertTrue(excluded.contains(" tabindex=\""), "the exclusion map dropped tabindex as well: " + excluded);

		// An empty map is behaviourally identical to no argument: the keys are opt-in, and
		// an unrecognised key is ignored rather than an error. A reader who misspells
		// "domClass" gets the class back, silently - which is worth pinning.
		assertEquals(bracketed("zk.Widget.$('$full').domAttrs_()"),
				bracketed("zk.Widget.$('$full').domAttrs_({})"),
				"an empty exclusion map changed the output");
		assertTrue(bracketed("zk.Widget.$('$full').domAttrs_({domclass: 1})").contains(" class=\""),
				"a misspelled exclusion key was honoured, so the key names are not exact");

		assertNoJSError();
	}

	// ----------------------------------------------------------------------- two classes

	@Test
	@DisplayName("a mold that writes a second class attribute next to domAttrs_() loses it: the parser keeps the first and nothing reports an error")
	public void aSecondClassAttributeNeverReachesTheDom() {
		connect(PAGE);
		waitResponse();

		String before = jq("$bare").attr("class");
		assertEquals("z-simplelabel", before, "precondition: the default mold emits exactly the zclass");

		// Install the mistake the book names, in the shape WpdExtendlet delivers a mold.
		// The mold really does contain the second attribute - asserted below - so this
		// cannot pass because the probe forgot to make the mistake.
		assertEquals("function", getEval(installMold("twoclass",
				"function twoclass$mold$(out) {"
						+ "  out.push('<span', this.domAttrs_(), ' class=\"my-widget\">',"
						+ "    '<span id=\"', this.uuid, '-inner\" class=\"', this.$s('inner'), '\">',"
						+ "    zUtl.encodeXML(this.getValue()),"
						+ "    '</span>',"
						+ "    '</span>');"
						+ "}")),
				"zkmld did not accept the mold");
		assertEquals("true", getEval("String(com.foo.SimpleLabel.molds['twoclass'].toString().indexOf('my-widget') >= 0)"),
				"the installed mold does not contain the second class attribute at all");

		assertEquals("twoclass", getEval(
				"(function(){var w=zk.Widget.$('$bare');w.setMold('twoclass');w.rerender(-1);return w.getMold();})()"));
		waitResponse();

		// The element exists and rendered - so the duplicate attribute was not fatal.
		assertTrue(jq("$bare").exists(), "the mold rendered no DOM at all");
		// ...and the FIRST class attribute won: my-widget is nowhere in the DOM.
		assertEquals("z-simplelabel", jq("$bare").attr("class"),
				"the class attribute is not the one domAttrs_() wrote");
		assertFalse(jq("$bare").hasClass("my-widget"),
				"the hand-written second class attribute reached the DOM, so the book's warning is wrong");
		assertEquals("0", getEval("String(jq('.my-widget').length)"),
				"a .my-widget element exists somewhere on the page");

		// "and nothing reports an error" - the failure really is silent.
		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------ F9

	@Test
	@DisplayName("client attributes reach the DOM only because domAttrs_() appends them: a hand-rolled root element silently drops setClientAttribute")
	public void aHandRolledRootElementSilentlyDropsClientAttributes() {
		connect(PAGE);
		waitResponse();

		// The server sent it: ca:data-role="banner" became domExtraAttrs on the widget.
		// Asserting this first separates "the mold dropped it" from "it never arrived".
		assertTrue(getEval("JSON.stringify(zk.Widget.$('$attrs').domExtraAttrs)").contains("data-role"),
				"the client attribute never reached the widget, so this test cannot say anything about the mold");

		// With domAttrs_() it is on the element.
		assertEquals("banner",
				getEval("document.getElementById('" + uuidOf("attrs") + "').getAttribute('data-role')"),
				"domAttrs_() did not append the server's client attribute");

		// Now the mistake: a mold that writes id and class by hand. Same DOM otherwise.
		assertEquals("function", getEval(installMold("handrolled",
				"function handrolled$mold$(out) {"
						+ "  out.push('<span id=\"' + this.uuid + '\" class=\"' + this.$s() + '\">',"
						+ "    '<span id=\"', this.uuid, '-inner\" class=\"', this.$s('inner'), '\">',"
						+ "    zUtl.encodeXML(this.getValue()),"
						+ "    '</span>',"
						+ "    '</span>');"
						+ "}")),
				"zkmld did not accept the mold");

		assertEquals("handrolled", getEval(
				"(function(){var w=zk.Widget.$('$attrs');w.setMold('handrolled');w.rerender(-1);return w.getMold();})()"));
		waitResponse();

		// The widget still renders and still knows the attribute...
		assertTrue(jq("$attrs").exists(), "the hand-rolled mold rendered no DOM");
		assertEquals("z-simplelabel", jq("$attrs").attr("class"), "the hand-rolled mold wrote no zclass");
		assertTrue(getEval("JSON.stringify(zk.Widget.$('$attrs').domExtraAttrs)").contains("data-role"),
				"the widget lost the client attribute itself, which is not what the book claims");

		// ...but the DOM no longer carries it, and nothing complained. That silence is the
		// whole argument for always calling domAttrs_().
		assertEquals("null",
				getEval("String(document.getElementById('" + uuidOf("attrs") + "').getAttribute('data-role'))"),
				"the hand-rolled root element still carried the client attribute, so the book's warning is wrong");

		assertNoZKError();
		assertNoJSError();
	}

	// --------------------------------------------------------------------- encoding/XSS

	@Test
	@DisplayName("server-supplied text is XML-encoded on its way into out and again by the value hook, so markup in a value renders as text")
	public void serverTextIsEncodedByTheMoldAndByThePropertyHook() {
		connect(PAGE);
		waitResponse();

		String inner = uuidOf("markup") + "-inner";

		// FIRST PAINT, i.e. the mold's zUtl.encodeXML(this.getValue()). The value is
		// "<b>bold</b>": as text it is visible verbatim, and there must be no <b> ELEMENT.
		assertEquals("<b>bold</b>", getEval("document.getElementById('" + inner + "').textContent"),
				"the mold did not render the value as text");
		assertEquals("0", getEval("String(document.getElementById('" + inner + "').getElementsByTagName('b').length)"),
				"the value's markup was parsed as markup: the mold did not encode it, which is an XSS hole");
		assertTrue(getEval("document.getElementById('" + inner + "').innerHTML").contains("&lt;b&gt;"),
				"the rendered HTML does not carry the escaped form of the value");

		// LATER UPDATE, i.e. the $define value hook: n.innerHTML = zUtl.encodeXML(_value).
		// A component that encodes at first paint but not in the hook is a real and common
		// bug, and only this second half sees it.
		// Unquoted attribute values on purpose: they are legal HTML, so an unencoded value
		// would really execute, and they keep this probe free of nested quoting.
		getEval("(function(){zk.Widget.$('$markup').setValue('<i>x</i><img src=x onerror=window.__xss=1>');return '';})()");
		assertEquals("<i>x</i><img src=x onerror=window.__xss=1>",
				getEval("document.getElementById('" + inner + "').textContent"),
				"the value hook did not render the new value as text");
		assertEquals("0", getEval("String(document.getElementById('" + inner + "').getElementsByTagName('i').length)"),
				"the value hook injected markup: it does not encode, which is an XSS hole");
		assertEquals("undefined", getEval("typeof window.__xss"),
				"an injected onerror handler executed: the value hook does not encode");

		assertNoJSError();
	}

	// ------------------------------------------------------------- the mold file's shape

	@Test
	@DisplayName("the mold file is inlined on the right-hand side of an assignment, so a top-level statement in it is a syntax error")
	public void theMoldFileMustBeASingleExpressionOnTheRightHandSide() {
		connect(PAGE);
		waitResponse();

		// The exact context the widget package response builds - see MoldFormIT, which
		// asserts that shape against the real .wpd response:
		//     zk._m['default'] = <the whole mold file, verbatim>
		//     ;
		// Each candidate file body is placed there and the result is PARSED (new Function)
		// and then RUN, so the two failure modes are told apart instead of merged.
		//
		// This probe can fail: it reports the outcome of six shapes, and any change in any
		// one of them changes the string compared below.
		String observed = getEval(SHAPE_PROBE);

		assertEquals("expression=ok"
				+ "|named-expression=ok"
				+ "|parenthesized=ok"
				+ "|trailing-semicolon=ok"
				+ "|var-statement=parse:SyntaxError"
				+ "|import-statement=parse:SyntaxError"
				+ "|module-exports=run:ReferenceError", observed,
				"a mold file shape behaves differently from what the_mold_file_form.md states");

		assertNoJSError();
	}

	/**
	 * Places six candidate mold-file bodies in the widget package response's assignment
	 * context and reports, per shape, {@code ok} / {@code parse:<Error>} / {@code run:<Error>}.
	 */
	private static final String SHAPE_PROBE = "(function(){"
			+ "var shapes = ["
			+ "  ['expression', 'function (out) { out.push(1); }'],"
			+ "  ['named-expression', 'function a$mold$(out) { out.push(1); }'],"
			+ "  ['parenthesized', '(function a$mold$(out) { out.push(1); })'],"
			+ "  ['trailing-semicolon', 'function a$mold$(out) { out.push(1); };'],"
			+ "  ['var-statement', 'var m = function (out) { out.push(1); }'],"
			+ "  ['import-statement', \"import m from 'x'\"],"
			+ "  ['module-exports', 'module.exports = function (out) { out.push(1); }']"
			+ "];"
			+ "var out = [];"
			+ "for (var i = 0; i < shapes.length; i++) {"
			+ "  var name = shapes[i][0], body = shapes[i][1], fn;"
			+ "  var src = \"var zk = {_m: {}};\\nzk._m['default'] = \" + body + \"\\n;\";"
			+ "  try { fn = new Function(src); } catch (e) { out.push(name + '=parse:' + e.name); continue; }"
			+ "  try { fn(); out.push(name + '=ok'); } catch (e) { out.push(name + '=run:' + e.name); }"
			+ "}"
			+ "return out.join('|');"
			+ "})()";

	// ------------------------------------------------------------------------- helpers

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
}
