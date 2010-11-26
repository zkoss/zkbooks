/* HeaderTitleComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 10:57:14 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import java.util.HashMap;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.HeaderEvent;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

/**
 * HeaderTitleComposer demonstrate how to change row or column header title
 * @author Sam
 *
 */
public class HeaderTitleComposer extends GenericForwardComposer {
	
	private Popup inputTitlePopup;
	private Textbox titleEditor;
	
	private boolean isColumnHeader;
	private int currentIndex;
	private Spreadsheet spreadsheet;
	
	/**
	 * Open editor when user double click on header
	 * @param event
	 */
	public void onHeaderDoubleClick$spreadsheet(HeaderMouseEvent event) {
		String currentTitle = null;
		int headerType = event.getType();
		if (headerType == HeaderEvent.TOP_HEADER) {
			isColumnHeader = true;
			currentTitle = spreadsheet.getColumntitle(currentIndex);
		} else {
			isColumnHeader = false;
			currentTitle =  spreadsheet.getRowtitle(currentIndex);
		}
		
		inputTitlePopup.open(event.getPageX(), event.getPageY());
		titleEditor.setText(currentTitle);
		titleEditor.focus();
		
		currentIndex = event.getIndex();
	}
	
	/**
	 * Sets the new title
	 */
	public void onOK$titleEditor() {
		HashMap<Integer, String> titles = new HashMap<Integer, String>();
		titles.put(Integer.valueOf(currentIndex), titleEditor.getText());
		
		if (isColumnHeader) {
			spreadsheet.setColumntitles(titles);
		} else {
			spreadsheet.setRowtitles(titles);
		}
		inputTitlePopup.close();
	}
	
	/**
	 * Cancel editing title
	 */
	public void onCancel$titleEditor() {
		inputTitlePopup.close();
	}
}