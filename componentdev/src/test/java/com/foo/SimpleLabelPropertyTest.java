/* SimpleLabelPropertyTest.java

	Pure-Java property logic, with no harness at all.

	Worth showing in the book as its own layer: a component is still an ordinary Java
	object, and logic that does not touch the desktop needs neither ZATS nor a browser.

	Note WHY this works: smartUpdate() only queues an AU response when the component
	is attached to a desktop. On a detached component it is a no-op, so the setter can
	be exercised directly. Call setValue() on an ATTACHED component from outside an
	event listener and ZK throws "Components can be accessed only in event listeners" -
	which is exactly why these assertions live here rather than in the ZATS test.
*/
package com.foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleLabelPropertyTest {

	@Test
	@DisplayName("value defaults to the empty string, not null")
	void valueDefaultsToEmpty() {
		assertEquals("", new SimpleLabel().getValue());
	}

	@Test
	@DisplayName("setValue(null) is normalized to the empty string, never NPEs")
	void nullValueIsNormalized() {
		SimpleLabel label = new SimpleLabel();
		label.setValue("something");
		label.setValue(null);
		assertEquals("", label.getValue());
	}

	@Test
	@DisplayName("setValue stores what it was given")
	void valueRoundTrips() {
		SimpleLabel label = new SimpleLabel();
		label.setValue("Hello ZK");
		assertEquals("Hello ZK", label.getValue());
	}

	@Test
	@DisplayName("getZclass() supplies the default the mold and $s() both build on")
	void zclassHasADefault() {
		assertEquals("z-simplelabel", new SimpleLabel().getZclass());
	}

	@Test
	@DisplayName("setZclass overrides the default without the mold having to care")
	void zclassIsOverridable() {
		SimpleLabel label = new SimpleLabel();
		label.setZclass("my-label");
		assertEquals("my-label", label.getZclass());
	}

	@Test
	@DisplayName("a fresh component is not cleared")
	void startsUncleared() {
		assertFalse(new SimpleLabel().isCleared());
	}
}
