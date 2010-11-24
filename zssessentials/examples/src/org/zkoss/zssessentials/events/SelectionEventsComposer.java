package org.zkoss.zssessentials.events;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.event.SelectionChangeEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

public class SelectionEventsComposer extends GenericForwardComposer {

	Spreadsheet ss;
	Label currentAreaLbl;
	Label origAreaLbl;
	Button selectBtn;

	public void onCellSelection$ss(CellSelectionEvent event) {
		int s = event.getSelectionType();
		
		Rect rect = ss.getSelection();
		String area = "None";
		switch (s) {
		case CellSelectionEvent.SELECT_CELLS:
			area = ss.getColumntitle(rect.getLeft()) + ss.getRowtitle(rect.getTop()) + ":" + 
				ss.getColumntitle(rect.getRight()) + ss.getRowtitle(rect.getBottom());
			break;
		case CellSelectionEvent.SELECT_COLUMN:
			area = "COL:" + ss.getColumntitle(rect.getLeft());
			break;
		case CellSelectionEvent.SELECT_ROW:
			area = "ROW:" + ss.getRowtitle(rect.getTop());
			break;
		case CellSelectionEvent.SELECT_ALL:
			break;
		}
		currentAreaLbl.setValue(area);
	}
	
	public void onSelectionChange$ss(SelectionChangeEvent event) {
		int s = event.getSelectionType();
		Rect rect = ss.getSelection();
		String currentArea = ss.getColumntitle(rect.getLeft()) + ss.getRowtitle(rect.getTop()) + ":" + 
		ss.getColumntitle(rect.getRight()) + ss.getRowtitle(rect.getBottom());
		String area = "None";
		switch (s) {
		case CellSelectionEvent.SELECT_CELLS:
			area = ss.getColumntitle(event.getOrigleft()) + ss.getRowtitle(event.getOrigtop()) + ":" + 
				ss.getColumntitle(event.getOrigright()) + ss.getRowtitle(event.getOrigbottom());
			break;
		case CellSelectionEvent.SELECT_COLUMN:
			area = "COL:" + ss.getColumntitle(event.getOrigleft());
			break;
		case CellSelectionEvent.SELECT_ROW:
			area = "ROW:" + ss.getRowtitle(event.getOrigtop());
			break;
		case CellSelectionEvent.SELECT_ALL:
			break;
		}
		origAreaLbl.setValue(area);
		currentAreaLbl.setValue(currentArea);
	}
}
