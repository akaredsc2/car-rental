<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="error.404.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="inc/locale.jsp"/>
<jsp:include page="inc/welcome.jsp"/>

<fmt:message key="error.404.description" bundle="${info}"/><br>

<c:choose>
    <c:when test="${empty sessionScope.session_user}">
        <a href="index.jsp"><fmt:message key="signIn.href" bundle="${info}"/></a>
    </c:when>
    <c:when test="${not empty sessionScope.session_user}">
        <a href="home.jsp"><fmt:message key="home.href" bundle="${info}"/></a>
    </c:when>
</c:choose>
</body>
</html>