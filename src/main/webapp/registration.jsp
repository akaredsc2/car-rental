<%--
  Created by IntelliJ IDEA.
  User: vitaly
  Date: 2017-04-27
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="parameters" var="par"/>
<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="reg"/>
<html>
<head>
    <title><fmt:message key="reg.title" bundle="${reg}"/></title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        <fmt:message key="reg.login" bundle="${reg}"/>
        <input type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="reg.password" bundle="${reg}"/>
        <input type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="reg.name" bundle="${reg}"/>
        <input type="text" name="<fmt:message key="param.user.name" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.birthday" bundle="${reg}"/>
        <input type="datetime-local" name="<fmt:message key="param.user.birthday" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.passport" bundle="${reg}"/>
        <input type="text" name="<fmt:message key="param.user.passport" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.driver" bundle="${reg}"/>
        <input type="text" name="<fmt:message key="param.user.driver" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="registration">
    <input type="submit">
</form>
<a href="index.jsp">index</a>
</body>
</html>
