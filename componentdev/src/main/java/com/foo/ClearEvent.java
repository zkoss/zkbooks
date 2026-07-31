/* ClearEvent.java

	A custom event class, so listeners get typed data instead of digging in a Map.
	This is the pattern the "Client/Server Communication" chapter documents.
*/
package com.foo;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/** Fired when the user clears a {@link SimpleLabel} in the browser. */
public class ClearEvent extends Event {

	/** The event name, used on BOTH sides: the client fires it, addClientEvent
	 * declares it, and a ZUL page listens with onClear="...". */
	public static final String NAME = "onClear";

	private final boolean _cleared;

	/** Builds the event from the raw AU request the client sent. */
	public static ClearEvent getClearEvent(AuRequest request) {
		final Map<String, Object> data = request.getData();
		return new ClearEvent(NAME, request.getComponent(),
				AuRequests.getBoolean(data, "cleared"));
	}

	public ClearEvent(String name, Component target, boolean cleared) {
		super(name, target);
		_cleared = cleared;
	}

	/** Whether the label is now cleared (true) or restored (false). */
	public boolean isCleared() {
		return _cleared;
	}
}
