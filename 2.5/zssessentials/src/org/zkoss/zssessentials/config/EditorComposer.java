/* EditorComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 9:00:15 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

/**
 * EditorComposer demonstrate how to let user input cell's value or formula
 * @author Sam
 *
 */
public class EditorComposer extends GenericForwardComposer {

	private Combobox focusCombobox;
	private Comboitem position;
	
	private Textbox formulaEditor;
	
	private Cell currentCell;
	private Range currentRange;
	private Spreadsheet spreadsheet;
	
	/**
	 * Save the current cell and display cell's value in textbox
	 * @param event
	 */
	public void onCellFocused$spreadsheet(CellEvent event) {
		int row = event.getRow();
		int col = event.getColumn();
		focusCombobox.setText(spreadsheet.getRowtitle(row) + spreadsheet.getColumntitle(col));

		Worksheet sheet = spreadsheet.getSelectedSheet();
		currentCell = Utils.getCell(sheet, row, col);
		currentRange = Ranges.range(sheet, row, col);
		formulaEditor.setText(currentRange.getEditText());
	}
	
	/**
	 * Sets cell's value
	 */
	public void onOK$formulaEditor() {
		currentRange.setEditText(formulaEditor.getText());
		spreadsheet.focusTo(currentCell.getRowIndex() + 1, currentCell.getColumnIndex());
	}
	
	/**
	 * Sets cell's value
	 */
	public void onChanging$formulaEditor(InputEvent event) {
		if (currentCell.getCellType() != Cell.CELL_TYPE_FORMULA)
			currentRange.setEditText(event.getValue());
	}
	
	/**
	 * Sets cell's value
	 */
	public void onChange$formulaEditor() {
		currentRange.setEditText(formulaEditor.getText());
	}
}
