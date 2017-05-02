<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="personal.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<fmt:message key="personal.login" bundle="${info}"/> : <c:out value="${sessionScope.session_user.login}"/><br>
<fmt:message key="personal.name" bundle="${info}"/> : <c:out value="${sessionScope.session_user.fullName}"/><br>
<fmt:message key="personal.birthday" bundle="${info}"/> : <c:out value="${sessionScope.session_user.birthDate}"/><br>
<fmt:message key="personal.passport" bundle="${info}"/> : <c:out
        value="${sessionScope.session_user.passportNumber}"/><br>
<fmt:message key="personal.driver" bundle="${info}"/> : <c:out
        value="${sessionScope.session_user.driverLicenceNumber}"/><br>
<fmt:message key="personal.role" bundle="${info}"/> : <c:out value="${sessionScope.session_user.role}"/><br>

<form method="post" action="change_password">
    <label>
        <fmt:message key="personal.change.old" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.pass.old" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="personal.change.new" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.pass.new" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="personal.change.repeat" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.pass.repeat" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.user.id" bundle="${par}"/>"
           value="${sessionScope.session_user.id}">

    <input type="submit" value="<fmt:message key="personal.change.submit" bundle="${info}"/>">
</form>

<c:if test="${sessionScope.session_user.role == 'ADMIN'}">
    <form action="promote" method="get">
        <input type="submit" value="<fmt:message key="personal.promote.submit" bundle="${info}"/>">
    </form>
</c:if>
</body>
</html>
