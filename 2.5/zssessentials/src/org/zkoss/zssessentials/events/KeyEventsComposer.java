package org.zkoss.zssessentials.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.event.EditboxEditingEvent;
import org.zkoss.zss.ui.event.Events;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zss.ui.event.StartEditingEvent;
import org.zkoss.zss.ui.event.StopEditingEvent;
import org.zkoss.zul.Textbox;

public class KeyEventsComposer extends GenericForwardComposer {

	private transient Spreadsheet ss;
	private transient Textbox editBox;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ss.addEventListener(org.zkoss.zk.ui.event.Events.ON_CTRL_KEY,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						doCtrlKeyEvent((KeyEvent) event);
					}
				});
	}

	private void doCtrlKeyEvent(KeyEvent event) {
		editBox.setValue("" + event.getKeyCode());
		char c = (char) event.getKeyCode();
		switch (c) {
		case 'X':
			// do cut operation
			break;
		case 'C':
			// do copy operation
			break;
		case 'V':
			// do paste operation
			break;
		default:
			return;
		}
	}
}
