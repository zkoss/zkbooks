/* SheetbarComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 5, 2012 10:43:38 AM , Created by sam
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
public class SheetbarComposer extends GenericForwardComposer {

	Button toggleSheetbar;
	Spreadsheet spreadsheet;
	
	public void onClick$toggleSheetbar() {
		boolean toggle = !spreadsheet.isShowSheetbar();
		spreadsheet.setShowSheetbar(toggle);
	}
}
