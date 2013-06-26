/* MoveRangeComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 10:51:11 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;

/**
 * MoveRangeComposer demonstrate how to move cell's value
 * @author Sam
 *
 */
public class MoveRangeComposer extends GenericForwardComposer {

	Spreadsheet spreadsheet;
	Range currentRange;
	
	Intbox moveRows;
	Intbox moveCols;
	Button moveBtn;
	
	//override
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		moveRows.setWidgetListener("onFocus", "this.$f('" + spreadsheet.getId() + "').focus(false)");
		moveCols.setWidgetListener("onFocus", "this.$f('" + spreadsheet.getId() + "').focus(false)");
	}

	/**
	 * Save the current selection
	 * @param event
	 */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
		currentRange = Ranges.range(event.getSheet(), event.getTop(), event.getLeft(),
				event.getBottom(), event.getRight());
	}

	/**
	 * Move current selection
	 */
	public void onClick$moveBtn() {
		Integer row = moveRows.getValue();
		Integer col = moveCols.getValue();
		if (currentRange != null && row != null && col != null) {
			currentRange.move(row, col);
			currentRange = null;
		}
	}
}