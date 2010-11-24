package org.zkoss.zssessentials.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.EditboxEditingEvent;
import org.zkoss.zss.ui.event.Events;
import org.zkoss.zss.ui.event.StartEditingEvent;
import org.zkoss.zss.ui.event.StopEditingEvent;
import org.zkoss.zul.Textbox;

public class EditingEventsComposer extends GenericForwardComposer {

	private transient Spreadsheet ss;
	private transient Textbox editBox;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ss.addEventListener(Events.ON_EDITBOX_EDITING, new EventListener() {
			public void onEvent(Event event) throws Exception {
				doEditboxEditingEvent((EditboxEditingEvent) event);
			}
		});
		ss.addEventListener(Events.ON_START_EDITING, new EventListener() {
			public void onEvent(Event event) throws Exception {
				doStartEditingEvent((StartEditingEvent) event);
			}
		});
		ss.addEventListener(Events.ON_STOP_EDITING, new EventListener() {
			public void onEvent(Event event) throws Exception {
				doStopEditingEvent((StopEditingEvent) event);
			}
		});
	}

	private void doEditboxEditingEvent(EditboxEditingEvent event) {
		editBox.setValue((String) event.getEditingValue());
	}

	private void doStartEditingEvent(StartEditingEvent event) {
		// for example you can restrict which cells can be edited here
	}

	private void doStopEditingEvent(StopEditingEvent event) {
		// for example you can apply value or style transformation here on
		// entered value
	}
}
