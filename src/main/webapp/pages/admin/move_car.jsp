<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="car.move.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="move_car">
    ${requestScope.attr_car_id}<br>
    <label>
        <fmt:message key="car.move.location" bundle="${info}"/>
        <select name="<fmt:message key="param.location.id" bundle="${par}"/>" required>
            <c:forEach items="${requestScope.attr_location_list}" var="location">
                <option value="${location.id}">
                    <c:out value="${location.state}"/>
                    <c:out value="${location.city}"/>
                    <c:out value="${location.street}"/>
                    <c:out value="${location.building}"/>
                </option>
            </c:forEach>
        </select>
    </label><br>
    <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${requestScope.attr_car_id}">
    <input type="submit" value="<fmt:message key="car.move.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
