/**
 * 
 */
package org.zkoss.reference.developer.spring.security.ui.error;

import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.zkoss.reference.developer.spring.security.SecurityUtil;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.GenericInitiator;

/**
 * @author Ian YT Tsai (zanyking)
 * @author Hawk
 *
 */
public class AjaxAccessDeniedHandler extends GenericInitiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {
		// when this initiator has been executed that means users encounter access denied problem.
		
		Execution exec = Executions.getCurrent();
		
		if(exec.isAsyncUpdate(null) ){
			if (null == SecurityUtil.getUser()){ //unauthenticated user
				exec.sendRedirect("/login.zul");
			}else{
				throw (AccessDeniedException)exec.getAttribute("javax.servlet.error.exception");
			}
		}
	}

	
}
