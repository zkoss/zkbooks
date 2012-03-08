<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="z" uri="http://www.zkoss.org/jsp/zul"  %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<z:page>
<z:window title="VERY Secure Page" border="normal" width="500px">
This is a protected page. You can only see me if you are a supervisor.

<authz:authorize ifAllGranted="ROLE_SUPERVISOR">
   You have "ROLE_SUPERVISOR" (this text is surrounded by &lt;authz:authorize&gt; tags).
</authz:authorize>

<z:separator bar="true" spacing="10px"/>
<z:hbox>
<z:button label="Home" href="../../home.zul"/>
<z:button label="Logout" href="../../j_spring_security_logout"/>
</z:hbox>
</z:window>
</z:page>
</body>
</html>