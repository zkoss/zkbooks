/* ExportComposer.java

	Purpose:
		
	Description:
		
	History:
		November 05, 5:53:16 PM     2010, Created by Ashish Dasnurkar

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zssessentials.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.poi.ss.util.AreaReference;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.impl.PdfExporter;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * @author ashish
 * 
 */
public class ExportComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	Button exportBtn;

	public void onClick$exportBtn(Event evt) throws IOException {
		Book wb = spreadsheet.getBook();
		Exporter c = new PdfExporter();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		c.export(wb, baos);
		Filedownload.save(baos.toByteArray(), "application/pdf",
				wb.getBookName());
	}
}
