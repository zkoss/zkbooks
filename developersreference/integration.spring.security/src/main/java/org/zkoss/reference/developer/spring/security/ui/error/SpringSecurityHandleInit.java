/**
 * 
 */
package org.zkoss.reference.developer.spring.security.ui.error;

import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.util.GenericInitiator;

/**
 * @author Ian YT Tsai (zanyking)
 *
 */
public class SpringSecurityHandleInit extends GenericInitiator {

	private static final String VAR_SPRING_SECURITY_ERROR = "VAR_SPRING_SECURITY_ERROR";
	private static final String VAR_DESKTOP_REQ_URI = "VAR_DESKTOP_REQ_URI";
	
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		System.out.println(">>>> Security Process: In Proccessing...");
		// when this initiator has been executed that means user encountered AA problem.
		
		Execution exec = Executions.getCurrent();
		
		exec.getAttribute("javax.servlet.error.message");
		Exception ex = (Exception) exec.getAttribute("javax.servlet.error.exception");
		Session sess = Sessions.getCurrent();
		
		
		if(exec.isAsyncUpdate(null) ){
			//STEP 1: convert Ajax Request to Page Request(Error Handling Page Request)
			System.out.println(">>>> Security Process: STEP 1");
			if(ex instanceof AccessDeniedException){
				sess.setAttribute(VAR_DESKTOP_REQ_URI, getOriginalDesktopUri());
				sess.setAttribute(VAR_SPRING_SECURITY_ERROR, ex);
				
				Executions.sendRedirect(toSecurityProcessUrl((AccessDeniedException) ex));// GOTO STEP 2 by redirection.
			}else{
				throw new IllegalArgumentException(
					"How come an unexpected Exception type will be mapped to this handler? " +
					"please correct it in your zk.xml");
			}
		}else{
			//This initiator should only accept AccessDeniedException.
			AccessDeniedException adEx = 
				(AccessDeniedException) sess.getAttribute(VAR_SPRING_SECURITY_ERROR);
			String dtPath = (String) sess.getAttribute(VAR_DESKTOP_REQ_URI);
			if(adEx !=null){
				//STEP 2: throw Error in Error Handling Page Request.
				System.out.println(">>>> Security Process: STEP 2");
				sess.removeAttribute(VAR_SPRING_SECURITY_ERROR);
				throw adEx;// we suppose Spring Security Error Filter Chain will handle this properly.
				
			}else if(dtPath!=null){
				System.out.println(">>>> Security Process: STEP 3");
				//STEP 3: if Spring Security Authentication was triggered at STEP 2, 
				//then we need STEP 3 to redirect back to original URI the very first desktop belongs to.
				sess.removeAttribute(VAR_DESKTOP_REQ_URI);
				exec.sendRedirect(dtPath);
				
			}else{// User Direct Access, meaningless
				System.out.println(">>>> Security Process: ELSE");
				exec.sendRedirect("/");
			}
		}
	}

	private static String getOriginalDesktopUri(){
		// developer may implement this part to adapt to PushState or any other Page based Framework, that might have interference to request URI.
		String str = Executions.getCurrent().getDesktop().getRequestPath();
		String qs = Executions.getCurrent().getDesktop().getQueryString();
		System.out.println(">>>security Process: Desktop path= "+str);
		return str+"?"+qs;
	}
	
	/**
	 * 
	 * @param ex
	 * @param httpsPort
	 * @param request 
	 * @return
	 */
	private static String toSecurityProcessUrl(AccessDeniedException ex){
		Configuration sConfiguration = WebApps.getCurrent().getConfiguration();
		String ePage = sConfiguration.getErrorPage("ajax", ex);// need to append query part back...
		return ePage;
	}
	
}
