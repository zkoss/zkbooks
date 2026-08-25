/* SalesReportServlet.java

	Purpose:
		Demonstrate why <include mode="defer"> exists.

	Description:
		This servlet stands in for the kind of resource defer mode was built for:
		a non-ZUML technology (JSP, JSF, Struts, a reporting servlet, ...) that only
		produces output when the servlet container runs a full request lifecycle for
		it, and that takes its input from the query string.

		In defer mode Include calls Execution#include, which ends up in
		RequestDispatcher#include -- so this servlet is reached exactly as if the
		browser had requested it. In instant mode Include calls
		Execution#createComponents on a ZUML page definition instead, which cannot
		reach a servlet at all.

	History:
		Created for the ZK Component Reference "Include" page.
*/
package org.zkoss.reference.component.essential;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Writes an HTML fragment reporting what the servlet container handed it.
 *
 * <p>Everything it prints is evidence that defer mode dispatched a real request:
 * <ul>
 * <li><code>getDispatcherType()</code> is <code>INCLUDE</code> -- the container,
 * not ZK, invoked this servlet.</li>
 * <li><code>getParameter()</code> returns the values from the query string written
 * in the <code>src</code> attribute.</li>
 * <li>the <code>javax.servlet.include.*</code> request attributes are set, which
 * only <code>RequestDispatcher#include</code> does.</li>
 * <li>the filter chain ran first (see {@link IncludeAuditFilter});</li>
 * <li>the call counter grows every time the include is invalidated, because each
 * re-include is a new request.</li>
 * </ul>
 *
 * <p>Note: an included fragment must not emit &lt;html&gt; or &lt;!DOCTYPE&gt;;
 * Include rejects that with a UiException.
 */
public class SalesReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Counts service() calls, so a re-include is visible on the page. */
	private static final AtomicInteger CALLS = new AtomicInteger();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final PrintWriter out = response.getWriter();

		out.println("<div class='report'>");
		out.println("<h3>Sales report (rendered by a servlet)</h3>");
		out.println("<dl>");

		//1. the query string of src became real request parameters
		row(out, "request.getParameter(\"region\")", request.getParameter("region"));
		row(out, "request.getParameter(\"year\")", request.getParameter("year"));

		//2. <include> dynamic properties arrive as request attributes
		row(out, "request.getAttribute(\"currency\")", String.valueOf(request.getAttribute("currency")));

		//3. proof that the servlet container performed the dispatch
		row(out, "request.getDispatcherType()", request.getDispatcherType().name());
		row(out, RequestDispatcher.INCLUDE_SERVLET_PATH,
				(String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH));
		row(out, RequestDispatcher.INCLUDE_QUERY_STRING,
				(String) request.getAttribute(RequestDispatcher.INCLUDE_QUERY_STRING));

		//4. proof that the container's filter chain ran for this dispatch
		row(out, "filter that ran before this servlet", (String) request.getAttribute(IncludeAuditFilter.ATTR_AUDIT));

		//5. every (re-)include is a fresh call, so this number grows on invalidate()
		row(out, "times this servlet has been called", String.valueOf(CALLS.incrementAndGet()));

		out.println("</dl>");
		out.println("</div>");
	}

	private static void row(PrintWriter out, String name, String value) {
		out.print("<dt>");
		out.print(escape(name));
		out.println("</dt>");
		out.print("<dd>");
		out.print(value != null ? escape(value) : "<em>null</em>");
		out.println("</dd>");
	}

	/** The values come from the query string, so they are escaped before echoing. */
	private static String escape(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
