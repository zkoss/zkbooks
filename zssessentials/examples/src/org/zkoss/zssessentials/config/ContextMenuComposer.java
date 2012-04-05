/* ContextMenuComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 3, 2012 7:06:20 PM , Created by sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Button;

/**
 * @author sam
 *
 */
public class ContextMenuComposer extends GenericForwardComposer {

	Button toggleContextMenu;
	Spreadsheet spreadsheet;
	
	public void onClick$toggleContextMenu() {
		boolean toggle = !spreadsheet.isShowContextMenu();
		spreadsheet.setShowContextMenu(toggle);
	}
}
