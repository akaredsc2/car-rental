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
    <c:out value="${car.state}"/>
    <c:out value="${car.registrationPlate}"/>
    <c:out value="${car.color}"/>
    <c:out value="${car.pricePerDay}"/><br>
    <form method="get" action="locations">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="cars.location.href" bundle="${info}"/>">
    </form>
    <form method="get" action="models">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="cars.model.href" bundle="${info}"/>">
    </form>
    <c:if test="${sessionScope.session_user.role=='ADMIN'}">
        <form method="post" action="update_car">
            <label>
                <fmt:message key="car.update.color" bundle="${info}"/>
                <input type="text" name="<fmt:message key="param.car.color" bundle="${par}"/>" required>
            </label><br>
            <label>
                <fmt:message key="car.update.price" bundle="${info}"/>
                <input type="number" name="<fmt:message key="param.car.price" bundle="${par}"/>" min="1" max="1000"
                       step="0.01" required>
            </label><br>
            <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">

            <input type="submit" value="<fmt:message key="car.update.submit" bundle="${info}"/>">
        </form>

        <form method="get" action="move_car">
            <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
            <input type="submit" value="<fmt:message key="car.move.href" bundle="${info}"/>">
        </form>

        <form method="post" action="change_car_state">
            <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
            <input type="hidden" name="<fmt:message key="param.car.state" bundle="${par}"/>"
                   value="<c:out value="${car.state}"/>">
            <label>
                <fmt:message key="car.change.title" bundle="${info}"/>
                <select name="<fmt:message key="param.car.state.new" bundle="${par}"/>">
                    <option value="unavailable"><fmt:message key="car.change.unavailable" bundle="${info}"/></option>
                    <option value="available"><fmt:message key="car.change.available" bundle="${info}"/></option>
                    <option value="reserved"><fmt:message key="car.change.reserved" bundle="${info}"/></option>
                    <option value="served"><fmt:message key="car.change.served" bundle="${info}"/></option>
                    <option value="returned"><fmt:message key="car.change.returned" bundle="${info}"/></option>
                </select>
            </label>
            <input type="submit" value="<fmt:message key="car.change.submit" bundle="${info}"/>">
        </form>
    </c:if>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
