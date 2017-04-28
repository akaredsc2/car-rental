<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="reg.title" bundle="${info}"/></title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        <fmt:message key="reg.login" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="reg.password" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="reg.name" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.name" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.birthday" bundle="${info}"/>
        <input type="datetime-local" name="<fmt:message key="param.user.birthday" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.passport" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.passport" bundle="${par}"/>" required>
    </label>
    <label><br>
        <fmt:message key="reg.driver" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.driver" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="registration">
    <input type="submit" value="<fmt:message key="reg.submit" bundle="${info}"/>">
</form>
<a href="index.jsp"><fmt:message key="sign.in.href" bundle="${info}"/></a>
</body>
</html>
