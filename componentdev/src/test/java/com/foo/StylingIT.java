/* StylingIT.java

	The styling / DOM-class contract of com.foo.SimpleLabel, asserted in a real browser.

	Written from the research note tasks/research/component-dev/styling-dom.md. Every test
	below is one documented claim; the @DisplayName is the claim, so a failure names the
	broken promise.

	Why this file exists at all, given SimpleLabelIT: that suite asserts class NAMES only.
	Nothing in the lab asserted that the component's stylesheet ever REACHES the browser or
	that a single rule of it applies - and a stylesheet that 404s, or that arrives with an
	invalid declaration, is invisible to every source-level and every ZATS assertion. The
	computed-style and zk.wcs-body tests here are that missing layer.

	Runs under failsafe in `mvn verify`. Needs a local Chrome; runs headless.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;
import org.zkoss.test.webdriver.ztl.JQuery;

public class StylingIT extends WebDriverTestCase {

	/** The fixture. Deliberately NOT simplelabel.zul - see styling.zul's own comment. */
	private static final String PAGE = "/styling.zul";

	/**
	 * Selects a widget's SUB-element by its real DOM id.
	 *
	 * <p>{@code jq("$id")} resolves a ZK <em>component</em> id only, so
	 * {@code jq("$plain-inner")} matches nothing at all. The negative half of that claim
	 * is asserted in {@link #subElementsAreUnreachableByComponentIdSelector()}.
	 */
	private JQuery sub(String zkId, String subId) {
		return jq("#" + uuidOf(zkId) + "-" + subId);
	}

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	// ----------------------------------------------------------------- F1 / F3(b)

	@Test
	@DisplayName("$s(sub) is getZclass() + '-' + sub, and with no sclass the root's class attribute is exactly the zclass")
	public void scopedClassIsZclassPlusSubId() {
		// connect() MUST be first: it launches the browser and publishes the driver to the
		// ThreadLocal every jq()/getEval() reads.
		connect(PAGE);
		waitResponse();

		assertTrue(jq("$plain").exists(), "the widget rendered no DOM at all");

		// The server never transmits its getZclass() default (the FIELD _zclass is null -
		// see StylingContractPropertyTest), so this string is the CLIENT's own
		// 'z-' + widgetName. Asserted as an exact string, not hasClass: an extra class
		// here would mean something other than domAttrs_() contributed to the attribute.
		assertEquals("z-simplelabel", jq("$plain").attr("class"),
				"domAttrs_() did not emit exactly the zclass on the root element");

		// $s('inner') == encodeXML(getZclass()) + '-inner'.
		assertEquals("z-simplelabel-inner", sub("plain", "inner").attr("class"),
				"$s('inner') is documented as getZclass() + '-' + sub");

		// The scoped class belongs to the sub-element; the root must not carry it.
		assertFalse(jq("$plain").hasClass("z-simplelabel-inner"),
				"the root element carries the sub-element's scoped class");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------- F2

	@Test
	@DisplayName("the client zclass default ignores the PACKAGE: com.foo.SimpleLabel and labts.SimpleLabel both default to z-simplelabel")
	public void widgetNameIsTheLowercasedLastSegmentOnly() {
		connect(PAGE);
		waitResponse();

		// Two different widget classes in two different packages, one shared simple name.
		// widgetName = clsnm.substring(clsnm.lastIndexOf('.')+1).toLowerCase(), so both
		// derive the same zclass and therefore share every $s() class and every CSS rule.
		assertEquals("true", getEval("!!(window.com && com.foo && com.foo.SimpleLabel)"),
				"the plain-JS widget package did not load");
		assertEquals("true", getEval("!!(window.labts && labts.SimpleLabel)"),
				"the TypeScript widget package did not load");

		assertEquals("z-simplelabel", jq("$plain").attr("class"),
				"com.foo.SimpleLabel did not default to z-simplelabel");
		assertEquals("z-simplelabel", jq("$ts2").attr("class"),
				"labts.SimpleLabel did not default to z-simplelabel - the package must be ignored");
		assertEquals("z-simplelabel-inner", sub("ts2", "inner").attr("class"),
				"the two packages must produce the same $s() strings");

		assertNoJSError();
	}

	// ------------------------------------------------------------------------- F4

	@Test
	@DisplayName("domClass_ emits sclass FIRST and zclass SECOND: sclass=\"foo\" gives class=\"foo z-simplelabel\"")
	public void sclassIsEmittedBeforeZclass() {
		connect(PAGE);
		waitResponse();

		// ZK's own TSDoc says "z-button foo"; the shipped code produces the reverse.
		// This is why the assertion is the exact string rather than two hasClass calls.
		assertEquals("foo z-simplelabel", jq("$styled").attr("class"),
				"the documented order is sclass then zclass");

		assertNoJSError();
	}

	// ---------------------------------------------------------------------- F1(b)

	@Test
	@DisplayName("setZclass clears the $s() memo and re-runs the mold, so a sub-element's scoped class follows the new zclass")
	public void setZclassRebuildsTheScopedClasses() {
		connect(PAGE);
		waitResponse();

		assertEquals("z-simplelabel-inner", sub("plain", "inner").attr("class"),
				"precondition: the sub-element must start out scoped to the default zclass");

		getEval("(function(){zk.Widget.$('$plain').setZclass('my-label');return '';})()");
		waitResponse();

		assertEquals("my-label", jq("$plain").attr("class"),
				"setZclass did not reach the root element");
		// The memo (_subzcls) is emptied ONLY by setZclass, and rerender() regenerates the
		// whole subtree - so a stale "z-simplelabel-inner" here means one of the two did
		// not happen.
		assertEquals("my-label-inner", sub("plain", "inner").attr("class"),
				"the $s() memo was not cleared, or the mold was not re-run");
		assertFalse(sub("plain", "inner").hasClass("z-simplelabel-inner"),
				"the stale scoped class survived setZclass");

		assertNoJSError();
	}

	// ------------------------------------------------------------------- F11 / F12

	@Test
	@DisplayName("jq(\"$id-sub\") can never match a sub-element: only the real DOM id <uuid>-<subId> reaches it")
	public void subElementsAreUnreachableByComponentIdSelector() {
		connect(PAGE);
		waitResponse();

		String uuid = uuidOf("plain");

		// Positive controls FIRST. Without them the negative assertion below would also
		// pass on a page where the component never rendered at all.
		assertTrue(jq("$plain").exists(),
				"positive control: a component id selector must resolve the widget's ROOT node");
		assertTrue(jq("#" + uuid + "-inner").exists(),
				"positive control: the sub-element must exist under the id <uuid>-inner");

		// The load-bearing negative: ZK's $id selector looks for a COMPONENT whose id is
		// the literal string "plain-inner", so it matches nothing.
		assertFalse(jq("$plain-inner").exists(),
				"jq(\"$id-sub\") resolved something; every book snippet using that form would then be legal");

		// $n(subId) resolves exactly the node the mold wrote.
		assertEquals(uuid + "-inner", getEval("zk.Widget.$('$plain').$n('inner').id"),
				"$n(subId) did not resolve the <uuid>-<subId> node the mold emitted");

		assertNoJSError();
	}

	// ------------------------------------------------------- F19(c) / F20 / F21

	@Test
	@DisplayName("the component's <css-uri> stylesheet really reaches the browser and its rules apply, with no *.dsp servlet mapping in web.xml")
	public void theComponentStylesheetAppliesInTheBrowser() {
		connect(PAGE);
		waitResponse();

		// The control element: a span the component stylesheet does not match. If these
		// two read the same as the component's, the probe proves nothing.
		assertEquals("inline", getEval("getComputedStyle(document.getElementById('ctl')).display"),
				"control: an unstyled span must compute to display:inline");
		assertEquals("0px", getEval("getComputedStyle(document.getElementById('ctl')).paddingLeft"),
				"control: an unstyled span must have no padding");

		// .z-simplelabel { display: inline-block }. A <span> with no CSS computes to
		// "inline", so this is red the moment the stylesheet fails to arrive - which is
		// exactly what the (now removed) *.dsp servlet mapping was claimed to prevent.
		assertEquals("inline-block", jq("$plain").css("display"),
				"the <css-uri> stylesheet did not apply to the root element");

		// .z-simplelabel-inner { padding: ... } - a second, differently scoped selector.
		assertNotEquals("0px", sub("plain", "inner").css("padding-left"),
				"the $s('inner') scoped rule did not apply");

		// .z-simplelabel-fancy - the second mold's extra scoped class, styled from the
		// SAME single stylesheet.
		assertNotEquals("0px", sub("deco", "inner").css("border-top-width"),
				"the $s('fancy') scoped rule did not apply");
		assertNotEquals("rgba(0, 0, 0, 0)", sub("deco", "inner").css("background-color"),
				"the $s('fancy') scoped rule did not apply");

		assertNoZKError();
		assertNoJSError();

		// Finally, prove the CAUSAL link rather than a coincidence: drop the one
		// aggregated stylesheet and the rule must stop applying. Without this step the
		// assertions above would also be green if display:inline-block came from anywhere
		// else - which is the whole failure mode this test exists to rule out.
		getEval("(function(){jq('link[href*=\"zk.wcs\"]').remove();return '';})()");
		assertEquals("inline", jq("$plain").css("display"),
				"display:inline-block did not come from the zk.wcs response, so this test proves nothing");
	}

	// ------------------------------------------------------------------------ F19

	@Test
	@DisplayName("<css-uri> is language-wide, not mold-scoped: the stylesheet applies on a page that uses no mold declaring it")
	public void cssUriIsLanguageWideNotMoldScoped() {
		// styling-langwide.zul contains only <simplelabelts>, whose component and mold
		// declare no <css-uri> whatsoever.
		connect("/styling-langwide.zul");
		waitResponse();

		assertEquals("inline", getEval("getComputedStyle(document.getElementById('ctl')).display"),
				"control: an unstyled span must compute to display:inline");

		assertTrue(jq("$ts2").exists(), "the fixture rendered no widget");
		assertEquals("inline-block", jq("$ts2").css("display"),
				"the <css-uri> stylesheet did not load on a page that uses no mold declaring it");

		// Still ONE aggregated response - the .wcs is per LANGUAGE, not per mold or page.
		assertEquals(1, jq("link[href*=\"zk.wcs\"]").length(),
				"expected exactly one <link> to the language's .wcs");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------ F17 / F25

	@Test
	@DisplayName("the shipped --zk-* custom properties are defined on :root, and the component's var(--zk-...) declaration parsed as VALID CSS")
	public void themeFollowingThroughCssCustomProperties() {
		connect(PAGE);
		waitResponse();

		// 842 --zk-* properties ship on :root in norm.css.dsp; a component can
		// theme-follow with plain CSS and no LESS/DSP/build step. An undefined custom
		// property reads back as the empty string, so this assertion can fail.
		String rootValue = getEval(
				"getComputedStyle(document.documentElement).getPropertyValue('--zk-base-content-font-family').trim()");
		assertFalse(rootValue.isEmpty(),
				"--zk-base-content-font-family is not defined on :root, so a component cannot theme-follow with it");

		// CSSOM, i.e. the declaration as the browser PARSED it. This is the assertion the
		// lab lacked: an unresolved ${fontFamilyC} produces "font-family: ;", the parser
		// drops the invalid declaration, and getPropertyValue then reads "" - while every
		// computed-style assertion still passes, because font-family is inherited.
		String declared = getEval(RULE_PROBE);
		assertTrue(declared.contains("display=[inline-block]"),
				"the .z-simplelabel rule is not in the browser's CSSOM at all: " + declared);
		assertFalse(declared.contains("font-family=[]"),
				"the .z-simplelabel rule ships an INVALID font-family declaration (the parser dropped it): " + declared);
		assertTrue(declared.contains("var(--zk-"),
				"the component's font-family does not follow a --zk-* custom property: " + declared);

		// And it resolves: no var() survives into the computed value, and the computed
		// value IS the shipped custom property's value.
		String computed = jq("$plain").css("font-family");
		assertFalse(computed.trim().isEmpty(), "the computed font-family is empty");
		assertFalse(computed.contains("var("), "the custom property never resolved: " + computed);
		assertEquals(unquote(rootValue), unquote(computed),
				"the component's font-family is not the value --zk-base-content-font-family carries");

		// Causality, not coincidence: the control element - which no rule of the component
		// stylesheet matches - computes the UA default instead. If these were equal, the
		// assertion above could be satisfied by plain inheritance and would prove nothing.
		String control = getEval("getComputedStyle(document.getElementById('ctl')).fontFamily");
		assertNotEquals(unquote(control), unquote(computed),
				"the component's font-family is indistinguishable from an unstyled element's");

		assertNoJSError();
	}

	/** Chrome keeps the authored quoting in a font stack; compare without it. */
	private static String unquote(String cssValue) {
		return cssValue.replace("\"", "").replace("'", "").trim();
	}

	/** Reads the {@code .z-simplelabel} rule back out of the browser's CSSOM. */
	private static final String RULE_PROBE = "(function(){"
			+ "var hits=[];"
			+ "for (var i=0;i<document.styleSheets.length;i++){"
			+ "  var rules;try{rules=document.styleSheets[i].cssRules;}catch(e){continue;}"
			+ "  if(!rules) continue;"
			+ "  for (var j=0;j<rules.length;j++){var r=rules[j];"
			+ "    if(r.selectorText && r.selectorText.replace(/\\s/g,'')=='.z-simplelabel')"
			+ "      hits.push('display=['+r.style.getPropertyValue('display')"
			+ "        +'] font-family=['+r.style.getPropertyValue('font-family')+']');"
			+ "  }"
			+ "}"
			+ "return hits.join(' ; ');"
			+ "})()";

	// ------------------------------------------------------------------ F19 / F22

	@Test
	@DisplayName("one <link> to the language's zk.wcs, none for the component's own stylesheet, and the href is <ctx>/zkau/web/<cache-segment>/zul/css/zk.wcs")
	public void oneAggregatedStylesheetLinkAndNoPerComponentLink() {
		connect(PAGE);
		waitResponse();

		// (a) exactly one aggregated stylesheet response for the whole language.
		assertEquals(1, jq("link[href*=\"zk.wcs\"]").length(),
				"expected exactly one <link> to the language's .wcs");

		// (b) <css-uri> is server-side-INCLUDED into that response, so the component's own
		// stylesheet is never a <link> of its own. This is what distinguishes <css-uri>
		// from <stylesheet>.
		assertEquals(0, jq("link[href*=\"simplelabel\"]").length(),
				"the component stylesheet was emitted as its own <link>; <css-uri> must fold into zk.wcs");

		// The ~./ URL shape: <ctxpath> + <update-uri> + /web + one cache-busting segment
		// + the ~./-relative tail.
		String href = jq("link[href*=\"zk.wcs\"]").attr("href");
		assertTrue(href.matches("^\\Q" + getContextPath() + "\\E/zkau/web/[^/]+/zul/css/zk\\.wcs$"),
				"unexpected ~./ URL shape: " + href);

		// The CLIENT-side builder uses its own literal prefix for the same mapping.
		assertTrue(getEval("zk.ajaxResourceURI('/js/com/foo/x.png')").contains("/zkau/web/_zv"),
				"zk.ajaxResourceURI did not build the documented /zkau/web/_zv<build> form");

		assertNoJSError();
	}

	// ------------------------------------------------- F19 / F20b / F25 / F18

	@Test
	@DisplayName("the single zk.wcs response really carries the component's own rules, fully interpreted: no ${...}, no raw <% and no empty declaration")
	public void theAggregatedWcsBodyCarriesTheComponentRules() throws Exception {
		connect(PAGE);
		waitResponse();

		// Self-check of the predicate used at the end of this test: it must be able to see
		// the defect it exists to catch, otherwise this whole test is decoration.
		assertEquals("", declaredValue(".z-simplelabel {\n\tfont-family: ;\n}", "font-family"),
				"the predicate cannot see an empty declaration");
		assertEquals("serif", declaredValue(".z-simplelabel { font-family: serif; }", "font-family"),
				"the predicate cannot read a normal declaration");

		String url = getEval("(function(){var l=jq('link[href*=\"zk.wcs\"]')[0];return l?l.href:'';})()");
		assertTrue(url.startsWith("http"), "could not resolve the zk.wcs URL from the page: " + url);

		String css = httpGet(url);

		// The component's stylesheet was concatenated into the language's one .wcs body.
		assertTrue(css.contains(".z-simplelabel"),
				"the <css-uri> stylesheet was NOT included in zk.wcs");
		assertTrue(css.contains(".z-simplelabel-inner"), "the $s('inner') rule is missing from zk.wcs");
		assertTrue(css.contains(".z-simplelabel-fancy"), "the $s('fancy') rule is missing from zk.wcs");

		// "one request, MANY stylesheets": the same body also carries the language's own
		// norm.css.dsp, which is where the --zk-* custom property the component's rule
		// depends on is defined.
		assertTrue(css.contains("--zk-base-content-font-family"),
				"the aggregated body does not carry the language's own stylesheet, so it is not an aggregation");

		// The DSP interpreter ran in-process: no directive, no comment and no expression
		// survived into the served CSS.
		assertFalse(css.contains("<%"),
				"a raw DSP directive reached the browser; the .css.dsp was streamed uninterpreted");
		assertFalse(css.contains("${"),
				"an unresolved ${...} reached the browser");

		// An unresolved ${...} writes NOTHING, which yields the invalid `font-family: ;`.
		String block = ruleBlock(css, ".z-simplelabel");
		assertFalse(block.isEmpty(), "could not locate the .z-simplelabel rule in the served CSS");
		String fontFamily = declaredValue(block, "font-family");
		assertFalse(fontFamily.isEmpty(),
				"the served .z-simplelabel rule declares an EMPTY font-family; an unresolved EL wrote nothing");
		assertTrue(fontFamily.contains("var(--zk-"),
				"the served font-family does not use a --zk-* custom property: " + fontFamily);
	}

	/** The body of the first rule whose selector is exactly {@code selector}. */
	private static String ruleBlock(String css, String selector) {
		Matcher m = Pattern.compile(Pattern.quote(selector) + "\\s*\\{([^}]*)\\}").matcher(css);
		return m.find() ? m.group(1) : "";
	}

	/** The declared value of {@code property} inside a rule body, "" when absent or empty. */
	private static String declaredValue(String ruleBody, String property) {
		Matcher m = Pattern.compile("(?:^|[;{])\\s*" + Pattern.quote(property) + "\\s*:([^;}]*)")
				.matcher(ruleBody);
		return m.find() ? m.group(1).trim() : "";
	}

	private static String httpGet(String url) throws Exception {
		HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
		// Ask for no gzip: WcsExtendlet compresses bodies over 200 bytes.
		http.setRequestProperty("Accept-Encoding", "identity");
		http.setRequestProperty("Accept", "text/css,*/*");
		try {
			assertEquals(200, http.getResponseCode(), "GET " + url);
			assertTrue(String.valueOf(http.getContentType()).contains("text/css"),
					"the aggregated stylesheet is not served as text/css: " + http.getContentType());
			try (InputStream in = http.getInputStream()) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[8192];
				for (int n; (n = in.read(buf)) > 0;)
					out.write(buf, 0, n);
				return new String(out.toByteArray(), StandardCharsets.UTF_8);
			}
		} finally {
			http.disconnect();
		}
	}
}
