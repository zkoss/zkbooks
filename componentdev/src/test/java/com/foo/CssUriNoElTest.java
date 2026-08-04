/* CssUriNoElTest.java

	"The element accepts no EL." - the last sentence of the "Declare it with css-uri"
	section of zk_component_dev_essentials/delivering_your_component_stylesheet.md (U1),
	and the only claim in that chapter that cannot be asserted from a descriptor the suite
	loads for real: an EL-bearing <css-uri> aborts the parse of its language addon, so
	putting one in src/main/resources/metainfo/zk/lang-addon.xml would silently drop the
	component the whole lab is about.

	The seam is therefore DefinitionLoaders.addAddon(Locator, URL), whose javadoc
	documents exactly this use ("usually used when an application want to load additional
	addons"). With the language already loaded it parses immediately, and a parse failure
	is logged and swallowed - which is what makes the consequence observable at all:

	  DefinitionLoaders.java:296-300
	    private static void loadLang(Locator locator, URL url, boolean addon) {
	        try { parseLang(new SAXBuilder(true, false, true).build(url), locator, url, addon);
	        } catch (Exception ex) { log.error("Failed to load " + ... ); //keep running }

	The fixture it loads is src/test/resources/fixtures/elprobe-lang-addon.xml - an
	ANTI-PATTERN, deliberately NOT under metainfo/zk/ so nothing auto-loads it.

	This class stands alone on purpose: it mutates this JVM's LanguageDefinition. surefire
	runs forkCount=1/reuseForks=false, a fresh JVM per test class, so the mutation cannot
	reach any other test.

	Runs under surefire in `mvn test`. No browser.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.zkoss.util.resource.Locators;
import org.zkoss.zk.ui.metainfo.DefinitionLoaders;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;

// The order is load-bearing: the first test asserts a BASELINE in which the anti-pattern
// addon has not been loaded yet, and both tests load it. Without a fixed order the
// baseline would depend on JUnit's arbitrary method order.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CssUriNoElTest extends LabZatsTestCase {

	private static final String FIXTURE = "fixtures/elprobe-lang-addon.xml";

	@Test
	@Order(1)
	@DisplayName("<css-uri> accepts no EL: the value is not merely ignored - it aborts the parse of that addon, so the declaration and everything after it never load")
	void anElBearingCssUriAbortsTheAddonParse() {
		LanguageDefinition lang = LanguageDefinition.lookup("xul/html");

		// Baseline, so nothing below can be vacuously green: none of the three probe
		// components exists yet, and no registered CSS URI carries an expression.
		assertNull(lang.getComponentDefinitionIfAny("elprobebefore"), "baseline: the fixture is already loaded");
		assertNull(lang.getComponentDefinitionIfAny("elprobeel"), "baseline: the fixture is already loaded");
		assertNull(lang.getComponentDefinitionIfAny("elprobeafter"), "baseline: the fixture is already loaded");
		assertTrue(elBearing(lang).isEmpty(), "baseline: a <css-uri> with EL is already registered: " + elBearing(lang));

		URL url = Locators.getDefault().getResource(FIXTURE);
		assertNotNull(url, "the anti-pattern fixture is not on the test classpath: " + FIXTURE);

		DefinitionLoaders.addAddon(Locators.getDefault(), url);

		// The addon really was parsed - the component declared BEFORE the offending mold is
		// registered. Without this the two assertions below would also hold for a file that
		// was never read at all, which is the failure mode this test exists to rule out.
		assertNotNull(lang.getComponentDefinitionIfAny("elprobebefore"),
				"the fixture addon was never parsed, so nothing here says anything about EL");

		// THE CLAIM. No EL reached the language's CSS list...
		assertTrue(elBearing(lang).isEmpty(),
				"a <css-uri> containing ${...} was accepted into the language: " + elBearing(lang));

		// ...and the reason is not that it was skipped: the parse ABORTED at that element,
		// so the component declared AFTER it is silently missing. That asymmetry is the
		// whole consequence a reader has to know about, and the reason an EL-bearing value
		// can never sit in a descriptor that ships.
		assertNull(lang.getComponentDefinitionIfAny("elprobeafter"),
				"the addon kept parsing past the EL-bearing <css-uri>; the consequence is milder than"
						+ " 'the declaration and everything after it never load'");
	}

	@Test
	@Order(2)
	@DisplayName("the only report of an EL-bearing <css-uri> is a server-side log line: ZK's own \"css-uri does not support EL expressions\"")
	void theRejectionIsReportedOnlyToTheServerLog() {
		// Deliberately a SEPARATE test from the structural assertions above: this one is
		// about the diagnostic, and its failure must not be confusable with "the EL was
		// accepted". slf4j-simple writes ERROR to System.err, and its default output choice
		// resolves System.err on each write, so the redirection below is seen.
		PrintStream saved = System.err;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		try {
			System.setErr(new PrintStream(captured, true, "UTF-8"));
			URL url = Locators.getDefault().getResource(FIXTURE);
			assertNotNull(url, "the anti-pattern fixture is not on the test classpath: " + FIXTURE);
			DefinitionLoaders.addAddon(Locators.getDefault(), url);
		} catch (java.io.UnsupportedEncodingException ex) {
			throw new IllegalStateException(ex);
		} finally {
			System.setErr(saved);
		}

		String log = new String(captured.toByteArray(), StandardCharsets.UTF_8);
		assertTrue(log.contains("css-uri does not support EL expressions"),
				"ZK's own rejection message was not logged; the failure would then be entirely silent: " + log);
		assertTrue(log.contains("Failed to load addon"),
				"the log does not say the ADDON failed to load, only that one element was rejected: " + log);
	}

	/** Every registered CSS URI that still carries an EL expression. */
	private static List<String> elBearing(LanguageDefinition lang) {
		List<String> found = new ArrayList<>();
		for (String uri : lang.getCSSURIs())
			if (uri.contains("${"))
				found.add(uri);
		return found;
	}
}
