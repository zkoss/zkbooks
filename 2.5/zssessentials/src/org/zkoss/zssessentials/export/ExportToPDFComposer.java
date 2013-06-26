/* ExportToPDFComposer.java

	Purpose:
		
	Description:
		
	History:
		November 05, 5:53:16 PM     2010, Created by Ashish Dasnurkar

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zssessentials.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.zkoss.poi.ss.util.AreaReference;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 * @author ashish
 * 
 */
public class ExportToPDFComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	Button exportBtn;
	Button exportSheetBtn;
	Listbox sheets;
	Button exportCurrentSelectionBtn;
	Button showWorksheetBtn;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Book book = spreadsheet.getBook();
		ListModelList sheetNames = new ListModelList();
		int sheetCount = book.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			Worksheet sheet = (Worksheet) book.getSheetAt(i);
			sheetNames.add(sheet.getSheetName());
		}
		sheets.setModel(sheetNames);
	}

	public void onSelect$sheets(SelectEvent evt) {
		Listbox lb = (Listbox) evt.getTarget();
		spreadsheet.setSelectedSheet(lb.getSelectedItem().getLabel());
	}

	public void onClick$exportBtn(Event evt) throws IOException {
		Book wb = spreadsheet.getBook();
		Exporter c = Exporters.getExporter("pdf");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		c.export(wb, baos);
		Filedownload.save(baos.toByteArray(), "application/pdf",
				wb.getBookName());
	}

	public void onClick$exportSheetBtn(Event evt) throws IOException {
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Exporter c = Exporters.getExporter("pdf");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		c.export(sheet, baos);
		Filedownload.save(baos.toByteArray(), "application/pdf", spreadsheet
				.getBook().getBookName());
	}

	public void onClick$exportCurrentSelectionBtn(Event evt) throws IOException {
		Rect rect = spreadsheet.getSelection();
		String area = spreadsheet.getColumntitle(rect.getLeft())
				+ spreadsheet.getRowtitle(rect.getTop()) + ":"
				+ spreadsheet.getColumntitle(rect.getRight())
				+ spreadsheet.getRowtitle(rect.getBottom());
		Exporter c = Exporters.getExporter("pdf");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		c.exportSelection(spreadsheet.getSelectedSheet(), new AreaReference(
				area), baos);
		Filedownload.save(baos.toByteArray(), "application/pdf", spreadsheet
				.getBook().getBookName());
	}

	/** Required to register for Spreadsheet#getSelection() to work */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
	}
}
