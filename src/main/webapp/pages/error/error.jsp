<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="error.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<fmt:message key="error.description" bundle="${info}"/><br>

<c:forEach items="${requestScope.attr_error}" var="error">
    <fmt:message key="${error}" bundle="${info}"/><br>
</c:forEach>

<c:choose>
    <c:when test="${empty sessionScope.session_user}">
        <a class="col-sm-offset-2 col-sm-10" href="<c:url value="/index.jsp"/>"><fmt:message key="signIn.href"
                                                                                             bundle="${info}"/></a>
    </c:when>
    <c:when test="${not empty sessionScope.session_user}">
        <a class="col-sm-offset-2 col-sm-10" href="<c:url value="/home.jsp"/>"><fmt:message key="home.href"
                                                                                            bundle="${info}"/></a>
    </c:when>
</c:choose>
</body>
</html>
