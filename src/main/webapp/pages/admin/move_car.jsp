<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user or sessionScope.session_user.role != 'ADMIN'}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="car.move.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" class="form-horizontal">
        <div class="form-group">
            <label for="rp" class="control-label col-xs-4">
                <fmt:message key="car.add.plate" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="rp"
                       type="text"
                       class="form-control"
                       value="<c:out value="${requestScope.attr_car.registrationPlate}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="l" class="control-label col-xs-4">
                <fmt:message key="car.move.location" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <select id="l"
                        class="form-control"
                        name="<fmt:message key="param.location.id" bundle="${par}"/>"
                        required>
                    <c:forEach items="${requestScope.attr_location_list}" var="location">
                        <option value="${location.id}">
                            <c:out value="${location.state}"/>,
                            <c:out value="${location.city}"/>,
                            <c:out value="${location.street}"/>,
                            <c:out value="${location.building}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       class="btn btn-default"
                       value="<fmt:message key="car.move.submit" bundle="${info}"/>">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="move_car">

        <input type="hidden"
               name="<fmt:message key="param.car.id" bundle="${par}"/>"
               value="${requestScope.attr_car.id}">
    </form>
</div>
</html>
