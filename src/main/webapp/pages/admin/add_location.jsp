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
    <title><fmt:message key="location.add.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="rental" enctype="multipart/form-data">
    <label>
        <fmt:message key="location.add.photo" bundle="${info}"/>
        <input type="file" name="<fmt:message key="param.location.photo" bundle="${par}"/>">
    </label>
    <label>
        <fmt:message key="location.add.state" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.state" bundle="${par}"/>" required
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               minlength="4"
               maxlength="30">
    </label><br>
    <label>
        <fmt:message key="location.add.city" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.city" bundle="${par}"/>" required
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               minlength="4"
               maxlength="30">
    </label><br>
    <label>
        <fmt:message key="location.add.street" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.street" bundle="${par}"/>" required
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               minlength="4"
               maxlength="30">
    </label><br>
    <label>
        <fmt:message key="location.add.building" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.building" bundle="${par}"/>" required
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               minlength="4"
               maxlength="30">
    </label><br>

    <input type="hidden" name="command" value="add_location">
    <input type="submit" value="<fmt:message key="location.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>

