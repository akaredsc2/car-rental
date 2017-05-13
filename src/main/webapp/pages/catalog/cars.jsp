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
    <title><fmt:message key="cars.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-8"></div>
            <div class="col-xs-4">
                <form method="get" action="rental">
                    <input type="hidden"
                           name="command"
                           value="add_car">
                    <input type="submit"
                           class="btn btn-default"
                           value="<fmt:message key="car.add.href" bundle="${info}"/>">
                </form>
            </div>
        </div>
    </div>
</c:if>

<c:forEach items="${requestScope.attr_car_list}" var="car">
    <div class="container debug-border width-75">

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="car.state" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${car.state}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="car.add.color" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${car.color}"/>
            </div>

            <div class="col-xs-4">
                <form method="get" action="rental">
                    <input type="hidden"
                           name="command"
                           value="models">

                    <input type="hidden"
                           name="<fmt:message key="param.car.id" bundle="${par}"/>"
                           value="${car.id}">

                    <input type="submit"
                           class="btn btn-link"
                           value="<fmt:message key="cars.model.href" bundle="${info}"/>">
                </form>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="car.add.price" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${car.pricePerDay}"/>
            </div>

            <div class="col-xs-4">
                <form method="get" action="rental">
                    <input type="hidden"
                           name="command"
                           value="locations">

                    <input type="hidden"
                           name="<fmt:message key="param.car.id" bundle="${par}"/>"
                           value="${car.id}">

                    <input type="submit"
                           class="btn btn-link"
                           value="<fmt:message key="cars.location.href" bundle="${info}"/>">
                </form>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="car.add.plate" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${car.registrationPlate}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <c:choose>
                    <c:when test="${sessionScope.session_user.role=='CLIENT' and car.state=='available'}">
                        <div class="row">
                            <form method="post" action="rental" class="form-inline">
                                <div class="form-group col-xs-4">
                                    <label for="pu">
                                        <fmt:message key="reservation.create.pick" bundle="${info}"/>
                                    </label>

                                    <input id="pu"
                                           type="datetime-local"
                                           class="form-control"
                                           name="<fmt:message key="param.reservation.pick" bundle="${par}"/>">
                                </div>

                                <div class="form-group col-xs-4">
                                    <label for="do">
                                        <fmt:message key="reservation.create.drop" bundle="${info}"/>
                                    </label>

                                    <input id="do"
                                           type="datetime-local"
                                           class="form-control"
                                           name="<fmt:message key="param.reservation.drop" bundle="${par}"/>">
                                </div>

                                <input type="hidden"
                                       name="<fmt:message key="param.reservation.car" bundle="${par}"/>"
                                       value="${car.id}">

                                <input type="hidden"
                                       name="command"
                                       value="create_reservation">

                                <div class="form-group col-xs-4">
                                    <input type="submit"
                                           class="btn btn-default"
                                           value="<fmt:message key="reservation.create.submit" bundle="${info}"/>">
                                </div>
                            </form>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.session_user.role=='ADMIN'}">
                        <c:if test="${car.state == 'available' or car.state == 'unavailable' or car.state == 'served'}">
                            <div class="row">
                                <div class="col-xs-8"></div>
                                <div class="col-xs-4">
                                    <form method="post" action="rental" class="form-inline">
                                        <input type="hidden"
                                               name="command"
                                               value="change_car_state">

                                        <input type="hidden"
                                               name="<fmt:message key="param.car.id" bundle="${par}"/>"
                                               value="${car.id}">

                                        <c:choose>
                                            <c:when test="${car.state=='available'}">
                                                <input type="hidden"
                                                       name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                                       value="unavailable">
                                                <input type="submit"
                                                       class="btn btn-default"
                                                       value="<fmt:message key="car.change.unavailable" bundle="${info}"/>">
                                            </c:when>
                                            <c:when test="${car.state=='unavailable'}">
                                                <input type="hidden"
                                                       name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                                       value="available">
                                                <input type="submit"
                                                       class="btn btn-default"
                                                       value="<fmt:message key="car.change.available" bundle="${info}"/>">
                                            </c:when>
                                            <c:when test="${car.state=='served'}">
                                                <input type="hidden"
                                                       name="<fmt:message key="param.car.state.new" bundle="${par}"/>"
                                                       value="returned">
                                                <input type="submit"
                                                       class="btn btn-default"
                                                       value="<fmt:message key="car.change.returned" bundle="${info}"/>">
                                            </c:when>
                                        </c:choose>
                                    </form>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${car.state=='unavailable'}">
                            <div class="row">
                                <div class="col-xs-8">
                                    <form method="post" action="rental" class="form-inline">
                                        <div class="form-group">
                                            <label for="color">
                                                <fmt:message key="car.update.color" bundle="${info}"/>
                                            </label>

                                            <div class="tip">
                                                <span class="tiptext">
                                                    <fmt:message key="correct.format" bundle="${info}"/><br>
                                                    <fmt:message key="correct.color" bundle="${info}"/>
                                                </span>

                                                <input id="color" type="text"
                                                       name="<fmt:message key="param.car.color" bundle="${par}"/>"
                                                       value="${car.color}"
                                                       class="form-control"
                                                       pattern="<fmt:message key="correct.color" bundle="${info}"/>"
                                                       minlength="3"
                                                       maxlength="30"
                                                       required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="price">
                                                <fmt:message key="car.update.price" bundle="${info}"/>
                                            </label>

                                            <input id="price" type="number"
                                                   name="<fmt:message key="param.car.price" bundle="${par}"/>"
                                                   value="${car.pricePerDay}"
                                                   class="form-control"
                                                   min="0.01"
                                                   max="1000"
                                                   step="0.01"
                                                   required>
                                        </div>

                                        <input type="hidden"
                                               name="command"
                                               value="update_car">

                                        <input type="hidden"
                                               name="<fmt:message key="param.car.id" bundle="${par}"/>"
                                               value="${car.id}">

                                        <input type="submit"
                                               class="btn btn-default"
                                               value="<fmt:message key="car.update.submit" bundle="${info}"/>">
                                    </form>
                                </div>

                                <div class="col-xs-4">
                                    <form method="get" action="rental">
                                        <input type="hidden"
                                               name="command"
                                               value="move_car">

                                        <input type="hidden"
                                               name="<fmt:message key="param.car.id" bundle="${par}"/>"
                                               value="${car.id}">

                                        <input type="submit"
                                               class="btn btn-default"
                                               value="<fmt:message key="car.move.href" bundle="${info}"/>">
                                    </form>
                                </div>
                            </div>
                        </c:if>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>
</c:forEach>
</body>
</html>
