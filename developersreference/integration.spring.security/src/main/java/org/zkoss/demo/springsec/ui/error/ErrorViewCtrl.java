/**
 * 
 */
package org.zkoss.demo.springsec.ui.error;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class ErrorViewCtrl extends SelectorComposer<Component> {
	@Wire
	private Html traceHtml;
	@Wire
	private Label reqUriLbl;
	@Wire
	private Label httpCodeLbl;
	@Wire
	private Label errMesgLbl;
	@Wire
	private Label errTypeLbl;

	@Wire 
	private Label servletNameLbl;
	/**
	 *
	 *
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Execution exec = Executions.getCurrent();
		
		
		
		
		Exception e = (Exception) exec.getAttribute("javax.servlet.error.exception");
		System.out.println("javax.servlet.error.exception: "+e);
		if(e!=null){
			StringBuffer sBuffer = new StringBuffer("<pre>");
			for(StackTraceElement element : e.getStackTrace()){;
				sBuffer.append(element);
			}
			sBuffer.append("</pre>");
			traceHtml.setContent(sBuffer.toString());
		}
		
		String reqUri = (String) exec.getAttribute("javax.servlet.error.request_uri");
		System.out.println("javax.servlet.error.request_uri: "+reqUri);
		reqUriLbl.setValue(reqUri);
		
		String errMesg = (String) exec.getAttribute("javax.servlet.error.message");
		System.out.println("javax.servlet.error.message: "+errMesg);
		errMesgLbl.setValue(errMesg);
		
		Class<?> errType = (Class<?>) exec.getAttribute("javax.servlet.error.exception_type");
		System.out.println("javax.servlet.error.exception_type: "+errType);
		errTypeLbl.setValue((errType==null)? "unknown" : ""+errType);
		
		int httpCode = (Integer) exec.getAttribute("javax.servlet.error.status_code");
		System.out.println("javax.servlet.error.status_code: "+httpCode);
		httpCodeLbl.setValue(""+httpCode);
		
		String servletName = (String) exec.getAttribute("javax.servlet.error.servlet_name");
		System.out.println("javax.servlet.error.servlet_name: "+servletName);
		servletNameLbl.setValue(servletName);
	}
	
	
}
