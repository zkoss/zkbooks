/* CssSourcePropertyTest.java

	The SOURCE half of the ".css.dsp earns its place" claim of
	zk_component_dev_essentials/delivering_your_component_stylesheet.md (U5):

	  "Reach for .css.dsp when a declaration genuinely needs the server. The one case that
	   recurs is a resource shipped inside your own jar, where hard-coding a URL would break
	   under a different context path or ZK build:
	   url(${c:encodeThemeURL("~./js/com/foo/img/dot.png")}). That expression needs the core
	   taglib declared at the top of the file:
	   <%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>"

	Both quoted strings are asserted BYTE FOR BYTE against the shipped stylesheet, because
	the book's fenced blocks are extracted from the lab: a snippet that no longer matches
	the file it was taken from is a defect even when the runtime behaviour is fine. The
	runtime half - that the expression resolves to a URL that really serves the PNG - is
	CssDeliveryIT's.

	Layer: plain JUnit (surefire). No server, no browser: these are properties of two
	files in src/main/resources.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CssSourcePropertyTest {

	private static final Path STYLESHEET = Paths
			.get("src/main/resources/web/js/com/foo/css/simplelabel.css.dsp");
	private static final Path IMAGE = Paths.get("src/main/resources/web/js/com/foo/img/dot.png");

	/** The expression the book quotes, verbatim. */
	private static final String EXPRESSION = "url(${c:encodeThemeURL(\"~./js/com/foo/img/dot.png\")})";
	/** The taglib directive the book quotes, verbatim. */
	private static final String TAGLIB = "<%@ taglib uri=\"http://www.zkoss.org/dsp/web/core\" prefix=\"c\" %>";

	@Test
	@DisplayName("the .css.dsp resolves a classpath resource with url(${c:encodeThemeURL(\"~./js/com/foo/img/dot.png\")}), and that expression needs the core taglib at the top of the file")
	void theStylesheetUsesEncodeThemeUrlAndDeclaresTheCoreTaglib() throws IOException {
		String css = read(STYLESHEET);

		// MY OWN TEST WAS WRONG TWICE HERE, and both fixes are recorded rather than hidden.
		// (1) It first asserted count(css, EXPRESSION) == 1 and went red with "expected 1 but
		//     was 2": the file names the expression twice - once in the <%-- --%> header
		//     comment that explains it, once as the real declaration - so counting the whole
		//     file was the wrong measure.
		// (2) The second attempt scoped the count to the .z-simplelabel-fancy rule body and
		//     went red with "expected 1 but was 0": a rule body cannot be cut at the first
		//     '}' when the body contains ${...}, whose own '}' comes first.
		// What the book's snippet has to match is the DECLARATION, i.e. the part of the file
		// the interpreter sees, which is everything after the taglib directive.
		String served = css.substring(css.indexOf(TAGLIB) + TAGLIB.length());
		assertEquals(1, count(served, EXPRESSION),
				"the book quotes this expression verbatim, and the lab's declaration must be where it"
						+ " was quoted from: " + EXPRESSION);
		assertEquals(1, count(css, TAGLIB),
				"the taglib directive the book quotes is not in the file exactly once: " + TAGLIB);

		// "at the top of the file": before the expression that needs it. A DSP taglib
		// declared after its first use is not in scope.
		assertTrue(css.indexOf(TAGLIB) < css.lastIndexOf(EXPRESSION),
				"the taglib is declared after the expression that uses it");

		// And the resource the expression names really ships, as a real PNG rather than a
		// placeholder - otherwise the runtime assertion in CssDeliveryIT would be checking a
		// URL that resolves to nothing.
		assertTrue(Files.isRegularFile(IMAGE), "the resource the expression names does not exist: " + IMAGE);
		byte[] png = Files.readAllBytes(IMAGE);
		assertTrue(png.length > 8, "the shipped image is empty: " + IMAGE);
		assertEquals("89504e47", hex(png, 4),
				"the shipped image does not start with the PNG signature: " + IMAGE);
	}

	@Test
	@DisplayName("the EL is confined to the one rule that needs the server: every other rule of the component stylesheet is plain CSS")
	void onlyTheRuleThatNeedsTheServerContainsEl() throws IOException {
		String css = read(STYLESHEET);
		// EL is masked FIRST, because a ${...} carries its own '}' and would otherwise end
		// the rule body it sits in - the trap that made an earlier version of this test read
		// zero expressions inside the rule that has one.
		String body = maskEl(css.substring(css.indexOf(TAGLIB) + TAGLIB.length()));

		// NOTE FOR THE DOC WRITER. delivering_your_component_stylesheet.md:92-94 says
		// "Every rule in the lab's stylesheet is plain CSS for that reason". That is no
		// longer true: .z-simplelabel-fancy carries the encodeThemeURL expression the same
		// page recommends two paragraphs later. This test pins what the file really is -
		// exactly one rule with EL, and it is the resource-URL rule - and the sentence is
		// reported to the doc writer rather than assumed away here.
		assertEquals(1, count(body, MASK),
				"the interpreted part of the stylesheet does not carry exactly one EL expression");
		String fancy = ruleBlock(body, ".z-simplelabel-fancy");
		assertFalse(fancy.isEmpty(), "could not find the .z-simplelabel-fancy rule in " + STYLESHEET);
		assertEquals(1, count(fancy, MASK),
				"the one EL expression is not inside the .z-simplelabel-fancy rule: " + fancy);

		// The two rules the book prints as plain CSS really are plain CSS - which is what
		// makes "use .css.dsp only for the declaration that needs the server" observable in
		// the lab rather than merely advised.
		String root = ruleBlock(body, ".z-simplelabel");
		String inner = ruleBlock(body, ".z-simplelabel-inner");
		assertFalse(root.isEmpty(), "could not find the .z-simplelabel rule");
		assertFalse(inner.isEmpty(), "could not find the .z-simplelabel-inner rule");
		assertFalse(root.contains(MASK), "the .z-simplelabel rule needs no server-side EL: " + root);
		assertFalse(inner.contains(MASK), "the .z-simplelabel-inner rule needs no server-side EL: " + inner);

		// Self-check of the extractor: without it, an empty match would make the two
		// assertions above vacuously green.
		assertEquals(" a: b; ", ruleBlock(".x { a: b; }\n.xy { c: d; }", ".x"),
				"the rule extractor cannot read a rule body");
		assertEquals("", ruleBlock(".x { a: b; }", ".nosuch"),
				"the rule extractor invents a body for a selector that is absent");
	}

	@Test
	@DisplayName("the stylesheet's own comment records the plain-.css equivalence the delivery page cites")
	void theFileRecordsThePlainCssEquivalence() throws IOException {
		// The book says "the file's own comment records that pointing <css-uri> at a plain
		// .css produced byte-identical output". That is a claim about the FILE, so this is
		// the only layer that can check it - and it is worth checking, because a comment is
		// the one kind of evidence a reader cannot re-run.
		String css = read(STYLESHEET);
		assertTrue(css.contains("byte-identical"),
				"the delivery page cites this file's comment as its evidence for the plain-.css"
						+ " equivalence, and the comment no longer records it: " + STYLESHEET);
	}

	// ------------------------------------------------------------------------- helpers

	/** Stands in for one {@code ${...}} expression, so a rule body can be cut at a brace. */
	private static final String MASK = "EL_MASKED";

	private static String maskEl(String css) {
		return css.replaceAll("\\$\\{[^}]*\\}", MASK);
	}

	/** The body of the first rule whose selector is exactly {@code selector}. */
	private static String ruleBlock(String css, String selector) {
		Matcher m = Pattern.compile("(?:^|[};/\\n])\\s*" + Pattern.quote(selector) + "\\s*\\{([^}]*)\\}")
				.matcher(css);
		return m.find() ? m.group(1) : "";
	}

	/** The first {@code n} bytes as lowercase hex. */
	private static String hex(byte[] bytes, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n && i < bytes.length; i++)
			sb.append(String.format("%02x", bytes[i]));
		return sb.toString();
	}

	private static int count(String haystack, String needle) {
		int n = 0;
		for (int at = haystack.indexOf(needle); at >= 0; at = haystack.indexOf(needle, at + needle.length()))
			n++;
		return n;
	}

	private static String read(Path p) throws IOException {
		assertTrue(Files.isRegularFile(p), "expected file does not exist: " + p.toAbsolutePath());
		return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
	}
}
