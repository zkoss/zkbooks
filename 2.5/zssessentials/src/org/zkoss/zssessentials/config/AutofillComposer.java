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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;

/**
 * AutofillComposer demonstrate how to auto fill cell value
 * @author Sam
 *
 */
public class AutofillComposer extends GenericForwardComposer {

	Spreadsheet spreadsheet;
	Cell currentCell;
	Rect selection;
	
	Intbox fillCells;
	Combobox fillOrientation;
	
	Button autoFill;
	Intbox autoFillCells;
	Combobox autofillType;

	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		fillOrientation.setSelectedIndex(0);
		autofillType.setSelectedIndex(0);
	}

	/**
	 * Cache current cell when focus on cell
	 * @param event
	 */
	public void onCellFocused$spreadsheet(CellEvent event) {
		currentCell = Utils.getCell(event.getSheet(), event.getRow(), event.getColumn());
	}
	
	/**
	 * Cache the current selection range
	 */
	public void onCellSelection$spreadsheet() {
		selection = spreadsheet.getSelection();
	}
	
	/**
	 * Fill cells from current cell
	 */
	public void onClick$fill() {
		String orientation = (String)fillOrientation.getSelectedItem().getValue();
		if ("down".equals(orientation)) {
			int leftCol = currentCell.getColumnIndex();
			int topRow = currentCell.getRowIndex();
			int rightCol = leftCol;
			int btmRow = topRow + fillCells.getValue();
			Ranges.range(spreadsheet.getSelectedSheet(), topRow, leftCol, btmRow, rightCol).fillDown();
		} else if ("right".equals(orientation)) {
			int leftCol = currentCell.getColumnIndex();
			int topRow = currentCell.getRowIndex();
			int rightCol = leftCol + fillCells.getValue();
			int btmRow = topRow;
			Ranges.range(spreadsheet.getSelectedSheet(), topRow, leftCol, btmRow, rightCol).fillRight();
		} else if ("left".equals(orientation)) {
			int rightCol = currentCell.getColumnIndex();
			int leftCol = rightCol - fillCells.getValue();
			int topRow = currentCell.getRowIndex();
			int btmRow = topRow;
			Ranges.range(spreadsheet.getSelectedSheet(), topRow, leftCol, btmRow, rightCol).fillLeft();
		} else if ("up".equals(orientation)) {
			int btmRow = currentCell.getRowIndex();
			int topRow = btmRow - fillCells.getValue();
			int leftCol = currentCell.getColumnIndex();
			int rightCol = leftCol;
			Ranges.range(spreadsheet.getSelectedSheet(), topRow, leftCol, btmRow, rightCol).fillUp();
		}
	}

	/**
	 * AutoFill cells
	 */
	public void onClick$autofill() {
		if (selection == null) {
			alert("Selection can not be empty");
			return;
		}
		
		final Worksheet worksheet = spreadsheet.getSelectedSheet();
		final int topRow = selection.getTop();
		final int leftCol = selection.getLeft();
		final int btmRow = selection.getBottom();
		final int rightCol = selection.getRight();
		
		final int fillCells = autoFillCells.getValue();
		
		//fill down
		Range autofillRange = 
			Ranges.range(worksheet, topRow, leftCol, btmRow + fillCells, leftCol);

		int type = Range.FILL_DEFAULT;
		String fillType = (String) autofillType.getSelectedItem().getValue();
		if ("copy".equals(fillType)) {
			type = Range.FILL_COPY;
		}
		
		final Range srcRange = 
			Ranges.range(worksheet, topRow, leftCol, btmRow, rightCol);
		
		srcRange.autoFill(autofillRange, type);
	}
}
