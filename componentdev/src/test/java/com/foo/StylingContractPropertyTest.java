/* StylingContractPropertyTest.java

	The parts of the styling/DOM contract that need no desktop and no browser.

	Written from the research note tasks/research/component-dev/styling-dom.md, not from
	the lab source: each test states a documented claim and fails if the claim is false.

	Layer: plain JUnit (surefire). Everything here runs on a DETACHED component or on the
	mold FILES, so there is no "Components can be accessed only in event listeners".
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.zk.ui.sys.JsContentRenderer;

class StylingContractPropertyTest {

	// ------------------------------------------------------------------ F2 + F3

	@Test
	@DisplayName("the server's zclass default is 'z-' + the lowercased LAST SEGMENT of the widget class name - the only reason it agrees with the client's independently computed default")
	void serverDefaultFollowsTheClientDerivationRule() {
		// The client sets widgetName to clsnm.substring(clsnm.lastIndexOf('.')+1)
		// .toLowerCase() and defaults getZclass() to 'z-' + widgetName. The package is
		// ignored, so BOTH com.foo.SimpleLabel and labts.SimpleLabel derive
		// "z-simplelabel". A server default that does not equal that string is a
		// server/client divergence no ZATS test can see, so assert the RULE, not just
		// the literal.
		String lastSegment = SimpleLabel.class.getName().substring(SimpleLabel.class.getName().lastIndexOf('.') + 1);
		assertEquals("z-" + lastSegment.toLowerCase(), new SimpleLabel().getZclass(),
				"the server default must equal the client's 'z-' + lowercased last segment, or the two disagree silently");
	}

	@Test
	@DisplayName("renderProperties transmits the _zclass FIELD, not getZclass(), so an overridden server-side default never reaches the client")
	void renderPropertiesSendsTheFieldNotTheGetter() throws IOException {
		SimpleLabel plain = new SimpleLabel();
		plain.setValue("Hello ZK");
		String rendered = render(plain);

		// Sanity: this renderer really did capture the component's properties.
		assertTrue(rendered.contains("value"),
				"renderProperties() rendered no 'value' at all; the probe is not seeing the component: " + rendered);

		// The claim: HtmlBasedComponent.renderProperties does render(renderer, "zclass",
		// _zclass) - the FIELD, which is still null even though getZclass() answers
		// "z-simplelabel". So nothing about zclass is transmitted at first paint.
		assertFalse(rendered.contains("zclass"),
				"zclass was transmitted although the FIELD is null; the client is supposed to recompute the default itself: "
						+ rendered);

		// And the counter-case, which proves the assertion above is about the field
		// rather than about zclass never being rendered at all.
		SimpleLabel custom = new SimpleLabel();
		custom.setZclass("my-label");
		String renderedCustom = render(custom);
		assertTrue(renderedCustom.contains("zclass"),
				"an explicitly set zclass must be transmitted: " + renderedCustom);
		// JsContentRenderer escapes '-' as '\-' inside a JS string literal, hence the
		// unescaping before comparing.
		assertTrue(renderedCustom.replace("\\", "").contains("my-label"),
				"the transmitted zclass is not the value that was set: " + renderedCustom);
	}

	private static String render(SimpleLabel label) throws IOException {
		JsContentRenderer renderer = new JsContentRenderer();
		label.renderProperties(renderer); // protected, and this test is in package com.foo
		return renderer.getBuffer().toString();
	}

	// ---------------------------------------------------------------------- F14

	@Test
	@DisplayName("no mold hard-codes a zclass-derived class: every z- literal in a mold must be a framework-global z-icon-*")
	void moldsRouteScopedClassesThroughDollarS() throws IOException {
		// The scanner must be able to see the defect it exists to catch. Without this
		// pair the loop below would be green even if the scan were broken.
		assertEquals(Collections.singletonList("z-bogus"),
				hardCodedZClasses("out.push('<i class=\"z-bogus\">');"),
				"the scanner cannot see a hard-coded z- class");
		assertEquals(Collections.emptyList(),
				hardCodedZClasses("out.push('<i class=\"z-icon-times\">');"),
				"the scanner must accept the framework-global z-icon-* glyph classes");
		assertEquals(Collections.emptyList(),
				hardCodedZClasses("out.push('<span class=\"', this.$s('inner'), '\">');"),
				"the scanner must accept a $s() expression");

		List<Path> molds = moldFiles();
		assertFalse(molds.isEmpty(), "found no mold files under src/main/resources/web/js/**/mold/ - the scan path is wrong");

		for (Path mold : molds) {
			String source = new String(Files.readAllBytes(mold), StandardCharsets.UTF_8);
			assertEquals(Collections.emptyList(), hardCodedZClasses(source),
					mold + " hard-codes a z- class; a class that must follow the widget's zclass belongs in $s()");
		}
	}

	private static List<Path> moldFiles() throws IOException {
		Path root = Paths.get("src/main/resources/web/js");
		if (!Files.isDirectory(root))
			return Collections.emptyList();
		try (Stream<Path> walk = Files.walk(root)) {
			return walk.filter(Files::isRegularFile)
					.filter(p -> p.toString().replace('\\', '/').contains("/mold/"))
					.filter(p -> p.toString().endsWith(".js"))
					.collect(Collectors.toList());
		}
	}

	/**
	 * Returns every literal class token inside a {@code class="..."} attribute that
	 * starts with {@code z-} and is not one of the framework-global {@code z-icon-*}
	 * glyph classes.
	 */
	private static List<String> hardCodedZClasses(String moldSource) {
		List<String> found = new ArrayList<>();
		Matcher m = Pattern.compile("class=\"([^\"]*)\"").matcher(moldSource);
		while (m.find()) {
			for (String token : m.group(1).split("\\s+")) {
				if (token.startsWith("z-") && !token.startsWith("z-icon-"))
					found.add(token);
			}
		}
		return found;
	}
}
