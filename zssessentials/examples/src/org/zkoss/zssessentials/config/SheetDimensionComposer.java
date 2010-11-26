/* SheetDimensionComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 5:42:27 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zssessentials.config;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Combobox;

/**
 * SheetDimensionComposer demonstrate how to change different sheet
 * @author Sam
 *
 */
public class SheetDimensionComposer extends GenericForwardComposer{
	
	Combobox sheets;
	Spreadsheet spreadsheet;
	//override
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		List<String> sheetNames = new ArrayList<String>();
		int sheetSize = spreadsheet.getBook().getNumberOfSheets();
		for (int i = 0; i < sheetSize; i++){
			sheetNames.add(spreadsheet.getSheet(i).getSheetName());
		}
		
		BindingListModelList model = new BindingListModelList(sheetNames, true);
		sheets.setModel(model);
	}
	
	/**
	 * Sets selected sheet
	 * @param event
	 */
	public void onSelect$sheets(Event event) {
		spreadsheet.setSelectedSheet(sheets.getText());
	}
}