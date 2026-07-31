/* SimpleLabelTest.java

	The server-side contract of com.foo.SimpleLabel, asserted with ZATS.

	Each test names the DOCUMENTED behavior it protects, so that when one fails it is
	clear whether the code drifted or the book was wrong.

	Runs under surefire in `mvn test`. No browser, no network.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.AuAgent;
import org.zkoss.zats.mimic.operation.AuData;
import org.zkoss.zul.Label;

class SimpleLabelTest extends LabZatsTestCase {

	@Test
	@DisplayName("lang-addon.xml maps <simplelabel> to com.foo.SimpleLabel")
	void componentNameResolves() {
		DesktopAgent desktop = connect("/simplelabel.zul");

		ComponentAgent greet = desktop.query("#greet");
		assertNotNull(greet, "lang-addon.xml did not map <simplelabel> to a component");
		// as(Class) hands back the REAL server-side instance, not a proxy.
		assertTrue(greet.as(Object.class) instanceof SimpleLabel,
				"<simplelabel> resolved to the wrong class");
	}

	@Test
	@DisplayName("the value property round-trips through the ZUL attribute")
	void valuePropertyRoundTrips() {
		DesktopAgent desktop = connect("/simplelabel.zul");
		assertEquals("Hello ZK", desktop.query("#greet").as(SimpleLabel.class).getValue());
	}

	@Test
	@DisplayName("mold=\"fancy\" in ZUL actually selects the second mold")
	void moldAttributeSelectsTheSecondMold() {
		DesktopAgent desktop = connect("/simplelabel.zul");

		assertEquals("default", desktop.query("#greet").as(SimpleLabel.class).getMold());
		assertEquals("fancy", desktop.query("#fancy").as(SimpleLabel.class).getMold());
	}

	@Test
	@DisplayName("a component starts un-cleared")
	void startsUncleared() {
		DesktopAgent desktop = connect("/simplelabel.zul");
		assertFalse(desktop.query("#greet").as(SimpleLabel.class).isCleared());
	}

	@Test
	@DisplayName("an onClear AU request updates server state and reaches the page's listener")
	void clearEventReachesTheServer() {
		DesktopAgent desktop = connect("/simplelabel.zul");

		// This is exactly the AU request the widget's fire('onClear', {cleared: true})
		// produces in a browser. AuAgent posts an arbitrary event name with arbitrary
		// data, which is how a CUSTOM client event is driven from ZATS - click() would
		// only send onClick, and the component does not listen for that.
		//
		// No browser and no JavaScript is involved, so this proves the SERVER half:
		// addClientEvent declared the event, service() decoded it, and postEvent
		// delivered it. Whether SimpleLabel.js actually fires it is the WebDriver
		// suite's job.
		desktop.query("#greet").as(AuAgent.class)
				.post(new AuData(ClearEvent.NAME).setData("cleared", true));

		assertTrue(desktop.query("#greet").as(SimpleLabel.class).isCleared(),
				"service() did not read the 'cleared' payload into server state");
		assertEquals("cleared", desktop.query("#msg").as(Label.class).getValue(),
				"the page's onClear listener never ran");
	}

	@Test
	@DisplayName("the onClear payload is honored, not assumed: cleared=false restores")
	void clearEventPayloadIsHonored() {
		DesktopAgent desktop = connect("/simplelabel.zul");

		desktop.query("#greet").as(AuAgent.class)
				.post(new AuData(ClearEvent.NAME).setData("cleared", false));

		assertFalse(desktop.query("#greet").as(SimpleLabel.class).isCleared());
		assertEquals("restored", desktop.query("#msg").as(Label.class).getValue());
	}
}
