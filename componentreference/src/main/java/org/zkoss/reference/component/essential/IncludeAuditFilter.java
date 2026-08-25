/* IncludeAuditFilter.java

	Purpose:
		Prove that <include mode="defer"> runs the container's full request lifecycle.

	Description:
		Mapped in web.xml with <dispatcher>INCLUDE</dispatcher>. If it runs, the
		included resource was reached through RequestDispatcher#include and therefore
		went through the ordinary servlet filter chain -- which is exactly what JSP,
		JSF and other request-driven technologies rely on.

	History:
		Created for the ZK Component Reference "Include" page.
*/
package org.zkoss.reference.component.essential;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** Records its own name on the request so the included resource can report it. */
public class IncludeAuditFilter implements Filter {
	public static final String ATTR_AUDIT = "org.zkoss.reference.include.audit";

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute(ATTR_AUDIT, IncludeAuditFilter.class.getSimpleName());
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
