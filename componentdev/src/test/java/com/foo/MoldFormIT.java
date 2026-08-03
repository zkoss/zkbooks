/* MoldFormIT.java

	Does the MOLD - in the form the lab and the book actually write it - render?

	Written from tasks/research/component-dev/{styling-dom,mold-wpd}.md and from the book
	page zk_component_dev_essentials/implementing_molds.md. Every @DisplayName is one
	documented claim.

	Why this is an *IT and not a ZATS test: a mold is client-side. ZATS proves the server
	knows the mold NAME (MoldFormTest) and nothing more - it never fetches the mold file,
	never runs it and never sees a class attribute. A mold that is declared but missing,
	or delivered but broken, is green in every ZATS test.

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;
import org.zkoss.test.webdriver.ztl.JQuery;

public class MoldFormIT extends WebDriverTestCase {

	private static final String PAGE = "/moldforms.zul";

	/**
	 * Selects a widget's SUB-element by its real DOM id.
	 *
	 * <p>{@code jq("$named")} resolves a ZK <em>component</em> id, so
	 * {@code jq("$named-inner")} matches nothing whatsoever - it looks for a component
	 * whose id is the literal string "named-inner". A sub-element has to be addressed by
	 * the id the mold wrote, {@code <uuid>-<subId>}. The negative half of that is
	 * asserted in ScopedDomIT.
	 */
	private JQuery sub(String zkId, String subId) {
		return jq("#" + uuidOf(zkId) + "-" + subId);
	}

	private String uuidOf(String zkId) {
		return getEval("zk.Widget.$('$" + zkId + "').uuid");
	}

	// -------------------------------------------------------------- mold registration

	@Test
	@DisplayName("every <mold> declared in lang-addon.xml arrives as a function on the widget class under its <mold-name>, and the named mold expression keeps its function name")
	public void bothDeclaredMoldsArriveAsNamedFunctions() {
		// connect() MUST be first: it launches the browser and publishes the driver to the
		// ThreadLocal that every jq()/getEval() reads. The factory is getWebDriver().
		connect(PAGE);
		waitResponse();

		assertEquals("true", getEval("!!(window.com && com.foo && com.foo.SimpleLabel)"),
				"zk.wpd did not deliver com.foo.SimpleLabel to the client");

		// A mold file is a single JS EXPRESSION; the WPD response assigns it into the
		// widget class's molds map, keyed by <mold-name>. Remove the second <mold> from
		// lang-addon.xml, or misspell its <mold-uri>, and this is what goes red.
		assertEquals("function", getEval("typeof com.foo.SimpleLabel.molds['default']"),
				"the default mold is not a function on the widget class");
		assertEquals("function", getEval("typeof com.foo.SimpleLabel.molds['fancy']"),
				"the fancy mold never reached the widget class");
		assertEquals("undefined", getEval("typeof com.foo.SimpleLabel.molds['nosuch']"),
				"the molds map answers for a mold nobody declared, so the assertions above prove nothing");

		// The lab (and 83 of the 98 molds in the released zul jar) write the mold as a
		// NAMED function expression. The name survives delivery, which is the only
		// observable difference from the anonymous form and the reason to prefer it: it is
		// what a stack trace shows. An anonymous mold reads back as ''.
		assertEquals("simpleLabel$mold$", getEval("com.foo.SimpleLabel.molds['default'].name"),
				"the default mold is not the NAMED function expression the lab ships");
		assertEquals("fancy$mold$", getEval("com.foo.SimpleLabel.molds['fancy'].name"),
				"the fancy mold is not the NAMED function expression the lab ships");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------ mold selection

	@Test
	@DisplayName("each mold renders its own root element, and the selected mold name reaches the client whether it was set with the ZUL attribute or with setMold() in Java")
	public void eachMoldRendersItsOwnRootAndSelectionReachesTheClient() {
		connect(PAGE);
		waitResponse();

		// The mold name really crossed the wire. If it had not, all three widgets would
		// render the default mold and the tag-name assertions below would all read "span".
		assertEquals("default", getEval("zk.Widget.$('$named').getMold()"));
		assertEquals("fancy", getEval("zk.Widget.$('$fancydecl').getMold()"),
				"mold=\"fancy\" in the ZUL never reached the client widget");
		assertEquals("fancy", getEval("zk.Widget.$('$fancyjava').getMold()"),
				"setMold(\"fancy\") in Java never reached the client widget");

		// ...and the two molds really are different functions: the default emits <span>,
		// the fancy one <div>. This is what "the mold renders" means observably.
		assertTrue(jq("$named").exists(), "the default mold rendered no DOM at all");
		assertTrue(jq("$named").is("span"), "the default mold must render a span");
		assertTrue(jq("$fancydecl").is("div"), "the fancy mold must render a div");
		assertTrue(jq("$fancyjava").is("div"),
				"the widget whose mold was chosen in Java rendered the DEFAULT mold's element");

		// Control: prove the tag-name probe actually distinguishes the two molds. Switch
		// the fancy widget back to the default mold and it must become a span. Without
		// this step the assertions above could be green for a reason unrelated to mold
		// selection - which is the failure mode this test exists to rule out.
		assertEquals("default", getEval(
				"(function(){var w=zk.Widget.$('$fancydecl');w.setMold('default');w.rerender(-1);return w.getMold();})()"));
		waitResponse();
		assertTrue(jq("$fancydecl").is("span"),
				"switching back to the default mold did not change the root element, so the div/span assertions prove nothing");

		assertNoZKError();
		assertNoJSError();
	}

	@Test
	@DisplayName("both molds keep the <uuid>-<subId> sub-element contract, so $n('inner') resolves under either mold")
	public void everyMoldKeepsTheSubElementContract() {
		connect(PAGE);
		waitResponse();

		for (String id : new String[] { "named", "fancydecl", "fancyjava" }) {
			String uuid = uuidOf(id);

			// The mold wrote id="<uuid>-inner" - NOT a hard-coded id="inner", which would
			// (a) be unresolvable by $n('inner') and (b) duplicate across two components.
			assertTrue(sub(id, "inner").exists(),
					id + ": the mold emitted no id=\"<uuid>-inner\" sub-element");
			assertEquals(uuid + "-inner", getEval("zk.Widget.$('$" + id + "').$n('inner').id"),
					id + ": $n('inner') did not resolve the node the mold wrote");

			// Both molds scope the sub-element through $s('inner'), so the class is shared
			// even though the structure differs.
			assertTrue(sub(id, "inner").hasClass("z-simplelabel-inner"),
					id + ": the sub-element lost the shared $s('inner') class");

			// The value rendered at FIRST paint under this mold.
			assertEquals("Hello ZK", sub(id, "inner").text(),
					id + ": renderProperties() did not reach this mold's sub-element");
		}

		// Only the fancy mold adds its own scoped class - otherwise "the molds differ"
		// would be unfalsifiable.
		assertFalse(sub("named", "inner").hasClass("z-simplelabel-fancy"),
				"the default mold emitted the fancy mold's scoped class");
		assertTrue(sub("fancydecl", "inner").hasClass("z-simplelabel-fancy"),
				"the fancy mold did not add its own $s('fancy') class");
		assertTrue(sub("fancyjava", "inner").hasClass("z-simplelabel-fancy"),
				"the Java-selected fancy mold did not add its own $s('fancy') class");

		assertNoJSError();
	}

	// ------------------------------------------------------------- the anonymous form

	@Test
	@DisplayName("the ANONYMOUS mold form the book's snippet shows is a valid mold expression and renders the same DOM as the named form")
	public void theAnonymousMoldFormRendersToo() {
		connect(PAGE);
		waitResponse();

		// The reference: what the NAMED mold file produced for the same widget.
		String namedRootClass = jq("$named").attr("class");
		String namedInnerClass = sub("named", "inner").attr("class");
		String uuid = uuidOf("named");
		assertTrue(jq("$named").is("span"), "precondition: the named mold renders a span");
		assertEquals("z-simplelabel", namedRootClass, "precondition: domAttrs_() emitted the zclass");

		// Install a mold written in the ANONYMOUS form - `function (out) {...}`, which is
		// what implementing_molds.md shows - in the exact context the WPD response builds:
		//     zk._m['<mold-name>'] = <the mold file's bytes> \n ;
		//     zkmld(<widget class>, zk._m);
		// The existing molds are copied in first because zkmld REPLACES the molds map.
		//
		// What this does and does not prove: it exercises the mold EXPRESSION and ZK's
		// real redraw dispatch (redraw -> molds[getMold()] -> domAttrs_/$s). It does not
		// exercise WpdExtendlet's file delivery - that half is
		// theWpdResponseInlinesEachMoldFileVerbatim(), which can only cover the named
		// form because the lab ships no anonymous mold FILE.
		assertEquals("function/anonymous", getEval(INSTALL_ANON_MOLD),
				"zkmld did not accept the anonymous mold expression");

		assertEquals("anon", getEval(
				"(function(){var w=zk.Widget.$('$named');w.setMold('anon');w.rerender(-1);return w.getMold();})()"),
				"setMold('anon') did not take");
		waitResponse();

		// Same observable DOM as the named form produced.
		assertTrue(jq("$named").is("span"), "the anonymous mold rendered no span root");
		assertEquals(namedRootClass, jq("$named").attr("class"),
				"the anonymous mold's domAttrs_() output differs from the named mold's");
		assertEquals(uuid, uuidOf("named"), "the widget lost its identity across the rerender");
		assertTrue(sub("named", "inner").exists(),
				"the anonymous mold emitted no <uuid>-inner sub-element");
		assertEquals(namedInnerClass, sub("named", "inner").attr("class"),
				"the anonymous mold's $s('inner') output differs from the named mold's");
		assertEquals("Hello ZK", sub("named", "inner").text(),
				"the anonymous mold did not render the value");
		assertEquals(uuid + "-inner", getEval("zk.Widget.$('$named').$n('inner').id"),
				"$n('inner') cannot resolve the anonymous mold's sub-element");

		// A mold that threw would leave the element empty and log to the console rather
		// than raise - ZK catches widget errors. Without these two the test could be green
		// on a broken mold.
		assertNoZKError();
		assertNoJSError();
	}

	/**
	 * Installs an anonymous-form mold in the exact shape {@code WpdExtendlet} emits, and
	 * returns {@code "function/anonymous"} when the map now holds a nameless function.
	 *
	 * <p>The {@code .name} check matters: an assignment to a MEMBER expression does not
	 * infer a function name, so a genuinely anonymous mold reads back as {@code ''}. If
	 * this returned {@code function/<something>} the test would be asserting the named
	 * form twice.
	 */
	private static final String INSTALL_ANON_MOLD = "(function(){"
			+ "var cls = com.foo.SimpleLabel;"
			+ "zk._m = {};"
			+ "for (var nm in cls.molds) zk._m[nm] = cls.molds[nm];"
			+ "zk._m['anon'] = function (out) {"
			+ "  var uuid = this.uuid;"
			+ "  out.push('<span', this.domAttrs_(), '>',"
			+ "    '<span id=\"', uuid, '-inner\" class=\"', this.$s('inner'), '\">',"
			+ "    zUtl.encodeXML(this.getValue()),"
			+ "    '</span>',"
			+ "    '</span>');"
			+ "}\n;"
			+ "zkmld(cls, zk._m);"
			+ "var fn = cls.molds['anon'];"
			+ "return (typeof fn) + '/' + (fn && fn.name === '' ? 'anonymous' : 'named:' + (fn && fn.name));"
			+ "})()";

	// ---------------------------------------------------------------- mold delivery

	@Test
	@DisplayName("the widget package response inlines each mold FILE verbatim on the right-hand side of zk._m['<mold-name>'] and hands the map to zkmld")
	public void theWpdResponseInlinesEachMoldFileVerbatim() throws Exception {
		// Self-check of the segment extractor used below: it must be able to see the defect
		// it exists to catch (a mold key whose right-hand side is not that mold's file) and
		// must not bleed one mold's segment into the next. Without this the assertions
		// further down could be decoration.
		String synthetic = "zkreg('x');zk._m={};\n"
				+ "zk._m['default']=function a$mold$(out){}\n;"
				+ "zk._m['fancy']=function b$mold$(out){}\n;"
				+ "zkmld(x,zk._m);\n";
		assertTrue(moldSegment(synthetic, "default").contains("function a$mold$(out)"),
				"the extractor cannot read a mold segment");
		assertFalse(moldSegment(synthetic, "default").contains("function b$mold$(out)"),
				"the extractor bleeds one mold's segment into the next");
		assertEquals("", moldSegment(synthetic, "nosuch"),
				"the extractor invents a segment for a mold that is not in the response");

		connect(PAGE);
		waitResponse();

		// The URL the BROWSER actually fetched, read back from the Resource Timing API.
		// Constructing it by hand is a trap worth recording: the widget package descriptor
		// lives at web/js/com/foo/zk.wpd on the classpath, but it is served under the
		// PACKAGE NAME - <update-uri>/web/<cache-segment>/js/com.foo.wpd. Every
		// directory-shaped variant (.../js/com/foo/zk.wpd) is a 404.
		String url = getEval("(function(){"
				+ "var es = performance.getEntriesByType('resource');"
				+ "for (var i = 0; i < es.length; i++)"
				+ "  if (es[i].name.indexOf('com.foo.wpd') >= 0) return es[i].name;"
				+ "return location.origin + zk.ajaxResourceURI('/js/com.foo.wpd');"
				+ "})()");
		assertTrue(url.startsWith("http"), "could not resolve the widget package URL: " + url);
		assertTrue(url.contains("/js/com.foo.wpd"),
				"the widget package is not served under its package name: " + url);

		String wpd = httpGet(url);

		// Sanity: this really is the com.foo package response, not an error page.
		assertTrue(wpd.contains("zkreg('com.foo.SimpleLabel')"),
				"the response does not register the widget class, so it is not the com.foo package: " + head(wpd));
		assertTrue(wpd.contains("zk.$extends"),
				"the response does not carry the widget class source");

		// A missing mold URI is not an exception: WpdExtendlet writes zk.error('... not
		// found') into the response and the client logs it. That is the failure mode this
		// assertion exists to catch.
		assertFalse(wpd.contains("not found"),
				"the widget package response reports a resource it could not load: " + wpd);

		// One segment per declared mold, each holding the mold file's own bytes. Asserted
		// per segment rather than by position, because getMoldNames() gives no order
		// guarantee.
		assertTrue(moldSegment(wpd, "default").contains("function simpleLabel$mold$(out)"),
				"zk._m['default'] is not the mold FILE's own named expression: " + moldSegment(wpd, "default"));
		assertTrue(moldSegment(wpd, "fancy").contains("function fancy$mold$(out)"),
				"zk._m['fancy'] is not the mold FILE's own named expression: " + moldSegment(wpd, "fancy"));
		// Verbatim, comments and all - the file is inlined, not transformed.
		assertTrue(moldSegment(wpd, "default").contains("this.$s('inner')"),
				"the mold file's $s('inner') call did not survive delivery");

		// ...and the map is handed to zkmld, which is what turns it into
		// com.foo.SimpleLabel.molds.
		assertTrue(wpd.contains("zkmld(com.foo.SimpleLabel,zk._m)"),
				"the molds map is never installed on the widget class");
	}

	/** The text between {@code zk._m['<mold>']=} and the next mold or the zkmld call. */
	private static String moldSegment(String wpd, String moldName) {
		String marker = "zk._m['" + moldName + "']=";
		int start = wpd.indexOf(marker);
		if (start < 0)
			return "";
		start += marker.length();
		int end = wpd.length();
		for (String terminator : new String[] { "zk._m['", "zkmld(" }) {
			int at = wpd.indexOf(terminator, start);
			if (at >= 0 && at < end)
				end = at;
		}
		return wpd.substring(start, end);
	}

	private static String head(String s) {
		return s.length() > 200 ? s.substring(0, 200) : s;
	}

	// --------------------------------------------------------------- track parity

	@Test
	@DisplayName("the TypeScript track ships its own mold file under the same <mold-name> and renders DOM identical to the plain-JS track's")
	public void bothTracksShipTheirOwnMoldAndRenderTheSameDom() {
		connect(PAGE);
		waitResponse();

		assertEquals("true", getEval("!!(window.labts && labts.SimpleLabel)"),
				"the TypeScript widget package did not load");
		assertEquals("function", getEval("typeof labts.SimpleLabel.molds['default']"),
				"the TS track's mold never reached its widget class");
		assertEquals("simpleLabel$mold$", getEval("labts.SimpleLabel.molds['default'].name"),
				"the TS track's mold is not the named form");

		// Two packages, two mold FILES: if these were the same function object the DOM
		// comparison below would be a tautology.
		assertEquals("false", getEval("String(com.foo.SimpleLabel.molds['default'] === labts.SimpleLabel.molds['default'])"),
				"both tracks are sharing one mold function, so this test proves nothing");

		assertTrue(jq("$tsnamed").is("span"), "the TS track's mold did not render a span");
		assertEquals(jq("$named").attr("class"), jq("$tsnamed").attr("class"),
				"the two tracks disagree about the root element's classes");
		assertEquals(sub("named", "inner").attr("class"), sub("tsnamed", "inner").attr("class"),
				"the two tracks disagree about the sub-element's scoped classes");
		assertEquals(sub("named", "inner").text(), sub("tsnamed", "inner").text(),
				"the two tracks disagree about the rendered value");
		assertEquals(uuidOf("tsnamed") + "-inner", getEval("zk.Widget.$('$tsnamed').$n('inner').id"),
				"the TS track's $n('inner') does not resolve the <uuid>-<subId> node");

		// Different uuids, same class strings: proves the comparison above ran against two
		// distinct elements.
		assertNotEquals(uuidOf("named"), uuidOf("tsnamed"));

		assertNoZKError();
		assertNoJSError();
	}

	private static String httpGet(String url) throws Exception {
		HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
		// WPD responses are gzipped when large; ask for the plain bytes.
		http.setRequestProperty("Accept-Encoding", "identity");
		try {
			assertEquals(200, http.getResponseCode(), "GET " + url);
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
