/* MyBean.java
 * 
 */

package org.zkoss.zssessentials.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.poi.ss.usermodel.RichTextString;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkjsf.ui.ZKJSFSpreadsheet;
import org.zkoss.zkplus.embed.Bridge;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.FormatText;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.impl.pdf.PdfExporter;

@ManagedBean
@SessionScoped
public class MyBean extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private String cellReference;
	private String cvalue;
	private ZKJSFSpreadsheet myBeanSpreadsheet;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	public void showCellValue(ActionEvent e) {
		Range range = Ranges.range(myBeanSpreadsheet.getSpreadsheet()
				.getSelectedSheet(), cellReference);
		FormatText ft = range.getFormatText();
		if (ft != null && ft.isCellFormatResult()) {
			setCvalue(ft.getCellFormatResult().text);
		} else {
			final RichTextString rstr = range == null ? null : range
					.getRichEditText();
			setCvalue(rstr != null ? rstr.getString() : "");
		}
	}

	public void exportToPdf(ActionEvent e) {

		Exporter c = Exporters.getExporter("pdf");
		((PdfExporter) c).enableGridLines(true);
		((PdfExporter) c).enableHeadings(true);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline=filename=file.pdf");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			c.export(myBeanSpreadsheet.getSpreadsheet().getBook(), baos);
			baos.writeTo(response.getOutputStream());
			response.getOutputStream().flush();
			context.responseComplete();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void resetSpreadsheet(ValueChangeEvent e) {
		UIInput input = (UIInput) e.getSource();
		System.out.println(input.getValue().toString());
		UISelectOne listbox = (UISelectOne) input.findComponent("listbox");
		if (listbox != null) {
			UISelectItems items = (UISelectItems) listbox.getChildren().get(0);
			System.out.println(items.getValue());
			myBeanSpreadsheet.getSpreadsheet().setSrc(
					items.getValue().toString());
		}
	}

	public void setCellText(ActionEvent e) throws IOException {

		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		PartialResponseWriter responseWriter = FacesContext
				.getCurrentInstance().getPartialViewContext()
				.getPartialResponseWriter();
		ServletContext svlctx = (ServletContext) ec.getContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		HttpServletResponse response = (HttpServletResponse) ec.getResponse();

		Bridge bridge = Bridge.start(svlctx, request, response,
				myBeanSpreadsheet.getSpreadsheet().getDesktop());
		try {

			final Range range = Ranges.range(myBeanSpreadsheet.getSpreadsheet()
					.getSelectedSheet(), cellReference);
			range.setEditText(cvalue);

			responseWriter.startDocument();
			responseWriter.startEval();
			responseWriter.write(bridge.getResult());
			responseWriter.endEval();
			responseWriter.endDocument();
			responseWriter.flush();
			responseWriter.close();

		} finally {
			bridge.close();
		}
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public ZKJSFSpreadsheet getMyBeanSpreadsheet() {
		return myBeanSpreadsheet;
	}

	public void setMyBeanSpreadsheet(ZKJSFSpreadsheet spreadsheet) {
		this.myBeanSpreadsheet = spreadsheet;
	}

	public String getCellReference() {
		return cellReference;
	}

	public void setCellReference(String cellReference) {
		this.cellReference = cellReference;
	}
}
