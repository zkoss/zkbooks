/* CopyPasteComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Oct 28, 2010 2:44:53 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * CopyPasteComposer demonstrate copy, copy and paste function using menu and control keys 
 * @author Sam
 */
public class CopyPasteComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	Menupopup cellMenu;
	Menuitem cutMenu;
	Menuitem copyMenu;
	Menuitem pasteMenu;
	Rect selectedRect;
	
	public void onCellSelection$spreadsheet() {
		selectedRect = spreadsheet.getSelection();
	}

	/* Save copy source  */
	private final static String KEY_SOURCE_RANGE = "KEY_SOURCE_RANGE";
	private final static String KEY_SOURCE_RECT = "KEY_SOURCE_RECT";
	/* Sets whether is cut or copy action*/
	private final static String KEY_IS_CUT = "KEY_IS_CUT";

	/**
	 * Open menu when user right click on cell 
	 * @param event
	 */
	public void onCellRightClick$spreadsheet(CellMouseEvent event) {
		cellMenu.open(event.getClientx(), event.getClienty());
	}

	/**
	 * Sets cut selection
	 */
	public void onClick$cutMenu() {
		onCut();
	}
	
	/**
	 * Sets copy selection 
	 */
	public void onClick$copyMenu() {
		onCopy();
	}

	/**
	 * Execute paste
	 */
	public void onClick$pasteMenu() {
		onPaste();
	}
	
	/**
	 * Execute cut copy or paste using key control
	 * @param evt
	 */
	public void onCtrlKey$spreadsheet(KeyEvent evt) {
		if (evt.isCtrlKey()) {
			switch (evt.getKeyCode()) {
			case 'C':
				onCopy();
				break;
			case 'V':
				onPaste();
				break;
			case 'X':
				onCut();
				break;
			}
			
		}
	}

	/**
	 * Sets copy source from spreadsheet
	 */
	private void onCopy() {
		setSource();
		
		/* Highlight copy source */
		spreadsheet.setHighlight(spreadsheet.getSelection());
	}
	
	/**
	 * Sets cut source from spreadsheet
	 */
	private void onCut() {
		spreadsheet.setAttribute(KEY_IS_CUT, Boolean.valueOf(true));
		setSource();
		
		/* Highlight cut source */
		spreadsheet.setHighlight(spreadsheet.getSelection());
	}
	
	/**
	 * Sets source range of selection 
	 */
	private void setSource() {
		Rect selectedRect = spreadsheet.getSelection();
		Range range = Ranges.range(spreadsheet.getSelectedSheet(), 
				selectedRect.getTop(), 
				selectedRect.getLeft(), 
				selectedRect.getBottom(),
				selectedRect.getRight());
		spreadsheet.setAttribute(KEY_SOURCE_RANGE, range);
		spreadsheet.setAttribute(KEY_SOURCE_RECT, selectedRect);
	}
	
	/**
	 * Paste selection to spreadsheet
	 */
	private void onPaste() {
		Range srcRange = (Range)spreadsheet.getAttribute(KEY_SOURCE_RANGE);
		Rect srcRect = (Rect)spreadsheet.getAttribute(KEY_SOURCE_RECT);
		
		if (srcRange != null && srcRect != null) {

			int columnCount = srcRect.getRight() - srcRect.getLeft();
			int rowCount = srcRect.getBottom() - srcRect.getTop();
			
			Rect dstRect = spreadsheet.getSelection();
			int rowIndex = dstRect.getTop();
			int columnIndex = dstRect.getLeft();
			
			Range dst = Ranges.range(spreadsheet.getSelectedSheet(),
					rowIndex, 
					columnIndex, 
					rowIndex + rowCount, 
					columnIndex + columnCount);
			srcRange.copy(dst);
			
			clearHighlightIfNeeded();
			clearCutRangeIfNeeded();
		}
	}

	/**
	 * Clear cut range from source
	 */
	private void clearCutRangeIfNeeded() {
		if (!isCut())
			return;
		
		Range srcRange = (Range)spreadsheet.getAttribute(KEY_SOURCE_RANGE);
		srcRange.clearContents();
		/* clear cell style*/
		CellStyle defaultCellStyle = spreadsheet.getBook().getCellStyleAt((short)0);
		srcRange.setStyle(defaultCellStyle);
	}

	/**
	 * Clear highlight if onCut
	 */
	private void clearHighlightIfNeeded() {
		if (isCut())
			spreadsheet.setHighlight(null);
	}
	/**
	 * Returns whether to cut source range or not
	 * <p> Default: false
	 * @return
	 */
	private boolean isCut() {
		Boolean isCut = (Boolean)spreadsheet.getAttribute(KEY_IS_CUT);
		return isCut != null && isCut;
	}
}