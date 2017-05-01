<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="cars.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <form method="get" action="add_car">
        <input type="submit" value="<fmt:message key="car.add.href" bundle="${info}"/>">
    </form>
</c:if>

<c:forEach items="${requestScope.attr_car_list}" var="car">
    ${car.state} ${car.registrationPlate} ${car.color} ${car.pricePerDay} <br>
    <form method="get" action="locations">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="locations.href" bundle="${info}"/>">
    </form>
    <form method="get" action="models">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="models.href" bundle="${info}"/>">
    </form>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
