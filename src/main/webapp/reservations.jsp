<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="reservations.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <form method="get" action="rental">
        <input type="hidden" name="command" value="reservations">
        <input type="hidden" name="<fmt:message key="param.unassigned" bundle="${par}"/>" value="true">
        <input type="submit" value="<fmt:message key="reservations.unassigned.submit" bundle="${info}"/>">
    </form>
</c:if>

<c:forEach items="${requestScope.attr_reservation_list}" var="res">
    <fmt:message key="reservations.id" bundle="${info}"/> : <c:out value="${res.id}"/><br>
    <fmt:message key="reservations.car" bundle="${info}"/> : <c:out value="${res.car.id}"/><br>
    <fmt:message key="reservations.pick" bundle="${info}"/> : <c:out value="${res.pickUpDatetime}"/><br>
    <fmt:message key="reservations.drop" bundle="${info}"/> : <c:out value="${res.dropOffDatetime}"/><br>
    <fmt:message key="reservations.state" bundle="${info}"/> : <c:out value="${res.state}"/><br>
    <fmt:message key="reservations.reason" bundle="${info}"/> : <c:out value="${res.rejectionReason}"/><br>

    <form method="get" action="rental">
        <input type="hidden" name="command" value="cars">
        <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
        <input type="submit" value="<fmt:message key="reservations.car.submit" bundle="${info}"/>">
    </form>

    <c:if test="${sessionScope.session_user.role=='ADMIN' and (empty res.admin)}">
        <form method="post" action="rental">
            <input type="hidden" name="command" value="assign">
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="submit" value="<fmt:message key="reservations.assign.submit" bundle="${info}"/>">
        </form>
    </c:if>

    <c:if test="${sessionScope.session_user.role=='ADMIN' and (not empty res.admin)}">
        <c:choose>
            <c:when test="${res.state=='new'}">
                <form method="post" action="rental">
                    <input type="hidden" name="command" value="change_reservation_state">
                    <label>
                        <fmt:message key="reservations.change.reason" bundle="${info}"/>
                        <input type="text" name="<fmt:message key="param.reservation.reason" bundle="${par}"/>"
                               required>
                    </label>
                    <input type="hidden" value="rejected"
                           name="<fmt:message key="param.reservation.state" bundle="${par}"/>">
                    <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                           value="${res.id}">
                    <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                           value="${res.car.id}">
                    <input type="submit" value="<fmt:message key="reservations.change.rejected" bundle="${info}"/>">
                </form>

                <form method="post" action="rental">
                    <input type="hidden" name="command" value="change_reservation_state">
                    <input type="hidden" value="approved"
                           name="<fmt:message key="param.reservation.state" bundle="${par}"/>">
                    <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                           value="${res.id}">
                    <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                           value="${res.car.id}">
                    <input type="submit" value="<fmt:message key="reservations.change.approved" bundle="${info}"/>">
                </form>
            </c:when>
            <c:when test="${res.state=='approved'}">
                <form method="post" action="rental">
                    <input type="hidden" name="command" value="change_reservation_state">
                    <input type="hidden" value="active"
                           name="<fmt:message key="param.reservation.state" bundle="${par}"/>">
                    <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                           value="${res.id}">
                    <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                           value="${res.car.id}">
                    <input type="submit" value="<fmt:message key="reservations.change.active" bundle="${info}"/>">
                </form>
            </c:when>
            <c:when test="${res.state=='active'}">
                <form method="post" action="rental">
                    <input type="hidden" name="command" value="change_reservation_state">
                    <input type="hidden" value="closed"
                           name="<fmt:message key="param.reservation.state" bundle="${par}"/>">
                    <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                           value="${res.id}">
                    <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                           value="${res.car.id}">
                    <input type="submit" value="<fmt:message key="reservations.change.closed" bundle="${info}"/>">
                </form>
            </c:when>
        </c:choose>
    </c:if>

    <c:if test="${sessionScope.session_user.role=='CLIENT' and res.state=='approved'}">
        <form method="post" action="rental">
            <input type="hidden" name="command" value="cancel_reservation">
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                   value="${res.car.id}">
            <input type="submit" value="<fmt:message key="reservations.cancel" bundle="${info}"/>">
        </form>
    </c:if>

    <c:if test="${res.state=='approved' or res.state=='active'}">
        <form method="get" action="rental">
            <input type="hidden" name="command" value="bills">
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="submit" value="<fmt:message key="bills.href" bundle="${info}"/>">
        </form>
    </c:if>

    <c:if test="${sessionScope.session_user.role=='ADMIN' and res.state=='active'}">
        <form method="post" action="rental">
            <input type="hidden" name="command" value="add_damage_bill">
            <label>
                <fmt:message key="reservations.bill.amount" bundle="${info}"/>
                <input type="number" name="<fmt:message key="param.bill.amount" bundle="${par}"/>" required
                       min="0.01"
                       step="0.01"
                       max="100000"
                >
            </label>
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="submit" value="<fmt:message key="bills.href" bundle="${info}"/>">
        </form>
    </c:if>
</c:forEach>
</body>
</html>