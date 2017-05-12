<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user or sessionScope.session_user.role != 'ADMIN'}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<html>
<head>
    <title><fmt:message key="model.add.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="rental" enctype="multipart/form-data">
    <label>
        <fmt:message key="model.add.photo" bundle="${info}"/>
        <input type="file" name="<fmt:message key="param.model.photo" bundle="${par}"/>">
    </label>
    <label>
        <fmt:message key="model.add.name" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.model.name" bundle="${par}"/>"
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               required>
    </label><br>
    <label>
        <fmt:message key="model.add.door" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.door" bundle="${par}"/>"
               min="1" max="10" step="1"
               required>
    </label><br>
    <label>
        <fmt:message key="model.add.seat" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.seat" bundle="${par}"/>"
               min="1" max="100" step="1"
               required>
    </label><br>
    <label>
        <fmt:message key="model.add.horse" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.horse" bundle="${par}"/>"
               min="1" max="3000" step="1"
               required>
    </label><br>

    <input type="hidden" name="command" value="add_model">
    <input type="submit" value="<fmt:message key="model.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
