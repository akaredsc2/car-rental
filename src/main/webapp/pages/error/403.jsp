<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="error.403.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<fmt:message key="error.403.description" bundle="${info}"/><br>

<c:choose>
    <c:when test="${empty sessionScope.session_user}">
        <a class="col-sm-offset-2 col-sm-10" href="<c:url value="/index.jsp"/>"><fmt:message key="signIn.href" bundle="${info}"/></a>
    </c:when>
    <c:when test="${not empty sessionScope.session_user}">
        <a class="col-sm-offset-2 col-sm-10" href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
    </c:when>
</c:choose>
</body>
</html>

