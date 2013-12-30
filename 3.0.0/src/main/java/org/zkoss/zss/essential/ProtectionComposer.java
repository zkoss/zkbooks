/* BookSheetComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 5:42:27 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zss.essential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.api.model.EditableCellStyle;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.event.SheetSelectEvent;
import org.zkoss.zul.Label;

/**
 * Demonstrate how to change different sheet
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class ProtectionComposer extends SelectorComposer<Component>{
	
	@Wire
	private Spreadsheet ss;
	@Wire
	private Label status;
	@Wire
	private Label lockStatus;
	

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		updateSheetProtectionStatus(ss.getSelectedSheet());
	}
	
	@Listen("onClick = #toggleProtection")
	public void toggleProtection(){
		Sheet selectedSheet = ss.getSelectedSheet();
		if (selectedSheet.isProtected()){
			Ranges.range(selectedSheet).protectSheet(null);
		}else{
			Ranges.range(selectedSheet).protectSheet("s");
		}
		updateSheetProtectionStatus(selectedSheet);
	}
	
	@Listen("onSheetSelect = #ss")
	public void selectSheet(SheetSelectEvent event) {
		updateSheetProtectionStatus(event.getSheet());
	}
	
	private void updateSheetProtectionStatus(Sheet sheet){
		status.setValue(Boolean.toString(sheet.isProtected()));
	}
	
	@Listen("onClick = #toggleLock")
	public void toggleLock(){
		Range selection = Ranges.range(ss.getSelectedSheet(), ss.getSelection());
		CellStyle oldStyle = selection.getCellStyle();
		EditableCellStyle newStyle = selection.getCellStyleHelper().createCellStyle(oldStyle);
		newStyle.setLocked(!oldStyle.isLocked());
		selection.setCellStyle(newStyle);
		updateCellLockedStatus(newStyle.isLocked());
	}
	
	@Listen("onCellSelection = #ss")
	public void selectCells(CellSelectionEvent event) {
		CellStyle style = Ranges.range(ss.getSelectedSheet(), ss.getSelection()).getCellStyle();
		updateCellLockedStatus(style.isLocked());
	}
	
	private void updateCellLockedStatus(Boolean status){
		lockStatus.setValue(status.toString());
	}
}