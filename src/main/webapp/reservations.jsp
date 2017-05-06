<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="reservations.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <form method="get" action="reservations">
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

    <c:if test="${sessionScope.session_user.role=='ADMIN' and (empty res.admin)}">
        <form method="post" action="assign">
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="submit" value="<fmt:message key="reservations.assign.submit" bundle="${info}"/>">
        </form>
    </c:if>

    <c:if test="${sessionScope.session_user.role=='ADMIN' and (not empty res.admin)}">
        <form method="post" action="change_reservation_state">
            <label>
                <fmt:message key="reservations.change.state" bundle="${info}"/>
                <select name="<fmt:message key="param.reservation.state" bundle="${par}"/>">
                    <option value="approved"><fmt:message key="reservations.change.approved" bundle="${info}"/></option>
                    <option value="rejected"><fmt:message key="reservations.change.rejected" bundle="${info}"/></option>
                    <option value="canceled"><fmt:message key="reservations.change.canceled" bundle="${info}"/></option>
                    <option value="active"><fmt:message key="reservations.change.active" bundle="${info}"/></option>
                    <option value="closed"><fmt:message key="reservations.change.closed" bundle="${info}"/></option>
                </select>
            </label>
            <label>
                <fmt:message key="reservations.change.reason" bundle="${info}"/>
                <input type="text" name="<fmt:message key="param.reservation.reason" bundle="${par}"/>" required>
            </label>
            <input type="hidden" name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">
            <input type="hidden" name="<fmt:message key="param.reservation.car" bundle="${par}"/>" value="${res.car.id}">
            <input type="submit" value="<fmt:message key="reservations.change.submit" bundle="${info}"/>">
        </form>
    </c:if>
</c:forEach>
</body>
</html>