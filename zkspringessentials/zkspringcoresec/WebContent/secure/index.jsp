<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="z" uri="http://www.zkoss.org/jsp/zul"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<z:page>
<z:window title="Secure Page" border="normal" width="500px">
<p>
This is a protected page. You can get to me if you've been remembered,
or if you've authenticated this session.
</p>

<sec:authorize ifAllGranted="ROLE_SUPERVISOR">
	You are a supervisor! You can therefore see the <a href="extreme/index.jsp">extremely secure page</a>.<br/><br/>
</sec:authorize>

<h4>Properties obtained using &lt;sec:authentication /&gt; tag</h4>
<z:grid>
	<z:columns>
		<z:column label="Tag"/>
		<z:column label="Value" width="50px"/>
	</z:columns>
	<z:rows>
		<z:row><z:label value="<sec:authentication property='name' />"/><sec:authentication property="name"/></z:row>
		<z:row><z:label value="<sec:authentication property='principal.username' />"/><sec:authentication property="principal.username"/></z:row>
		<z:row><z:label value="<sec:authentication property='principal.enabled' />"/><sec:authentication property="principal.enabled"/></z:row>
		<z:row><z:label value="<sec:authentication property='principal.accountNonLocked' />"/><sec:authentication property="principal.accountNonLocked"/></z:row>
	</z:rows>
</z:grid>
<z:separator bar="true"></z:separator>
<z:button label="Home" href="../home.zul"/>
<z:button label="Logout" href="../j_spring_security_logout"/>
</z:window>
</z:page>
</body>
</html>
