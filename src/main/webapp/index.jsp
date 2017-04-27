<%--
  Created by IntelliJ IDEA.
  User: vitaly
  Date: 2017-04-11
  Time: 21:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="parameters" var="par"/>
<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="in"/>
<html>
<head>
    <title><fmt:message key="sign.in.title" bundle="${in}"/></title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        <fmt:message key="sign.in.login" bundle="${in}"/>
        <input type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="sign.in.password" bundle="${in}"/>
        <input type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="sign_in">
    <input type="submit">
</form>
<a href="registration.jsp">register new user</a>
</body>
</html>
