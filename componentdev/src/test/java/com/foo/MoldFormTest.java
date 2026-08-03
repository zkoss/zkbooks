/* MoldFormTest.java

	The SERVER half of the multiple-molds contract, asserted with ZATS.

	Written from tasks/research/component-dev/styling-dom.md and from the book page
	zk_component_dev_essentials/implementing_molds.md, not from the lab source.

	Scope of this layer, stated so nothing is claimed that it cannot see: ZATS never
	renders HTML and never runs JavaScript, so it can only show that the server knows
	about both <mold> declarations and that the mold PROPERTY carries the right value.
	Whether either mold file exists, is delivered, or emits the documented DOM is
	MoldFormIT's job.

	Runs under surefire in `mvn test`. No browser.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.zats.mimic.DesktopAgent;

class MoldFormTest extends LabZatsTestCase {

	private static final String PAGE = "/moldforms.zul";

	@Test
	@DisplayName("each <mold> declared in lang-addon.xml becomes a mold the server knows by its <mold-name>")
	void declaredMoldNamesReachTheComponentDefinition() {
		DesktopAgent desktop = connect(PAGE);

		Collection<String> molds = desktop.query("#named").as(SimpleLabel.class)
				.getDefinition().getMoldNames();

		// <simplelabel> declares two <mold> elements: default and fancy. Drop either
		// declaration from lang-addon.xml and this goes red - which is the failure a
		// reader hits as "mold=... is silently ignored".
		assertTrue(molds.contains("default"), "the default mold is not registered: " + molds);
		assertTrue(molds.contains("fancy"), "the second <mold> did not reach the definition: " + molds);
		assertFalse(molds.contains("nosuch"),
				"the definition reports a mold nobody declared, so this assertion proves nothing: " + molds);

		// <simplelabelts> declares ONE mold. Asserting the difference is what shows the
		// mold set belongs to the COMPONENT definition rather than to the language.
		Collection<String> tsMolds = desktop.query("#tsnamed").as(SimpleLabel.class)
				.getDefinition().getMoldNames();
		assertTrue(tsMolds.contains("default"), "the TS track's default mold is not registered: " + tsMolds);
		assertFalse(tsMolds.contains("fancy"),
				"<simplelabelts> declares no fancy mold, yet the definition reports one: " + tsMolds);
	}

	@Test
	@DisplayName("a mold is selected either with the ZUL mold attribute or with setMold() in Java, and getMold() reports 'default' when neither is used")
	void moldIsSelectableFromZulAndFromJava() {
		DesktopAgent desktop = connect(PAGE);

		assertEquals("default", desktop.query("#named").as(SimpleLabel.class).getMold(),
				"a component with no mold attribute must report the default mold");
		assertEquals("fancy", desktop.query("#fancydecl").as(SimpleLabel.class).getMold(),
				"mold=\"fancy\" in the ZUL did not select the second mold");
		// The page's zscript called fancyjava.setMold("fancy") - the Java form the book
		// documents next to the ZUL attribute. setMold() rejects an undeclared name with
		// UiException, so a green assertion here also means "fancy" is a declared mold.
		assertEquals("fancy", desktop.query("#fancyjava").as(SimpleLabel.class).getMold(),
				"setMold(\"fancy\") in Java did not select the second mold");
	}
}
