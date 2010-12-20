/* DeleteRangeComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 8:20:11 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * DeleteRangeComposer demonstrate how to delete cell using range 
 * @author Sam
 *
 */
public class DeleteRangeComposer extends GenericForwardComposer {

	Menuitem shiftCellLeft;
	Menuitem shiftCellUp;
	Menuitem deleteEntireRow;
	Menuitem deleteEntireColumn;
	
	int rowIndex;
	int colIndex;
	Worksheet currentSheet;
	Spreadsheet spreadsheet;
	Menupopup cellMenupopup;
	
	//-- super --//
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		cellMenupopup.setWidgetListener("onOpen", "this.$f('" + spreadsheet.getId() + "').focus(false)");
	}

	/**
	 * Save current cell and cell's index
	 * @param event
	 */
	public void onCellRightClick$spreadsheet(CellMouseEvent event) {
		rowIndex = event.getRow();
		colIndex = event.getColumn();
		currentSheet = event.getSheet();
		cellMenupopup.open(event.getPageX(), event.getPageY());
	}

	/**
	 * Delete one cell and shift cell left
	 */
	public void onClick$shiftCellLeft() {
		Range rng = Ranges.range(currentSheet, rowIndex, colIndex);
		rng.delete(Range.SHIFT_LEFT);
	}
	
	/**
	 * Delete one cell and shift cell up
	 */
	public void onClick$shiftCellUp() {
		final Range rng = Ranges.range(currentSheet, rowIndex, colIndex);
		rng.delete(Range.SHIFT_UP);
	}
	
	/**
	 * Delete entire row
	 */
	public void onClick$deleteEntireRow() {
		Row row = currentSheet.getRow(rowIndex);
		int lCol = row.getFirstCellNum();
		int rCol  = row.getLastCellNum();
		Ranges.range(currentSheet, rowIndex, lCol, rowIndex, rCol).delete(Range.SHIFT_UP);
	}
	
	/**
	 * Delete entire column
	 */
	public void onClick$deleteEntireColumn() {
		int tRow = currentSheet.getFirstRowNum();
		int bRow = currentSheet.getPhysicalNumberOfRows();
		Ranges.range(currentSheet, tRow, colIndex, bRow, colIndex).delete(Range.SHIFT_LEFT);;	
	}
}
