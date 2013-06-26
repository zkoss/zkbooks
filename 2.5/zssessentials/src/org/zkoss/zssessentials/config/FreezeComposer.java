/* FreezeComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 4:44:08 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * FreezeComposer demonstrate how to freeze spreadsheet row or column
 * @author Sam
 *
 */
public class FreezeComposer extends GenericForwardComposer {
	
	private Spreadsheet spreadsheet;
	private Menupopup cellMenu;
	private Menuitem unfreezeCols;
	private Menuitem unfreezeRows;
	
	/**
	 * Open menu when right click on cell
	 * @param event
	 */
	public void onCellRightClick$spreadsheet(CellMouseEvent event) {
		cellMenu.open(event.getPageX(), event.getPageY());
	}
	
	/**
	 * Sets row freeze 
	 * @param event
	 */
	public void onFreezeRow(ForwardEvent event) {
		int row = Integer.parseInt((String)event.getData());
		spreadsheet.setRowfreeze(row);
	}
	
	/**
	 * Sets column freeze
	 * @param event
	 */
	public void onFreezeCol(ForwardEvent event) {
		int col = Integer.parseInt((String)event.getData());
		spreadsheet.setColumnfreeze(col);
	}
	
	/**
	 * Unfreeze all rows
	 */
	public void onClick$unfreezeRows() {
		spreadsheet.setRowfreeze(-1);
	}
	
	
	/**
	 * Unfreeze all columns
	 */
	public void onClick$unfreezeCols() {
		spreadsheet.setColumnfreeze(-1);
	}
}
