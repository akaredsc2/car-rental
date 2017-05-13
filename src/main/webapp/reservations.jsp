<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="reservations.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-8"></div>
            <div class="col-xs-4">
                <form method="get" action="rental" class="form-horizontal">
                    <input type="submit"
                           class="btn btn-default"
                           value="<fmt:message key="reservations.unassigned.submit" bundle="${info}"/>">

                    <input type="hidden"
                           name="command"
                           value="reservations">

                    <input type="hidden"
                           name="<fmt:message key="param.unassigned" bundle="${par}"/>"
                           value="true">
                </form>
            </div>
        </div>
    </div>
</c:if>

<c:forEach items="${requestScope.attr_reservation_list}" var="res">
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="reservations.id" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${res.id}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <form method="get" action="rental">
                    <input type="hidden"
                           name="command"
                           value="cars">

                    <input type="hidden"
                           name="<fmt:message key="param.reservation.id"
                           bundle="${par}"/>"
                           value="${res.id}">

                    <input type="submit"
                           class="btn btn-link"
                           value="<fmt:message key="reservations.car.submit" bundle="${info}"/>">
                </form>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="reservations.pick" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${res.pickUpDatetime}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="reservations.drop" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${res.dropOffDatetime}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="reservations.state" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${res.state}"/>
            </div>
        </div>

        <c:if test="${res.state=='rejected'}">
            <div class="row">
                <div class="col-xs-4">
                    <fmt:message key="reservations.reason" bundle="${info}"/>
                </div>

                <div class="col-xs-4">
                    <c:out value="${res.rejectionReason}"/>
                </div>
            </div>
        </c:if>

        <c:if test="${sessionScope.session_user.role=='ADMIN' and (empty res.admin)}">
            <div class="row">
                <div class="col-xs-4">
                    <form method="post" action="rental">
                        <input type="hidden"
                               name="command"
                               value="assign">

                        <input type="hidden"
                               name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                               value="${res.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="reservations.assign.submit" bundle="${info}"/>">
                    </form>
                </div>
            </div>
        </c:if>

        <c:if test="${sessionScope.session_user.role=='ADMIN' and (not empty res.admin)}">
            <c:choose>
                <c:when test="${res.state=='new'}">
                    <div class="row">
                        <div class="col-xs-8">
                            <form method="post" action="rental" class="form-inline">
                                <input type="hidden"
                                       name="command"
                                       value="change_reservation_state">

                                <div class="col-xs-6">
                                    <input type="text"
                                           class="form-control"
                                           name="<fmt:message key="param.reservation.reason" bundle="${par}"/>"
                                           placeholder="<fmt:message key="reservations.change.reason" bundle="${info}"/>"
                                           minlength="4"
                                           maxlength="150"
                                           required>
                                </div>

                                <input type="hidden"
                                       value="rejected"
                                       name="<fmt:message key="param.reservation.state" bundle="${par}"/>">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                                       value="${res.id}">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                                       value="${res.car.id}">

                                <div class="col-xs-6">
                                    <input type="submit"
                                           class="btn btn-default"
                                           value="<fmt:message key="reservations.change.rejected" bundle="${info}"/>">
                                </div>
                            </form>
                        </div>

                        <div class="col-xs-4">
                            <form method="post" action="rental">
                                <input type="hidden"
                                       name="command"
                                       value="change_reservation_state">

                                <input type="hidden"
                                       value="approved"
                                       name="<fmt:message key="param.reservation.state" bundle="${par}"/>">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                                       value="${res.id}">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                                       value="${res.car.id}">

                                <input type="submit"
                                       class="btn btn-default"
                                       value="<fmt:message key="reservations.change.approved" bundle="${info}"/>">
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:when test="${res.state=='approved'}">
                    <div class="row">
                        <div class="col-xs-8"></div>
                        <div class="col-xs-4">
                            <form method="post" action="rental" class="form-inline">
                                <input type="hidden"
                                       name="command"
                                       value="change_reservation_state">

                                <input type="hidden"
                                       value="active"
                                       name="<fmt:message key="param.reservation.state" bundle="${par}"/>">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                                       value="${res.id}">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                                       value="${res.car.id}">

                                <input type="submit"
                                       class="btn btn-default"
                                       value="<fmt:message key="reservations.change.active" bundle="${info}"/>">
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:when test="${res.state=='active'}">
                    <div class="row">
                        <div class="col-xs-8"></div>
                        <div class="col-xs-4">
                            <form method="post" action="rental" class="form-inline">
                                <input type="hidden"
                                       name="command"
                                       value="change_reservation_state">

                                <input type="hidden"
                                       value="closed"
                                       name="<fmt:message key="param.reservation.state" bundle="${par}"/>">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                                       value="${res.id}">

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                                       value="${res.car.id}">

                                <input type="submit"
                                       class="btn btn-default"
                                       value="<fmt:message key="reservations.change.closed" bundle="${info}"/>">
                            </form>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </c:if>

        <c:if test="${sessionScope.session_user.role=='CLIENT' and res.state=='approved'}">
            <div class="row">
                <div class="col-xs-8"></div>
                <div class="col-xs-4">
                    <form method="post" action="rental" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="cancel_reservation">

                        <input type="hidden"
                               name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                               value="${res.id}">

                        <input type="hidden"
                               name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                               value="${res.car.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="reservations.cancel" bundle="${info}"/>">
                    </form>
                </div>
            </div>
        </c:if>

        <div class="row">
            <c:if test="${res.state=='approved' or res.state=='active'}">
                <div class="col-xs-4">
                    <form method="get" action="rental" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="bills">

                        <input type="hidden"
                               name="<fmt:message key="param.reservation.id" bundle="${par}"/>"
                               value="${res.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="bills.href" bundle="${info}"/>">
                    </form>
                </div>
            </c:if>

            <c:if test="${sessionScope.session_user.role=='ADMIN' and res.state=='active'}">
                <div class="col-xs-8">
                    <form method="post" action="rental" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="add_damage_bill">


                        <div class="col-xs-6">
                            <div class="input-group">
                                <div class="input-group-addon">$</div>
                                <input type="number"
                                       name="<fmt:message key="param.bill.amount" bundle="${par}"/>"
                                       required
                                       class="form-control"
                                       placeholder="<fmt:message key="reservations.bill.amount" bundle="${info}"/>"
                                       min="0.01"
                                       step="0.01"
                                       max="100000">
                            </div>
                        </div>

                        <input type="hidden"
                               name="<fmt:message key="param.reservation.id" bundle="${par}"/>" value="${res.id}">

                        <div class="col-xs-6">
                            <input type="submit"
                                   class="btn btn-default"
                                   value="<fmt:message key="bill.damage.submit" bundle="${info}"/>">
                        </div>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>
</body>
</html>