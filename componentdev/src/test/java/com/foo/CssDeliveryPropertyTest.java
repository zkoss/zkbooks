/* CssDeliveryPropertyTest.java

	The DECLARATION half of the component-stylesheet contract, asserted at source level.

	Written from the book page
	zk_component_dev_essentials/delivering_your_component_stylesheet.md and from
	zclass_and_scoped_class_names.md. Three of their claims are about files and
	descriptors rather than about runtime behaviour, and nothing in the suite asserted
	them:

	  - "A relative value is rewritten to ~./js/ plus your widget package as a path, plus
	     the value - so css/simplelabel.css.dsp on widget package com.foo resolves to
	     ~./js/com/foo/css/simplelabel.css.dsp, which is classpath
	     /web/js/com/foo/css/simplelabel.css.dsp. Put the file there and no further
	     configuration is needed."
	  - "No *.dsp servlet mapping is required in web.xml for any of this."
	  - "Your stylesheet then mirrors the calls one-for-one."

	Layer: plain JUnit (surefire). The RUNTIME half - that the file really arrives, is
	interpreted and applies - is StylingIT's; this layer pins the arrangement that makes
	it possible, so a move of the file or a re-added servlet mapping is caught with a
	readable message instead of an unexplained computed-style failure.
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CssDeliveryPropertyTest {

	private static final Path LANG_ADDON = Paths.get("src/main/resources/metainfo/zk/lang-addon.xml");
	private static final Path WEB_XML = Paths.get("src/test/webapp/WEB-INF/web.xml");
	private static final Path POM = Paths.get("pom.xml");
	private static final Path JS_ROOT = Paths.get("src/main/resources/web/js");

	@Test
	@DisplayName("a relative <css-uri> resolves under the WIDGET package as a path, so the file must sit at /web/js/<widget-package>/<css-uri> on the classpath")
	void relativeCssUriResolvesUnderTheWidgetPackage() throws IOException {
		String addon = read(LANG_ADDON);

		// Sanity: this really is the descriptor, and it really declares a stylesheet.
		// Without this the loop below would be vacuously green if the file moved.
		assertTrue(addon.contains("<css-uri>"),
				"lang-addon.xml declares no <css-uri> at all, so the delivery page describes nothing the lab does");

		List<String[]> declared = cssUrisByWidgetPackage(addon);
		assertFalse(declared.isEmpty(), "found no <css-uri> inside a <component> that has a <widget-class>");

		for (String[] pair : declared) {
			String widgetPackage = pair[0], cssUri = pair[1];

			// "A relative value" - the documented rewriting only applies to one that does
			// not start with '~./' or '/'.
			assertFalse(cssUri.startsWith("~") || cssUri.startsWith("/"),
					"the book documents the RELATIVE form; this value is absolute: " + cssUri);
			assertFalse(cssUri.contains("${"),
					"<css-uri> accepts no EL, yet this value contains an expression: " + cssUri);

			Path expected = JS_ROOT.resolve(widgetPackage.replace('.', '/')).resolve(cssUri);
			assertTrue(Files.isRegularFile(expected),
					"<css-uri>" + cssUri + "</css-uri> on widget package " + widgetPackage
							+ " resolves to ~./js/" + widgetPackage.replace('.', '/') + "/" + cssUri
							+ ", i.e. classpath /web/js/... - but no file exists at " + expected);
		}
	}

	@Test
	@DisplayName("no *.dsp servlet mapping and no DSP dependency are needed: a .css.dsp reached through <css-uri> is interpreted in-process")
	void noDspServletMappingIsRequired() throws IOException {
		String webXml = read(WEB_XML);

		// Sanity: the descriptor really is the lab's, and it really does map the two
		// servlets ZK needs. Without this, "no .dsp mapping" would also be true of an
		// empty file.
		assertTrue(webXml.contains("DHtmlLayoutServlet"), "this is not the lab's web.xml: " + WEB_XML);
		assertTrue(webXml.contains("DHtmlUpdateServlet"), "the AU engine is not mapped; the lab could not serve ~./ at all");

		// The claim. A *.dsp url-pattern or a DSP servlet class here would mean the
		// stylesheet's delivery depends on a container mapping after all.
		assertFalse(webXml.contains(".dsp"),
				"web.xml mentions .dsp; the book states no *.dsp servlet mapping is required: " + WEB_XML);
		assertFalse(webXml.toLowerCase().contains("dspservlet"),
				"web.xml declares a DSP servlet; the book states the container never sees a .dsp request");

		// ...and no dependency supplies one either. zweb (which carries the in-process
		// DspExtendlet) arrives transitively through zul; nothing is declared for DSP.
		String pom = read(POM);
		Matcher m = Pattern.compile("<artifactId>([^<]*)</artifactId>").matcher(pom);
		while (m.find()) {
			assertFalse(m.group(1).toLowerCase().contains("dsp"),
					"pom.xml declares the DSP artifact " + m.group(1)
							+ "; the book states the .css.dsp is interpreted in-process with no such dependency");
		}

		// And the file the claim is about really is a .css.dsp - otherwise the claim is
		// about nothing.
		assertTrue(cssUrisByWidgetPackage(read(LANG_ADDON)).stream().anyMatch(p -> p[1].endsWith(".css.dsp")),
				"no <css-uri> points at a .css.dsp, so 'no *.dsp mapping is required' is untested by construction");
	}

	@Test
	@DisplayName("the stylesheet mirrors the molds one-for-one: every $s(sub) a mold emits has a .z-simplelabel-<sub> rule")
	void everyScopedClassAMoldEmitsHasAStylesheetRule() throws IOException {
		// Self-check of the extractor, so a broken regex cannot make this vacuous.
		assertEquals(new LinkedHashSet<>(java.util.Arrays.asList("inner", "fancy")),
				scopedSubIds("out.push(this.$s('inner'), ' ', this.$s(\"fancy\"));"),
				"the $s() extractor cannot read the calls a mold makes");
		assertEquals(Collections.emptySet(), scopedSubIds("out.push(this.$s());"),
				"the extractor invents a sub-id for a bare $s() call");

		String css = read(styleSheet());
		Set<String> subIds = new LinkedHashSet<>();
		for (Path mold : moldFiles())
			subIds.addAll(scopedSubIds(read(mold)));

		assertFalse(subIds.isEmpty(), "no mold calls $s(subId) at all, so there is nothing to mirror");

		// getZclass() is the prefix both halves build on; take it from the class under
		// test rather than hard-coding it, so a renamed component keeps this test honest.
		String zclass = new SimpleLabel().getZclass();
		for (String subId : subIds)
			assertTrue(css.contains("." + zclass + "-" + subId),
					"the mold emits $s('" + subId + "') but the stylesheet has no ." + zclass + "-" + subId
							+ " rule; the book states the selectors mirror the calls one-for-one");

		// The reverse direction is deliberately NOT asserted: a stylesheet may legitimately
		// carry a rule for a state class no mold writes literally. What must not happen is
		// a class in the DOM with no rule behind it.
		assertTrue(css.contains("." + zclass + " "), "the stylesheet has no rule for the zclass itself: " + css);
	}

	// ------------------------------------------------------------------------ helpers

	/** {@code {widget-package, css-uri}} for every {@code <css-uri>} in the descriptor. */
	private static List<String[]> cssUrisByWidgetPackage(String addon) {
		List<String[]> out = new ArrayList<>();
		Matcher comp = Pattern.compile("(?s)<component>(.*?)</component>").matcher(addon);
		while (comp.find()) {
			String body = comp.group(1);
			Matcher wc = Pattern.compile("<widget-class>\\s*([^<\\s]+)\\s*</widget-class>").matcher(body);
			if (!wc.find())
				continue;
			String widgetClass = wc.group(1);
			String widgetPackage = widgetClass.substring(0, widgetClass.lastIndexOf('.'));
			Matcher css = Pattern.compile("<css-uri>\\s*([^<\\s]+)\\s*</css-uri>").matcher(body);
			while (css.find())
				out.add(new String[] { widgetPackage, css.group(1) });
		}
		return out;
	}

	/** The sub-ids of every {@code $s('x')} / {@code $s("x")} call in a mold. */
	private static Set<String> scopedSubIds(String moldSource) {
		Set<String> found = new LinkedHashSet<>();
		Matcher m = Pattern.compile("\\$s\\(\\s*['\"]([^'\"]+)['\"]\\s*\\)").matcher(moldSource);
		while (m.find())
			found.add(m.group(1));
		return found;
	}

	private static Path styleSheet() throws IOException {
		List<String[]> declared = cssUrisByWidgetPackage(read(LANG_ADDON));
		assertFalse(declared.isEmpty(), "lang-addon.xml declares no <css-uri>");
		String[] first = declared.get(0);
		return JS_ROOT.resolve(first[0].replace('.', '/')).resolve(first[1]);
	}

	private static List<Path> moldFiles() throws IOException {
		try (Stream<Path> walk = Files.walk(JS_ROOT)) {
			return walk.filter(Files::isRegularFile)
					.filter(p -> p.toString().replace('\\', '/').contains("/mold/"))
					.filter(p -> p.toString().endsWith(".js"))
					.sorted()
					.collect(Collectors.toList());
		}
	}

	private static String read(Path p) throws IOException {
		assertTrue(Files.isRegularFile(p), "expected file does not exist: " + p.toAbsolutePath());
		return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
	}
}
