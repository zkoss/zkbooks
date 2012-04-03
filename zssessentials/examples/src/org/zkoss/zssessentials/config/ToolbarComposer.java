/* ToolbarComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 2, 2012 11:36:22 AM , Created by sam
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
public class ToolbarComposer extends GenericForwardComposer {
	
	Button toggleToolbar;
	
	Spreadsheet spreadsheet;

	public void onClick$toggleToolbar() {
		boolean isShowToolbar = spreadsheet.isShowToolbar();
		spreadsheet.setShowToolbar(!isShowToolbar);
	}
}
