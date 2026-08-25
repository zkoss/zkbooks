<%--
  The real-world case for <include mode="defer">: a JSP fragment.

  A JSP is a servlet. It only produces markup when the container compiles it and
  runs its service() method for a request, and it reads its input from the request.
  Instant mode cannot render it -- instant mode parses ZUML, it does not dispatch
  requests.

  Requires a JSP-enabled container; see the note in include-defer-mode.zul.
--%>
<div class="report">
  <h3>Sales report (rendered by a JSP)</h3>
  <dl>
    <dt>param.region (from the query string of src)</dt>
    <dd>${param.region}</dd>
    <dt>param.year (from the query string of src)</dt>
    <dd>${param.year}</dd>
    <dt>requestScope.currency (from a dynamic property)</dt>
    <dd>${requestScope.currency}</dd>
    <dt>served by</dt>
    <dd><%= getClass().getName() %></dd>
  </dl>
</div>
