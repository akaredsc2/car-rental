<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="error.title" bundle="${info}"/></title>
</head>
<body>
<c:import url="inc/welcome.jsp"/>
<fmt:message key="error.general" bundle="${info}"/><br>

${requestScope.attr_error}<br>

<c:choose>
    <c:when test="${empty sessionScope.session_user}">
        <a href="index.jsp"><fmt:message key="sign.in.href" bundle="${info}"/></a>
    </c:when>
    <c:when test="${not empty sessionScope.session_user}">
        <a href="home.jsp"><fmt:message key="home.href" bundle="${info}"/></a>
    </c:when>
</c:choose>
</body>
</html>
