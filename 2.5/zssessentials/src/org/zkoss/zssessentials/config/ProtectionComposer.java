/* ProtectionComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		May 23, 2011 10:34:51 AM , Created by sam
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

/**
 * @author sam
 *
 */
public class ProtectionComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	int topRow;
	int bottomRow;
	int leftCol;
	int rightCol;
	
	Button toggleSheetProtection;
	Button toggleLockCells;
	Label message;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setProtectionMessage();
	}

	void setProtectionMessage() {
		message.setValue(spreadsheet.getSelectedSheet().getProtect() ? "Protected sheet" : "Unprotected sheet");
	}
	
	/**
	 * Save current spreadsheet selection range
	 */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
		topRow = event.getTop();
		bottomRow = event.getBottom();
		leftCol = event.getLeft();
		rightCol = event.getRight();
	}
	
	/**
	 * Toggle sheet protection
	 * @param event
	 */
	public void onClick$toggleSheetProtection(Event event) {
		final Worksheet sheet = spreadsheet.getSelectedSheet();
		boolean sheetProtection = !sheet.getProtect();
		Ranges.range(sheet).protectSheet(sheetProtection ? "password" : null);
		setProtectionMessage();
	}
	
	/**
	 * Toggle locked cells
	 * @param event
	 */
	public void onClick$toggleLockCells(Event event) {
		final Worksheet sheet = spreadsheet.getSelectedSheet();
		boolean lock = !Utils.getOrCreateCell(sheet, topRow, leftCol).getCellStyle().getLocked();
		for (int r = topRow; r <= bottomRow; r++) {
			for (int c = leftCol; c <= rightCol; c++) {
				Cell cell = Utils.getOrCreateCell(sheet, r, c);
				CellStyle cellStyle = cell.getCellStyle();
				if (cellStyle.getLocked() != lock) {
					CellStyle newCellStyle = cloneStyle(cellStyle, sheet.getBook());
					newCellStyle.setLocked(lock);
					Ranges.range(sheet, r, c).setStyle(newCellStyle);
				}
			}
		}
	}
	
	/**
	 * Clone current style
	 * @param srcStyle
	 * @param book
	 * @return cellstyle
	 */
	private CellStyle cloneStyle(CellStyle srcStyle, Book book) {
		CellStyle newStyle =  book.createCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		return newStyle;
	}
}
