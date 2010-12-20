/* InsertRangeComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 7:51:07 PM , Created by Sam
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
 * InsertRangeComposer demonstrate how to insert cells using {@link Range}
 * @author Sam
 *
 */
public class InsertRangeComposer extends GenericForwardComposer{

	private Menuitem shiftCellDown;
	private Menuitem insertEntireRow;
	private Menuitem insertEntireColumn;
	
	private int rowIndex;
	private int colIndex;
	private Worksheet currentSheet;
	private Spreadsheet spreadsheet;
	private Menupopup cellMenupopup;
	private Menuitem shiftCellRight;
	
	//override
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		cellMenupopup.setWidgetListener("onOpen", "this.$f('" + spreadsheet.getId() + "').focus(false)");
	}

	/**
	 * Open menu when user right click on cell, and save current index
	 * @param event
	 */
	public void onCellRightClick$spreadsheet(CellMouseEvent event) {
		rowIndex = event.getRow();
		colIndex = event.getColumn();
		currentSheet = event.getSheet();
		cellMenupopup.open(event.getPageX(), event.getPageY());
	}
	
	/**
	 * Insert one cell and shift cell right
	 */
	public void onClick$shiftCellRight() {
		Range rng = Ranges.range(currentSheet, rowIndex, colIndex);
		rng.insert(Range.SHIFT_RIGHT, Range.FORMAT_RIGHTBELOW);
	}
	
	/**
	 * Insert once cell and shift cell down
	 */
	public void onClick$shiftCellDown() {
		final Range rng = Ranges.range(currentSheet, rowIndex, colIndex);
		rng.insert(Range.SHIFT_DOWN, Range.FORMAT_LEFTABOVE);
	}
	
	/**
	 * Insert entire row
	 */
	public void onClick$insertEntireRow() {
		Row row = currentSheet.getRow(rowIndex);
		int lCol = row.getFirstCellNum();
		int rCol  = row.getLastCellNum();
		Ranges.range(currentSheet, rowIndex, lCol, rowIndex, rCol).insert(Range.SHIFT_DOWN, Range.FORMAT_LEFTABOVE);
	}
	
	/**
	 * Insert entire column
	 */
	public void onClick$insertEntireColumn() {
		int tRow = currentSheet.getFirstRowNum();
		int bRow = currentSheet.getPhysicalNumberOfRows();
		Ranges.range(currentSheet, tRow, colIndex, bRow, colIndex).insert(Range.SHIFT_RIGHT, Range.FORMAT_RIGHTBELOW);
	}
}