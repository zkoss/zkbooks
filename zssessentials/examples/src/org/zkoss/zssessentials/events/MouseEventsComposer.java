package org.zkoss.zssessentials.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zss.ui.event.HeaderEvent;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Textbox;

public class MouseEventsComposer extends GenericForwardComposer {

	private transient Spreadsheet ss;
	private transient Textbox editBox;
	private transient Menupopup cellMenu;
	private transient Menupopup topHeaderMenu;
	private transient Menupopup leftHeaderMenu;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ss.addEventListener(org.zkoss.zss.ui.event.Events.ON_CELL_CLICK,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						doCellClickEvent((CellMouseEvent) event);
					}
				});
		ss.addEventListener(org.zkoss.zss.ui.event.Events.ON_CELL_RIGHT_CLICK,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						doCellRightClickEvent((CellMouseEvent) event);
					}
				});
	}

	private void doCellClickEvent(CellMouseEvent event) {
		Range range = Ranges.range(event.getSheet(),event.getRow(),event.getColumn());
		editBox.setValue(range.getEditText());
	}
	private void doCellRightClickEvent(CellMouseEvent event) {
		cellMenu.open(event.getClientx(),  event.getClienty());
	}
	public void onHeaderRightClick$ss(HeaderMouseEvent event) {
		if (HeaderEvent.TOP_HEADER == event.getType()) {
			topHeaderMenu.open(event.getClientx(),  event.getClienty());
		} else if ((HeaderEvent.LEFT_HEADER == event.getType())) {
			leftHeaderMenu.open(event.getClientx(),  event.getClienty());
		}
	}
}
