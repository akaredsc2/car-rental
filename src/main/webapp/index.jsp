<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="signIn.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="inc/locale.jsp"/>
<form method="post" action="CarRental">
    <label>
        <fmt:message key="signIn.login" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="signIn.password" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="sign_in">
    <input type="submit" value="<fmt:message key="signIn.submit" bundle="${info}"/>">
</form>
<a href="registration.jsp"><fmt:message key="reg.href" bundle="${info}"/></a>
</body>
</html>
