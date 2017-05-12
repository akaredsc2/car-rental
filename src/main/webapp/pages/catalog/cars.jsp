<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<html>
<head>
    <title><fmt:message key="cars.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <form method="get" action="rental">
        <input type="hidden" name="command" value="add_car">
        <input type="submit" value="<fmt:message key="car.add.href" bundle="${info}"/>">
    </form>
</c:if>

<c:forEach items="${requestScope.attr_car_list}" var="car">
    <c:out value="${car.state}"/>
    <c:out value="${car.registrationPlate}"/>
    <c:out value="${car.color}"/>
    <c:out value="${car.pricePerDay}"/><br>
    <form method="get" action="rental">
        <input type="hidden" name="command" value="locations">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="cars.location.href" bundle="${info}"/>">
    </form>
    <form method="get" action="rental">
        <input type="hidden" name="command" value="models">
        <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
        <input type="submit" value="<fmt:message key="cars.model.href" bundle="${info}"/>">
    </form>
    <c:choose>
        <c:when test="${sessionScope.session_user.role=='CLIENT' and car.state=='available'}">
            <form method="post" action="rental">
                <label>
                    <fmt:message key="reservation.create.pick" bundle="${info}"/>
                    <input type="datetime-local" name="<fmt:message key="param.reservation.pick" bundle="${par}"/>">
                </label>
                <label>
                    <fmt:message key="reservation.create.drop" bundle="${info}"/>
                    <input type="datetime-local" name="<fmt:message key="param.reservation.drop" bundle="${par}"/>">
                </label>
                <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                       value="${car.id}">

                <input type="hidden" name="command" value="create_reservation">
                <input type="submit" value="<fmt:message key="reservation.create.submit" bundle="${info}"/>">
            </form>
        </c:when>

        <c:when test="${sessionScope.session_user.role=='ADMIN'}">
            <c:if test="${car.state=='unavailable'}">
                <form method="post" action="rental">
                    <label>
                        <fmt:message key="car.update.color" bundle="${info}"/>
                        <input type="text" name="<fmt:message key="param.car.color" bundle="${par}"/>"
                               value="${car.color}"
                               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                               minlength="3"
                               maxlength="30"
                               required>
                    </label><br>
                    <label>
                        <fmt:message key="car.update.price" bundle="${info}"/>
                        <input type="number" name="<fmt:message key="param.car.price" bundle="${par}"/>"
                               value="${car.pricePerDay}"
                               min="0.01"
                               max="1000"
                               step="0.01"
                               required>
                    </label><br>

                    <input type="hidden" name="command" value="update_car">
                    <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
                    <input type="submit" value="<fmt:message key="car.update.submit" bundle="${info}"/>">
                </form>

                <form method="get" action="rental">
                    <input type="hidden" name="command" value="move_car">
                    <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
                    <input type="submit" value="<fmt:message key="car.move.href" bundle="${info}"/>">
                </form>
            </c:if>

            <c:if test="${car.state == 'available' or car.state == 'unavailable' or car.state == 'served'}">
                <form method="post" action="rental">
                    <input type="hidden" name="command" value="change_car_state">
                    <input type="hidden" name="<fmt:message key="param.car.id" bundle="${par}"/>" value="${car.id}">
                    <input type="hidden" name="<fmt:message key="param.car.state" bundle="${par}"/>"
                           value="<c:out value="${car.state}"/>">
                    <label>
                        <fmt:message key="car.change.title" bundle="${info}"/>
                        <c:choose>
                            <c:when test="${car.state=='available'}">
                                <input type="hidden" name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                       value="unavailable">
                                <input type="submit"
                                       value="<fmt:message key="car.change.unavailable" bundle="${info}"/>">
                            </c:when>
                            <c:when test="${car.state=='unavailable'}">
                                <input type="hidden" name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                       value="available">
                                <input type="submit" value="<fmt:message key="car.change.available" bundle="${info}"/>">
                            </c:when>
                            <c:when test="${car.state=='served'}">
                                <input type="hidden" name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                       value="returned">
                                <input type="submit" value="<fmt:message key="car.change.returned" bundle="${info}"/>">
                            </c:when>
                        </c:choose>
                    </label>
                </form>
            </c:if>
        </c:when>
    </c:choose>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
