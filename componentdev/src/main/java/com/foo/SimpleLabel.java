/* SimpleLabel.java

	The server-side half of the book's running example.

	Deliberately small: one property, one custom event, two molds. Every page in
	"ZK Component Development Essentials" that shows server-side code shows a piece
	of THIS class, so the snippets in the book and the code here must not drift.
*/
package com.foo;

import java.io.IOException;

import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

/**
 * A label that renders its own DOM and can be cleared from the client.
 *
 * <p>Extends {@link XulElement} rather than {@link AbstractComponent} so it
 * inherits sclass/style/tooltiptext and the standard client events.
 */
public class SimpleLabel extends XulElement {

	static {
		// Tell the client engine that this widget may send "onClear" to the server.
		// Without this the client's fire('onClear') is silently dropped.
		//
		// CE_IMPORTANT: deliver even when no server-side listener is registered, so
		// service() below always runs. See org.zkoss.zk.ui.sys.ComponentCtrl for the
		// full flag set (CE_NON_DEFERRABLE, CE_DUPLICATE_IGNORE, CE_REPEAT_IGNORE,
		// CE_BUSY_IGNORE).
		addClientEvent(SimpleLabel.class, ClearEvent.NAME, CE_IMPORTANT);
	}

	private String _value = "";
	private boolean _cleared;

	public String getValue() {
		return _value;
	}

	/**
	 * Sets the label text.
	 *
	 * <p>The equality guard is not an optimisation detail to gloss over: without it
	 * every setter call queues an AU response, and re-setting an unchanged value
	 * would repaint the client for nothing.
	 */
	public void setValue(String value) {
		if (value == null)
			value = "";
		if (!_value.equals(value)) {
			_value = value;
			// Patch just this property on the existing widget instead of
			// re-rendering the whole component (which invalidate() would do).
			smartUpdate("value", _value);
		}
	}

	/** Whether the client has cleared this label. */
	public boolean isCleared() {
		return _cleared;
	}

	/**
	 * Renders every property the widget needs at first paint.
	 *
	 * <p>smartUpdate handles LATER changes; this handles the INITIAL render. A
	 * property that is smartUpdate-ed but never rendered here works when you change
	 * it and breaks on a fresh page load -- a classic and confusing bug.
	 */
	@Override
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer); // never omit: renders id, sclass, style, ...
		render(renderer, "value", _value);
	}

	/**
	 * Receives the "onClear" request the widget fires from the browser.
	 *
	 * <p>The client sends an AU request; this turns it into a server-side event so
	 * application code can listen with a normal event listener.
	 */
	@Override
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (ClearEvent.NAME.equals(cmd)) {
			_cleared = AuRequests.getBoolean(request.getData(), "cleared");
			Events.postEvent(ClearEvent.getClearEvent(request));
		} else {
			super.service(request, everError);
		}
	}

	@Override
	public String getZclass() {
		// The CSS class prefix the mold and the client-side $s() both build on.
		// Returning a null-safe default here is what lets a page override it with
		// setZclass() without the mold having to care.
		return _zclass == null ? "z-simplelabel" : _zclass;
	}
}
