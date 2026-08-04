/* CssUriDeclarationTest.java

	The LANGUAGE-LEVEL half of two claims of
	zk_component_dev_essentials/delivering_your_component_stylesheet.md that no test
	reached before:

	  - "do not worry about declaring the same URI on several molds or components: the
	     list is a set, so duplicates collapse" (U2)
	  - "ZK does not validate the extension of a <css-uri> value at all" (U3)

	Both are properties of the LanguageDefinition the descriptors produced, so this is
	the cheapest layer that can see them: ZATS, which boots the real server and therefore
	the real DefinitionLoaders, without a browser.

	What this layer CANNOT see, and what covers it instead: whether a duplicate URI is
	nevertheless emitted twice into the aggregated response, and whether a resource with
	no interpreter really arrives verbatim. Those are bytes on the wire - CssDeliveryIT.

	The second declaration of the duplicated URI, and the two extension probes, come from
	the TEST-SCOPED language addon src/test/resources/metainfo/zk/lang-addon.xml, which is
	on the surefire/failsafe classpath only.

	Runs under surefire in `mvn test`. No browser.
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.zk.ui.metainfo.LanguageDefinition;

class CssUriDeclarationTest extends LabZatsTestCase {

	/** The URI both descriptors declare, in the rewritten absolute form. */
	private static final String SHARED = "~./js/com/foo/css/simplelabel.css.dsp";
	/** The relative value as it is written in both descriptors. */
	private static final String SHARED_RELATIVE = "css/simplelabel.css.dsp";

	private static final Path SHIPPED_ADDON = Paths.get("src/main/resources/metainfo/zk/lang-addon.xml");
	private static final Path FIXTURE_ADDON = Paths.get("src/test/resources/metainfo/zk/lang-addon.xml");

	@Test
	@DisplayName("duplicates collapse: one URI declared by two separate language addons appears ONCE in the language's CSS list")
	void duplicateCssUriDeclarationsCollapse() throws IOException {
		// The CONTROL, and the only thing that makes the assertion below non-vacuous: the
		// same relative value really is declared twice, in two descriptors, on two
		// different components. Without this a single declaration would also produce "one".
		assertEquals(1, declarations(SHIPPED_ADDON, SHARED_RELATIVE),
				"the shipped descriptor no longer declares " + SHARED_RELATIVE);
		assertEquals(1, declarations(FIXTURE_ADDON, SHARED_RELATIVE),
				"the test-scoped descriptor no longer declares " + SHARED_RELATIVE
						+ ", so nothing duplicates the shipped declaration");

		List<String> uris = cssURIs();

		// Both declarations were RELATIVE, so both were rewritten to ~./js/ + the widget
		// package as a path - the same string, which is what lets the set collapse them.
		assertTrue(uris.contains(SHARED),
				"the relative <css-uri> was not rewritten to " + SHARED + ": " + uris);
		assertEquals(1, Collections.frequency(uris, SHARED),
				"two descriptors declared " + SHARED + " and the language kept both: " + uris);

		// And nothing else collapsed with it: the other three fixture declarations are
		// still there, so "one entry" is deduplication rather than loss.
		assertTrue(uris.size() >= 4,
				"the language lost <css-uri> declarations rather than deduplicating one: " + uris);
	}

	@Test
	@DisplayName("ZK does not validate the extension of a <css-uri> value at all: a plain .css and even a .less are accepted into the language's CSS list")
	void noExtensionIsValidated() {
		List<String> uris = cssURIs();

		// The exemplary case: a plain .css. No extendlet is registered for "css".
		assertTrue(uris.contains("~./labfix/css/labfix-plain.css"),
				"a plain .css <css-uri> was rejected: " + uris);

		// The anti-pattern the book names ("never as a <css-uri> value"). It is accepted
		// all the same - which is the point: nothing tells you, at any point, that no
		// interpreter will ever look at it. Whether the bytes then arrive uncompiled is
		// CssDeliveryIT's assertion, not this layer's.
		assertTrue(uris.contains("~./labfix/css/labfix-never-do-this.less"),
				"a .less <css-uri> was rejected, so ZK DOES validate the extension: " + uris);

		// A .css.dsp is accepted too, so the list is genuinely extension-blind rather than
		// accidentally permissive about one spelling.
		assertTrue(uris.contains("~./labfix/css/labfix-unresolved-el.css.dsp"),
				"a .css.dsp <css-uri> declared with an absolute ~./ value was rejected: " + uris);
		assertTrue(uris.contains(SHARED), "the shipped .css.dsp is missing from the list: " + uris);

		// Control: the list does not simply answer true for everything.
		assertFalse(uris.contains("~./labfix/css/nosuch-never-declared.css"),
				"the CSS list reports a URI nobody declared, so these assertions prove nothing");
	}

	// ------------------------------------------------------------------------- helpers

	/** The language's CSS URIs, as a List so a duplicate would be countable. */
	private static List<String> cssURIs() {
		LanguageDefinition lang = LanguageDefinition.lookup("xul/html");
		Collection<String> uris = lang.getCSSURIs();
		assertFalse(uris.isEmpty(), "the language registered no <css-uri> at all");
		return new ArrayList<>(uris);
	}

	/** How many {@code <css-uri>} elements in this descriptor carry exactly this value. */
	private static int declarations(Path descriptor, String value) throws IOException {
		assertTrue(Files.isRegularFile(descriptor), "expected descriptor does not exist: " + descriptor);
		String xml = new String(Files.readAllBytes(descriptor), StandardCharsets.UTF_8);
		int n = 0;
		Matcher m = Pattern.compile("<css-uri>\\s*([^<\\s]+)\\s*</css-uri>").matcher(xml);
		while (m.find())
			if (value.equals(m.group(1)))
				n++;
		return n;
	}
}
