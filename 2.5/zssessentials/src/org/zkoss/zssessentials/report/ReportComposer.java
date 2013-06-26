/* ReportComposer.java

	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 4:54:47 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.report;

import java.io.IOException;
import java.util.List;

import org.zkoss.zss.model.Worksheet;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;

/**
 * @author henrichen
 *
 */
public class ReportComposer extends GenericForwardComposer {
	private Spreadsheet report;
	private Book book;
	private Worksheet reportSheet;
	private int offset; //offset to next row
	private Range templateRange;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		book = report.getBook();
		reportSheet = book.getWorksheet("ReportSheet");
		final Worksheet templateSheet = book.getWorksheet("TemplateSheet");
		templateRange = Ranges.range(templateSheet, "template");
		offset = ((Number)Ranges.range(templateSheet, "offset").getValue()).intValue();
	}

	//Generate report per the database contents.
	public void generate() throws IOException {
		//Load data from database
		List<ComputerBean> data = DataProvider.query();
		
		final int row = 1;
		final int col = 1;
		
		//copy row templates
		copyTemplate(row, col, data.size());
		
		//fill data
		fillData(data);

		//Resize Spreadsheet 
		report.setMaxrows(offset * data.size());
	}
	
	//copy Row templates into ReportSheet
	private void copyTemplate(int row, int col, int size) {
		for(int j = 0; j < size; j++) {
			//Copy to new position
			templateRange.copy(Ranges.range(reportSheet, row + j * offset, col));
		}
	}
	
	//fill in data to the Report
	private void fillData(List<ComputerBean> data) {
		for(int j = 0, sz = data.size(); j < sz; j++) {
			final ComputerBean computerBean = data.get(j);
			
			//fill in data into cells
			Ranges.range(reportSheet, 1+j*offset, 1).setValue(Integer.valueOf(j+1)); //Sequence Number
			Ranges.range(reportSheet, 2+j*offset, 2).setValue(computerBean.getId());
			Ranges.range(reportSheet, 3+j*offset, 2).setValue(computerBean.getModel());
			Ranges.range(reportSheet, 4+j*offset, 2).setValue(computerBean.getWarrantyTime());
			Ranges.range(reportSheet, 5+j*offset, 2).setValue(Double.valueOf(computerBean.getSalvage()));
			Ranges.range(reportSheet, 2+j*offset, 4).setValue(computerBean.getProduct());
			Ranges.range(reportSheet, 3+j*offset, 4).setValue(computerBean.getSerialNumber());
			Ranges.range(reportSheet, 4+j*offset, 4).setValue(computerBean.getOs());
			Ranges.range(reportSheet, 2+j*offset, 6).setValue(computerBean.getBrand());
			Ranges.range(reportSheet, 3+j*offset, 6).setValue(computerBean.getDate());
			Ranges.range(reportSheet, 4+j*offset, 6).setValue(Double.valueOf(computerBean.getCost()));
		}
	}
}
