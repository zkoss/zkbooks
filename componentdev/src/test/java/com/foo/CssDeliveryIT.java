/* CssDeliveryIT.java

	The claims of zk_component_dev_essentials/delivering_your_component_stylesheet.md that
	live on the wire or in the browser, and that no test in the lab reached before:

	  - "do not worry about declaring the same URI on several molds or components: the list
	     is a set, so duplicates collapse"                                            (U2)
	  - "ZK does not validate the extension of a <css-uri> value at all";
	    ".less: never as a <css-uri> value ... less has no interpreter registered at
	     runtime"; "A resource whose extension has no interpreter is streamed verbatim into
	     the aggregated response"                                                      (U3)
	  - "During development set the library property org.zkoss.zk.WCS.cache to false ...
	     editing your stylesheet on disk and reloading is enough"                      (U4)
	  - "url(${c:encodeThemeURL(\"~./js/com/foo/img/dot.png\")})" resolving a resource
	    shipped inside the jar                                                         (U5)
	  - "An unresolved expression writes nothing rather than failing, so
	     font-family: ${fontFamilyC}; is served as font-family: ; - an invalid declaration
	     the browser drops in silence while the rest of the rule still applies"        (U6)
	  - "<stylesheet> ... becomes its own <link> in the page rather than being folded into
	     the aggregate. It also passes through the theme machinery, so an application can
	     switch it off from zk.xml with <disable-theme-uri>"                           (U7)
	  - "The link's href shows the ~./ URL shape ... The opaque middle segment is a
	     cache-busting hash"                                                           (U8)

	Why an *IT and not ZATS: every one of them is either a byte of the aggregated response,
	a <link> in the rendered head, or a computed style. ZATS renders no HTML and evaluates
	no CSS, so all of it is invisible there. The declaration-side halves of U2 and U3 are
	CssUriDeclarationTest's, at the cheaper layer.

	The page it drives, src/test/webapp/cssdelivery.zul, is the ONLY page carrying elements
	that the fixture stylesheets match. The stylesheets themselves are language-wide (that
	is the point of U2/U3), but their selectors exist nowhere else, so no other fixture's
	computed-style assertions change. The fixture stylesheets live in
	src/test/resources/web/labfix/css/ and reach the classpath as target/test-classes only.

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.test.webdriver.WebDriverTestCase;

public class CssDeliveryIT extends WebDriverTestCase {

	private static final String PAGE = "/cssdelivery.zul";

	/** Where the fixture stylesheets sit on the running server's classpath. */
	private static final Path BUILT_FIXTURES = Paths.get("target/test-classes/web/labfix/css");
	/** The two descriptors that declare the duplicated URI. */
	private static final Path SHIPPED_ADDON = Paths.get("src/main/resources/metainfo/zk/lang-addon.xml");
	private static final Path FIXTURE_ADDON = Paths.get("src/test/resources/metainfo/zk/lang-addon.xml");
	private static final Path ZK_XML = Paths.get("src/test/webapp/WEB-INF/zk.xml");

	// ------------------------------------------------------------------------------- U2

	@Test
	@DisplayName("duplicates collapse in the delivered bytes too: one <css-uri> declared by two addons is included ONCE in the aggregated response")
	public void aDuplicatedCssUriIsAggregatedOnlyOnce() throws Exception {
		// connect() MUST be first: it launches the browser and publishes the driver to the
		// ThreadLocal that jq()/getEval() read.
		connect(PAGE);
		waitResponse();

		// The control that makes the count below mean "collapsed" rather than "declared
		// once": two separate descriptors really do declare the same relative value.
		assertEquals(1, declarations(SHIPPED_ADDON, "css/simplelabel.css.dsp"),
				"the shipped descriptor no longer declares the stylesheet");
		assertEquals(1, declarations(FIXTURE_ADDON, "css/simplelabel.css.dsp"),
				"the test-scoped descriptor no longer duplicates the shipped declaration, so this"
						+ " test cannot see a collapse");

		String css = httpGet(wcsUrl());

		// A List-based aggregation would concatenate the whole stylesheet twice, so every
		// rule in it would appear twice.
		assertEquals(1, count(css, ".z-simplelabel-fancy"),
				"the doubly-declared stylesheet was included more than once in zk.wcs");
		assertEquals(1, count(css, ".z-simplelabel-inner"),
				"the doubly-declared stylesheet was included more than once in zk.wcs");

		// Control: a URI declared exactly ONCE also arrives exactly once, so "1" is not an
		// artifact of the counter or of some global de-duplication of text.
		assertEquals(1, count(css, ".labfix-plain-css-probe"),
				"a singly-declared stylesheet did not arrive exactly once");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- U3

	@Test
	@DisplayName("a <css-uri> whose extension has no interpreter is streamed verbatim: a plain .css applies, and a .less arrives uncompiled because no LESS interpreter is registered")
	public void noInterpreterMeansVerbatimBytes() throws Exception {
		connect(PAGE);
		waitResponse();

		String css = httpGet(wcsUrl());

		// VERBATIM, and asserted as such: the whole file, its comment included, is present
		// in the aggregated body exactly as it is on disk.
		String plainSource = read(BUILT_FIXTURES.resolve("labfix-plain.css")).trim();
		assertTrue(css.contains(plainSource),
				"the plain .css was not streamed verbatim into zk.wcs; served body around the probe: "
						+ around(css, ".labfix-plain-css-probe"));

		// ...and the rules it carries really apply, so "the delivery path and the resulting
		// rules are the same as for a .css.dsp" is observable and not merely asserted about
		// bytes. 7px is unique to this fixture.
		assertEquals("7px", computed("p-plain", "padding-left"),
				"the plain .css arrived in the response but its rule did not apply");
		assertEquals("0px", computed("ctl", "padding-left"),
				"control: the unstyled control element must have no padding");

		// The .less is streamed just as verbatim - which is the whole argument for "never as
		// a <css-uri> value": its LESS variable arrives as CSS text...
		assertTrue(css.contains("@labfixLessVar: 9px;"),
				"the .less did not arrive verbatim, so something interpreted it after all: "
						+ around(css, "labfix-less-probe"));
		assertFalse(css.contains("padding-left: 9px"),
				"the LESS variable was RESOLVED, so a LESS interpreter IS registered at runtime");

		// ...and the browser therefore drops the declaration: no interpreter, no styling.
		assertEquals("0px", computed("p-less", "padding-left"),
				"the .less rule applied, so its variable was compiled somewhere");

		// The two probes must not be the same element, or the pair proves nothing.
		assertNotEquals(computed("p-plain", "padding-left"), computed("p-less", "padding-left"),
				"the plain-.css probe and the .less probe compute the same value");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- U6

	@Test
	@DisplayName("an unresolved ${...} writes NOTHING: the declaration is served empty, the browser drops just that declaration, and the rest of the rule still applies")
	public void anUnresolvedElWritesNothingAndOnlyThatDeclarationIsLost() throws Exception {
		connect(PAGE);
		waitResponse();

		// Self-check of the predicate, so this test can see the defect it is about.
		assertEquals("", declaredValue(".x {\n\tfont-family: ;\n}", "font-family"),
				"the predicate cannot see an empty declaration");
		assertEquals("serif", declaredValue(".x { font-family: serif; }", "font-family"),
				"the predicate cannot read a normal declaration");

		String css = httpGet(wcsUrl());
		String block = ruleBlock(css, ".labfix-unresolved-el");
		assertFalse(block.isEmpty(), "the fixture stylesheet is not in the aggregated response at all");

		// "writes nothing rather than failing": the declaration is there, its value is not,
		// and no ${...} survived - the interpreter DID run.
		assertTrue(block.contains("font-family:"),
				"the whole declaration disappeared rather than being served empty: " + block);
		assertEquals("", declaredValue(block, "font-family"),
				"the unresolved EL did not write an EMPTY value: " + block);
		assertFalse(block.contains("${"),
				"the expression reached the browser uninterpreted: " + block);

		// The browser's own reading of the same rule: the invalid declaration was dropped
		// (font-family reads back as ""), the valid one beside it survived.
		assertEquals("color=[rgb(1, 2, 3)] font-family=[]", getEval(RULE_PROBE),
				"the CSSOM does not show the invalid declaration dropped and the rest of the rule kept");

		// ...and "the rest of the rule still applies" as a computed value.
		assertEquals("rgb(1, 2, 3)", computed("p-el", "color"),
				"the declaration next to the invalid one did not apply");
		assertFalse(computed("p-el", "font-family").trim().isEmpty(),
				"the element's font-family is empty; the invalid declaration was not dropped but honoured");

		// "in silence": nothing anywhere reports it.
		assertNoZKError();
		assertNoJSError();
	}

	/** Reads the {@code .labfix-unresolved-el} rule back out of the browser's CSSOM. */
	private static final String RULE_PROBE = "(function(){"
			+ "var hits=[];"
			+ "for (var i=0;i<document.styleSheets.length;i++){"
			+ "  var rules;try{rules=document.styleSheets[i].cssRules;}catch(e){continue;}"
			+ "  if(!rules) continue;"
			+ "  for (var j=0;j<rules.length;j++){var r=rules[j];"
			+ "    if(r.selectorText && r.selectorText.replace(/\\s/g,'')=='.labfix-unresolved-el')"
			+ "      hits.push('color=['+r.style.getPropertyValue('color')"
			+ "        +'] font-family=['+r.style.getPropertyValue('font-family')+']');"
			+ "  }"
			+ "}"
			+ "return hits.join(' ; ');"
			+ "})()";

	// ------------------------------------------------------------------------------- U7

	@Test
	@DisplayName("<stylesheet> becomes its own <link> rather than being folded into the aggregate, and an application switches one off from zk.xml with <disable-theme-uri>")
	public void stylesheetIsItsOwnLinkAndDisableThemeUriTurnsItOff() throws Exception {
		connect(PAGE);
		waitResponse();

		// The two fixture <stylesheet> declarations are identical in every way except that
		// zk.xml names the second one in <disable-theme-uri>. So the pair controls itself:
		// the difference IS the zk.xml element.
		assertTrue(read(ZK_XML).contains("<disable-theme-uri>~./labfix/css/labfix-disabled-link.css"),
				"zk.xml no longer disables the fixture stylesheet, so the pair below is not a controlled"
						+ " experiment: " + ZK_XML);

		assertEquals(1, jq("link[href*=\"labfix-separate-link\"]").length(),
				"a <stylesheet> did not become its own <link>");
		assertEquals(0, jq("link[href*=\"labfix-disabled-link\"]").length(),
				"the <stylesheet> named in zk.xml <disable-theme-uri> was emitted anyway");

		// ...and the consequence, not just the tag count: one applies, the other cannot.
		assertEquals("11px", computed("p-link", "padding-right"),
				"the separately linked stylesheet did not load");
		assertEquals("0px", computed("p-disabled", "padding-right"),
				"the disabled stylesheet still applied");
		assertEquals("0px", computed("ctl", "padding-right"), "control: the control element has no padding");

		// The contrast that makes <stylesheet> and <css-uri> "different mechanisms, not
		// synonyms": nothing that arrived through <css-uri> is a <link> of its own.
		for (String name : new String[] { "labfix-plain", "labfix-never-do-this", "labfix-unresolved-el",
				"simplelabel" })
			assertEquals(0, jq("link[href*=\"" + name + "\"]").length(),
					name + ": a <css-uri> stylesheet was emitted as its own <link>");

		// ...while the one aggregated response is still exactly one <link>.
		assertEquals(1, jq("link[href*=\"zk.wcs\"]").length(),
				"expected exactly one <link> to the language's .wcs");

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- U4

	@Test
	@DisplayName("with org.zkoss.zk.WCS.cache=false the running server serves the edited bytes of BOTH a plain .css and a .css.dsp with no restart, and answers a revalidating request with them - but still sends a year-long client cache lifetime")
	public void editingAStylesheetOnDiskIsEnough() throws Exception {
		// The configuration the claim is about. Without this the test would show that
		// editing works, while saying nothing about the switch the page tells you to set.
		assertTrue(read(ZK_XML).contains("org.zkoss.zk.WCS.cache"),
				"zk.xml does not set the library property this claim is about: " + ZK_XML);

		connect(PAGE);
		waitResponse();

		Path plain = BUILT_FIXTURES.resolve("labfix-plain.css");
		Path dsp = BUILT_FIXTURES.resolve("labfix-unresolved-el.css.dsp");
		byte[] plainBytes = Files.readAllBytes(plain);
		byte[] dspBytes = Files.readAllBytes(dsp);

		String url = wcsUrl();
		String[] first = httpGetWithValidator(url);
		String before = first[0], lastModified = first[1], cacheControl = first[2];
		assertFalse(before.contains("labfix-devloop-plain-probe"),
				"baseline: the marker rule is already in the response");
		assertFalse(before.contains("labfix-devloop-dsp-probe"),
				"baseline: the marker rule is already in the response");

		// THE OTHER HALF OF THE DEV LOOP, and the reason the last step of this test
		// revalidates explicitly. org.zkoss.zk.WCS.cache is a SERVER-side switch; the
		// aggregated response is still sent with a year-long freshness lifetime, because that
		// header is governed by a DIFFERENT library property,
		// org.zkoss.web.classWebResource.cache, which the lab does not set
		// (WcsExtendlet.java:88 + :99-100 -> JspFns.java:188-194). So a browser that already
		// has the stylesheet does not ask again, and "reloading is enough" needs that caveat.
		assertTrue(cacheControl.contains("max-age="),
				"the aggregated response carries no Cache-Control at all: " + cacheControl);
		assertTrue(maxAge(cacheControl) > 86400,
				"the aggregated response is no longer sent with a long client-side freshness"
						+ " lifetime, so the dev-loop caveat in this test's comment is stale: " + cacheControl);

		try {
			// Edit BOTH forms on disk - the build output, never the source - with the server
			// still running and no restart in between.
			append(plain, "\n.labfix-devloop-plain-probe { padding-left: 17px; }\n");
			append(dsp, "\n.labfix-devloop-dsp-probe { padding-left: 19px; }\n");

			// THE CLAIM, in the form the property can deliver: the running server serves the
			// new bytes of BOTH forms - the aggregation's cache and the .css.dsp interpreter's
			// cache were both cleared - with no restart.
			String after = httpGet(url);
			assertTrue(after.contains("labfix-devloop-plain-probe"),
					"the edited plain .css was not re-read: the aggregation served a cached copy");
			assertTrue(after.contains("labfix-devloop-dsp-probe"),
					"the edited .css.dsp was not re-read: the interpreter served a cached copy");

			// ...and a REVALIDATING request gets them too, which is what makes a reload rather
			// than a restart sufficient: the server must not answer "not modified" for the
			// validator it handed out before the edit.
			//
			// WHAT THIS TEST DELIBERATELY DOES NOT ASSERT, because it is Chrome's freshness
			// heuristic and not ZK's contract: that a plain re-navigation shows the new rule.
			// Measured here on 10.3.0.1, it does NOT - the aggregated response carries a
			// far-future expiry, so the browser reuses its copy without asking. That is
			// reported to the doc writer as a nuance of "reloading is enough" rather than
			// asserted, and it is why this step revalidates explicitly instead.
			HttpURLConnection conditional = open(url);
			if (!lastModified.isEmpty())
				conditional.setRequestProperty("If-Modified-Since", lastModified);
			try {
				assertNotEquals(304, conditional.getResponseCode(),
						"the edited stylesheet is still answered 'not modified' for the validator issued"
								+ " before the edit (If-Modified-Since: " + lastModified
								+ "), so no reload can pick it up");
				assertTrue(new String(readAll(conditional.getInputStream()), StandardCharsets.UTF_8)
						.contains("labfix-devloop-plain-probe"),
						"the revalidated response does not carry the edited rule");
			} finally {
				conditional.disconnect();
			}
		} finally {
			Files.write(plain, plainBytes);
			Files.write(dsp, dspBytes);
		}

		// Restored, and observably so - otherwise this test would leave the fixture
		// stylesheets edited for every test that follows it.
		String restored = httpGet(wcsUrl());
		assertFalse(restored.contains("labfix-devloop-plain-probe"),
				"the fixture stylesheet was not restored");
		assertFalse(restored.contains("labfix-devloop-dsp-probe"),
				"the fixture stylesheet was not restored");
		assertTrue(restored.contains(".labfix-plain-css-probe"),
				"the restored fixture stylesheet lost its original rule");
	}

	// ------------------------------------------------------------------------------- U5

	@Test
	@DisplayName("url(${c:encodeThemeURL(\"~./js/com/foo/img/dot.png\")}) resolves to a real classpath URL: no EL is left in the served CSS, and the URL the browser parsed really serves the PNG")
	public void encodeThemeUrlResolvesAResourceShippedInTheJar() throws Exception {
		// The rule that uses it is the fancy mold's, so this one test drives styling.zul,
		// where a widget with mold="fancy" exists.
		connect("/styling.zul");
		waitResponse();

		String innerId = getEval("zk.Widget.$('$deco').uuid") + "-inner";
		String background = getEval(
				"getComputedStyle(document.getElementById('" + innerId + "')).getPropertyValue('background-image')");

		assertTrue(background.contains("/zkau/web/"),
				"the resolved URL is not a ~./ classpath URL: " + background);
		assertTrue(background.contains("/js/com/foo/img/dot.png"),
				"the resolved URL does not name the shipped resource: " + background);
		assertFalse(background.contains("${"), "the expression was never interpreted: " + background);
		assertFalse(background.contains("~./"), "the ~./ form was served raw to the browser: " + background);

		// The load-bearing half. A 404 is INVISIBLE in a computed style - the browser reports
		// the url() it parsed either way - so the URL has to be fetched.
		Matcher m = Pattern.compile("url\\(\"?([^\")]+)\"?\\)").matcher(background);
		assertTrue(m.find(), "could not read a url() out of the computed background-image: " + background);
		String resolved = m.group(1);

		HttpURLConnection http = open(resolved);
		try {
			assertEquals(200, http.getResponseCode(), "GET " + resolved);
			assertTrue(String.valueOf(http.getContentType()).contains("image/png"),
					"the resolved URL does not serve a PNG: " + http.getContentType());
			byte[] body = readAll(http.getInputStream());
			assertTrue(body.length > 8, "the resolved URL served an empty body");
			assertEquals("89504e47", hex(body, 4),
					"the resolved URL did not serve the PNG signature, so it is not the shipped image");
		} finally {
			http.disconnect();
		}

		// ...and the interpretation happened in-process, in the aggregated response itself.
		String block = ruleBlock(httpGet(wcsUrl()), ".z-simplelabel-fancy");
		assertFalse(block.isEmpty(), "the component's fancy rule is not in the aggregated response");
		assertTrue(block.contains("url("), "the served rule carries no url(): " + block);
		assertTrue(block.contains("/js/com/foo/img/dot.png"),
				"the served rule does not carry the resolved resource path: " + block);
		assertFalse(block.contains("${"), "the served rule still carries the expression: " + block);

		assertNoZKError();
		assertNoJSError();
	}

	// ------------------------------------------------------------------------------- U8

	@Test
	@DisplayName("the href shows the ~./ URL shape, and the opaque middle segment is ONE cache-busting hash per deployment: every server-emitted ~./ URL on the page carries the same one")
	public void oneOpaqueCacheSegmentPerDeployment() {
		connect(PAGE);
		waitResponse();

		String wcsHref = jq("link[href*=\"zk.wcs\"]").attr("href");
		assertTrue(wcsHref.matches("^\\Q" + getContextPath() + "\\E/zkau/web/[^/]+/zul/css/zk\\.wcs$"),
				"unexpected ~./ URL shape: " + wcsHref);

		String segment = cacheSegment(wcsHref);
		// Integer.toHexString of an int: 1-8 lowercase hex characters, and nothing that
		// identifies what it was computed from. THE DERIVATION ITSELF IS NOT ASSERTED - see
		// the report: recomputing it in the test would only prove the code does what it does.
		assertTrue(segment.matches("^[0-9a-f]{1,8}$"),
				"the cache segment is not an opaque hex hash: " + segment);

		// One per deployment: a completely different ~./ URL on the same page - the fixture
		// <stylesheet>, emitted by a different code path - carries the identical segment.
		String sepHref = getEval("(function(){var l=jq('link[href*=\"labfix-separate-link\"]')[0];"
				+ "return l ? l.getAttribute('href') : '';})()");
		assertFalse(sepHref.isEmpty(), "the fixture <stylesheet> link is missing, so there is nothing to compare");
		assertEquals(segment, cacheSegment(sepHref),
				"two ~./ URLs on one page carry different cache segments: " + wcsHref + " vs " + sepHref);

		// ...and it is not the version string, which is what "opaque" means observably.
		String version = getEval("zk.version");
		assertTrue(version.matches("^\\d+(\\.\\d+)+.*"),
				"could not read the client's ZK version, so 'not the version string' is untestable: " + version);
		assertNotEquals(version, segment, "the cache segment IS the version string, so it is not opaque");
		assertFalse(segment.contains(version), "the cache segment contains the version string: " + segment);
		assertFalse(segment.contains("."), "the cache segment carries a dotted version: " + segment);

		assertNoJSError();
	}

	private String cacheSegment(String href) {
		Matcher m = Pattern.compile("^\\Q" + getContextPath() + "\\E/zkau/web/([^/]+)/").matcher(href);
		assertTrue(m.find(), "not a ~./ URL: " + href);
		return m.group(1);
	}

	// ------------------------------------------------------------------------- helpers

	/** The absolute URL of the language's one aggregated stylesheet, as the page links it. */
	private String wcsUrl() {
		String url = getEval("(function(){var l=jq('link[href*=\"zk.wcs\"]')[0];return l?l.href:'';})()");
		assertTrue(url.startsWith("http"), "could not resolve the zk.wcs URL from the page: " + url);
		return url;
	}

	private String computed(String domId, String cssProperty) {
		String v = getEval("(function(){var n=document.getElementById('" + domId + "');"
				+ "return n ? getComputedStyle(n).getPropertyValue('" + cssProperty + "') : 'NO-SUCH-ELEMENT';})()");
		assertNotEquals("NO-SUCH-ELEMENT", v, "the fixture page has no element with id " + domId);
		return v;
	}

	/** The body of the first rule whose selector is exactly {@code selector}. */
	private static String ruleBlock(String css, String selector) {
		Matcher m = Pattern.compile("(?:^|[};/\\n])\\s*" + Pattern.quote(selector) + "\\s*\\{([^}]*)\\}")
				.matcher(css);
		return m.find() ? m.group(1) : "";
	}

	/** The declared value of {@code property} inside a rule body, "" when absent or empty. */
	private static String declaredValue(String ruleBody, String property) {
		Matcher m = Pattern.compile("(?:^|[;{])\\s*" + Pattern.quote(property) + "\\s*:([^;}]*)")
				.matcher(ruleBody);
		return m.find() ? m.group(1).trim() : "";
	}

	/** The max-age of a Cache-Control header, or -1. */
	private static long maxAge(String cacheControl) {
		Matcher m = Pattern.compile("max-age=(\\d+)").matcher(cacheControl);
		return m.find() ? Long.parseLong(m.group(1)) : -1;
	}

	private static int count(String haystack, String needle) {
		int n = 0;
		for (int at = haystack.indexOf(needle); at >= 0; at = haystack.indexOf(needle, at + needle.length()))
			n++;
		return n;
	}

	/** A window of the served body around a marker, for a readable failure message. */
	private static String around(String body, String marker) {
		int at = body.indexOf(marker);
		if (at < 0)
			return "<" + marker + " is not in the response at all>";
		return body.substring(Math.max(0, at - 200), Math.min(body.length(), at + 200));
	}

	private static int declarations(Path descriptor, String value) throws Exception {
		String xml = read(descriptor);
		int n = 0;
		Matcher m = Pattern.compile("<css-uri>\\s*([^<\\s]+)\\s*</css-uri>").matcher(xml);
		while (m.find())
			if (value.equals(m.group(1)))
				n++;
		return n;
	}

	private static String read(Path p) throws Exception {
		assertTrue(Files.isRegularFile(p), "expected file does not exist: " + p.toAbsolutePath());
		return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
	}

	private static void append(Path p, String text) throws Exception {
		byte[] now = Files.readAllBytes(p);
		byte[] extra = text.getBytes(StandardCharsets.UTF_8);
		byte[] merged = new byte[now.length + extra.length];
		System.arraycopy(now, 0, merged, 0, now.length);
		System.arraycopy(extra, 0, merged, now.length, extra.length);
		Files.write(p, merged);
	}

	private static String hex(byte[] bytes, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n && i < bytes.length; i++)
			sb.append(String.format("%02x", bytes[i]));
		return sb.toString();
	}

	private static HttpURLConnection open(String url) throws Exception {
		HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
		// No gzip and no caching anywhere: WcsExtendlet compresses bodies over 200 bytes,
		// and this test re-fetches the same URL after editing a file behind it.
		http.setUseCaches(false);
		http.setRequestProperty("Accept-Encoding", "identity");
		http.setRequestProperty("Cache-Control", "no-cache");
		return http;
	}

	private static String httpGet(String url) throws Exception {
		return httpGetWithValidator(url)[0];
	}

	/** {@code {body, Last-Modified, Cache-Control}} - the body plus what the client is told. */
	private static String[] httpGetWithValidator(String url) throws Exception {
		HttpURLConnection http = open(url);
		http.setRequestProperty("Accept", "text/css,*/*");
		try {
			assertEquals(200, http.getResponseCode(), "GET " + url);
			assertTrue(String.valueOf(http.getContentType()).contains("text/css"),
					"the aggregated stylesheet is not served as text/css: " + http.getContentType());
			String body = new String(readAll(http.getInputStream()), StandardCharsets.UTF_8);
			String lastModified = http.getHeaderField("Last-Modified");
			String cacheControl = http.getHeaderField("Cache-Control");
			return new String[] { body, lastModified == null ? "" : lastModified,
					cacheControl == null ? "" : cacheControl };
		} finally {
			http.disconnect();
		}
	}

	private static byte[] readAll(InputStream in) throws Exception {
		try (InputStream stream = in) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			for (int n; (n = stream.read(buf)) > 0;)
				out.write(buf, 0, n);
			return out.toByteArray();
		}
	}
}
