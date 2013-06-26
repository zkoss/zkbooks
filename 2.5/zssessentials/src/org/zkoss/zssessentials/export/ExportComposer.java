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

import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Button;

/**
 * @author ashish
 * 
 */
public class ExportComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	Button exportBtn;

	public void onClick$exportBtn(Event evt) throws IOException {
		Book wb = spreadsheet.getBook();
		Exporter c = Exporters.getExporter("pdf");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		c.export(wb, baos);
		Filedownload.save(baos.toByteArray(), "application/pdf",
				wb.getBookName());
	}
}
