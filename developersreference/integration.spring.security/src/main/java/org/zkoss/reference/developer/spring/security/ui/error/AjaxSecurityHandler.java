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
 * @author Hawk
 *
 */
public class AjaxSecurityHandler extends GenericInitiator {

	private static final String SPRING_SECURITY_ERROR_KEY = "SPRING_SECURITY_ERROR_KEY";
	private static final String ORIGINAL_REQUEST_URL = "ORIGINAL_REQUEST_URL";
	
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		// when this initiator has been executed that means users encounter access denied problem.
		
		Execution exec = Executions.getCurrent();
		
		exec.getAttribute("javax.servlet.error.message");
		Exception ex = (Exception) exec.getAttribute("javax.servlet.error.exception");
		Session sess = Sessions.getCurrent();
		
		
		if(exec.isAsyncUpdate(null) ){
			//STEP 1: convert an AJAX Request to a page request(Error Handling Page Request)
			System.out.println(">>>> Security Process: STEP 1");
			if(ex instanceof AccessDeniedException){
				sess.setAttribute(ORIGINAL_REQUEST_URL, getOriginalDesktopUri());
				sess.setAttribute(SPRING_SECURITY_ERROR_KEY, ex);
				
				Executions.sendRedirect(getAccessDeniedHandlingPageUrl((AccessDeniedException) ex));// GOTO STEP 2 by redirection.
			}else{
				throw new IllegalArgumentException(
					"How come an unexpected Exception type will be mapped to this handler? " +
					"please correct it in your zk.xml");
			}
		}else{
			//This initiator should only accept AccessDeniedException.
			AccessDeniedException accessDeniedException = 
				(AccessDeniedException) sess.getAttribute(SPRING_SECURITY_ERROR_KEY);
			String originalRequestUrl = (String) sess.getAttribute(ORIGINAL_REQUEST_URL);
			if(accessDeniedException !=null){
				//STEP 2: throw Error in Error Handling Page Request.
				System.out.println(">>>> Security Process: STEP 2");
				sess.removeAttribute(SPRING_SECURITY_ERROR_KEY);
				throw accessDeniedException;// we suppose Spring Security Error Filter Chain will handle this properly.
				
			}else if(originalRequestUrl!=null){
				System.out.println(">>>> Security Process: STEP 3");
				//STEP 3: after Spring Security Authentication is triggered at STEP 2, 
				//then we need STEP 3 to redirect back to original URI the very first desktop belongs to.
				sess.removeAttribute(ORIGINAL_REQUEST_URL);
				exec.sendRedirect(originalRequestUrl);
				
			}else{// directly access the security process page, meaningless
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
	private static String getAccessDeniedHandlingPageUrl(AccessDeniedException ex){
		Configuration sConfiguration = WebApps.getCurrent().getConfiguration();
		String ePage = sConfiguration.getErrorPage("ajax", ex);// need to append query part back...
		return ePage;
	}
	
}
