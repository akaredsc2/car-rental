<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="locations.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <a href="<c:url value="/pages/admin/add_location.jsp"/>"><fmt:message key="location.add.href" bundle="${info}"/></a><br>
</c:if>

<c:forEach items="${requestScope.attr_location_list}" var="location">
    <c:out value="${location.photoUrl}"/>
    <c:out value="${location.state}"/>
    <c:out value="${location.city}"/>
    <c:out value="${location.street}"/>
    <c:out value="${location.building}"/><br>
    <form method="get" action="controller/cars">
        <input type="hidden" name="<fmt:message key="param.location.id" bundle="${par}"/>" value="${location.id}">
        <input type="submit" value="<fmt:message key="locations.cars.href" bundle="${info}"/>">
    </form>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
