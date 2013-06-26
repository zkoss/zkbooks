/* ZSSComposer.java

	Purpose:
		
	Description:
		
	History:
		Dec 26, 2010 2:44:37 PM, Created by Ashish Dasnurkar

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.jsp;

import java.io.ByteArrayOutputStream;

import org.zkoss.poi.ss.usermodel.RichTextString;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.FormatText;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;

/**
 * @author ashish
 *
 */
public class ZSSComposer extends GenericForwardComposer {
	
	Spreadsheet myzss;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	public void onExport$myzss(Event evt) {
		Exporter exp = Exporters.getExporter("excel");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Book wb = myzss.getBook();
		exp.export(wb, baos);
		Filedownload.save(baos.toByteArray(), "application/file",
				wb.getBookName());
	}
	
	public void onShowCellText$myzss(ForwardEvent evt) {
		Object[] data = (Object[]) evt.getOrigin().getData();
		String cellRef = (String) data[0];
		Range range = Ranges.range(myzss.getSelectedSheet(), cellRef);
		FormatText ft = range.getFormatText();
		String eval = null;
		if (ft != null && ft.isCellFormatResult()) {
			eval = "jq('#cellTextTxt').val(\"" + ft.getCellFormatResult().text + "\");";
		} else {
			final RichTextString rstr = range == null ? null : range
					.getRichEditText();
			eval = "jq('#cellTextTxt').val(\"" + (rstr != null ? rstr.getString() : "") + "\");";
		}
		Clients.evalJavaScript(eval);
	}
	
	public void onSetCellText$myzss(ForwardEvent evt) {
		Object[] data = (Object[]) evt.getOrigin().getData();
		String cellRef = (String) data[0];
		String cellText = (String) data[1];
		final Range range = Ranges.range(myzss.getSelectedSheet(), cellRef);
		range.setEditText(cellText);
	}
}
