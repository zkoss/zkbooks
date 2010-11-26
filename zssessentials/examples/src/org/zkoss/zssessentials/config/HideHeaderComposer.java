/* HideHeaderComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 2:20:10 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.event.HeaderMouseEvent;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

/**
 * HideHeaderComposer demonstrate how to hide or show on column or row
 * @author Sam
 *
 */
public class HideHeaderComposer extends GenericForwardComposer {
	
	private Spreadsheet spreadsheet;
	private Range currentRange;
	
	private Menupopup headerMenupopup;
	private Menuitem show;
	private Menuitem hide;

	//override
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		headerMenupopup.setWidgetListener("onOpen", "this.$f('" + spreadsheet.getId() + "').focus(false)");
	}
	
	/**
	 * Save current selection range
	 * @param event
	 */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
		currentRange = Ranges.range(event.getSheet(), event.getTop(), event.getLeft(), 
				event.getBottom(), event.getRight());
	}
	
	/**
	 * Open menu when right click on header
	 * @param event
	 */
	public void onHeaderRightClick$spreadsheet(HeaderMouseEvent event) {
		headerMenupopup.open(event.getPageX(), event.getPageY());
	}
	
	/**
	 * Show all header in selection range
	 */
	public void onClick$show() {
		if (currentRange != null) {
			currentRange.setHidden(false);
			currentRange = null;
		}
	}
	
	/**
	 * Hide header in selection range
	 */
	public void onClick$hide() {
		if (currentRange != null) {
			currentRange.setHidden(true);
			currentRange = null;
		}
	}
}