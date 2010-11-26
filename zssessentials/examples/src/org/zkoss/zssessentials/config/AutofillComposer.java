/* AutofillComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 11:56:01 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;

/**
 * AutofillComposer demonstrate how to auto fill cell value
 * @author Sam
 *
 */
public class AutofillComposer extends GenericForwardComposer {

	Spreadsheet spreadsheet;
	Cell currentCell;
	
	Intbox rows;
	Intbox columns;
	Button fillDown;
	Button fillRight;
	Button fillLeft;
	Button fillUp;
	Button autoFill;

	/**
	 * Cache current cell when focus on cell
	 * @param event
	 */
	public void onCellFocused$spreadsheet(CellEvent event) {
		currentCell = Utils.getCell(event.getSheet(), event.getRow(), event.getColumn());
	}
	
	/**
	 * Fill down cell's value from current cell
	 */
	public void onClick$fillDown() {
		Integer rowNum = rows.getValue();
		if (currentCell == null || rowNum == null)
			return;
		
		Sheet sheet = spreadsheet.getSelectedSheet();
		int top = currentCell.getRowIndex();
		int left = currentCell.getColumnIndex();
		Range downRange = Ranges.range(sheet, top, left, top + rowNum, left);
		downRange.fillDown();
	}
	
	/**
	 * Fill right cell's value form current cell 
	 */
	public void onClick$fillRight() {
		Integer colNum = columns.getValue();
		if (currentCell == null || colNum == null)
			return;
		
		Sheet sheet = spreadsheet.getSelectedSheet();
		int top = currentCell.getRowIndex();
		int left = currentCell.getColumnIndex();
		Range rightRange = Ranges.range(sheet, top, left, top, left + colNum);
		rightRange.fillRight();
	}
	
	/**
	 * Fill left cell's value from current cell
	 */
	public void onClick$fillLeft() {
		Integer colNum = columns.getValue();
		if (currentCell == null || colNum == null)
			return;
		
		Sheet sheet = spreadsheet.getSelectedSheet();
		int top = currentCell.getRowIndex();
		int left = currentCell.getColumnIndex();
		Range leftRange = Ranges.range(sheet, top, left - colNum, top, left);
		leftRange.fillLeft();
	}
	
	/**
	 * Fill up cell's value from current cell
	 */
	public void onClick$fillUp() {
		Integer rowNum = rows.getValue();
		if (currentCell == null || rowNum == null)
			return;

		Sheet sheet = spreadsheet.getSelectedSheet();
		int top = currentCell.getRowIndex();
		int left = currentCell.getColumnIndex();
		Range upRange = Ranges.range(sheet, top - rowNum, left, top, left);
		upRange.fillUp();
	}
	
	/**
	 * Autofill cells value to right and bottom side 
	 */
	public void onClick$autoFill() {
		 Integer rowNum = rows.getValue();
		 Integer colNum = columns.getValue();
		 if (currentCell == null || rowNum == null || colNum == null)
		    return;
		         
		 Sheet sheet = spreadsheet.getSelectedSheet();
		 int top = currentCell.getRowIndex();
		 int left = currentCell.getColumnIndex();
		 Range range = Ranges.range(sheet, top, left);
		 Range rightRange = Ranges.range(sheet, top, left, top , left + colNum);
		 range.autoFill(rightRange, Range.FILL_COPY);
		 
		 Range downRange = Ranges.range(sheet, top, left, top + rowNum, left);
		 range.autoFill(downRange, Range.FILL_COPY);
	}
}
