/* ValidationComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jan 10, 2012 6:38:19 PM , Created by sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.DataValidation;
import org.zkoss.poi.ss.usermodel.DataValidationConstraint;
import org.zkoss.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;

/**
 * @author sam
 *
 */
public class ValidationComposer extends GenericForwardComposer {
	
	private final static int DATA_VALIDATION_ROW = 1;
	private final static int DATA_VALIDATION_COL = 2;

	private Combobox deparmentCombobox;
	
	private Spreadsheet spreadsheet;
	
	private Worksheet sheet;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		sheet = spreadsheet.getSelectedSheet();
		initCombobox();
	}
	
	private DataValidation getDataValidation() {
		return sheet.getDataValidation(DATA_VALIDATION_ROW, DATA_VALIDATION_COL);
	}
	
	private void initCombobox() {
		DataValidation dataValidation = getDataValidation();
		DataValidationConstraint constraint = dataValidation.getValidationConstraint();
		
		switch (constraint.getValidationType()) {
		case ValidationType.LIST:
			ListModelList model = new ListModelList(constraint.getExplicitListValues());
			deparmentCombobox.setModel(model);
			break;
		}
		
		//Add illegal item
		((ListModelList) deparmentCombobox.getModel()).add("Illegal cell value");
	}
	
	public void onSelect$deparmentCombobox() {
		String cellValue = deparmentCombobox.getSelectedItem().getLabel();
		boolean validInput = spreadsheet.validate(sheet, DATA_VALIDATION_ROW, DATA_VALIDATION_COL, cellValue, null);
		if (validInput) {
			Ranges.range(sheet, DATA_VALIDATION_ROW, DATA_VALIDATION_COL).setEditText(cellValue);
		}
	}
}
