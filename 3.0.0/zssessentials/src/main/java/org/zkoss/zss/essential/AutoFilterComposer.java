package org.zkoss.zss.essential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.SheetOperationUtil;
import org.zkoss.zss.api.Range.AutoFilterOperation;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;

/**
 * Demonstrate auto filter API usage
 * 
 * @author Hawk
 * 
 */
@SuppressWarnings("serial")
public class AutoFilterComposer extends SelectorComposer<Component> {

	@Wire
	private Spreadsheet ss;

	@Wire
	private Combobox typeBox;
	
	@Listen("onClick = button[label='Toogle Filter']")
	public void toggle() {
		Range selection = Ranges.range(ss.getSelectedSheet(), ss.getSelection());
		Range filteringRange = selection.findAutoFilterRange();
		if(!selection.isAutoFilterEnabled() &&  filteringRange == null) { 
			Messagebox.show("nothing to filter");
			return;
		}
		
		selection.enableAutoFilter(!selection.isAutoFilterEnabled());
		
//		SheetOperationUtil.toggleAutoFilter(selection);
	}
	
	@Listen("onClick = button[label='Clear Filter']")
	public void clear() {
		Range sheetRange = Ranges.range(ss.getSelectedSheet());
		sheetRange.resetAutoFilter();
		
//		SheetOperationUtil.resetAutoFilter(selection);
	}
	
	@Listen("onClick = button[label='Reapply Filter']")
	public void reapply() {
		Range sheetRange = Ranges.range(ss.getSelectedSheet());
		sheetRange.applyAutoFilter();
		
//		SheetOperationUtil.applyAutoFilter(selection);
	}
	
	@Listen("onClick = button[label='Apply']")
	public void apply() {
		Range sheetRange = Ranges.range(ss.getSelectedSheet());
		if (sheetRange.isAutoFilterEnabled()){
			String[] criteria = {typeBox.getValue()};
			sheetRange.enableAutoFilter(1, AutoFilterOperation.VALUES, criteria, null,
					true);
		}
	}
}
