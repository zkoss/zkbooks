/* AutoFilterComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		May 20, 2011 4:59:17 PM , Created by sam
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.AutoFilter;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

/**
 * @author sam
 *
 */
public class AutoFilterComposer extends GenericForwardComposer {
	
	Spreadsheet spreadsheet;
	int rowIndex;
	int columnIndex;

	Button autoFilter;
	Button reapplyAutoFilter;
	Button clearAutoFilter;
	
	/**
	 * Cache current cell when focus on cell
	 * @param event
	 */
	public void onCellFocused$spreadsheet(CellEvent event) {
		rowIndex = event.getRow();
		columnIndex = event.getColumn();
	}
	
	/**
	 * Toggle AutoFilter to sheet
	 */
	public void onClick$autoFilter() {
		AutoFilter autoFilter = Ranges.range(spreadsheet.getSelectedSheet(), rowIndex, columnIndex).autoFilter();
		alert(autoFilter == null ? "Clear AutoFilter" : "Applied AutoFilter");
	}
	
	/**
	 * Reapply AutoFilter use same criteria
	 */
	public void onClick$reapplyAutoFilter() {
		Ranges.range(spreadsheet.getSelectedSheet()).applyFilter();
	}
	
	/**
	 * Clear AutoFilter
	 */
	public void onClick$clearAutoFilter() {
		Ranges.range(spreadsheet.getSelectedSheet()).showAllData();
	}
}
