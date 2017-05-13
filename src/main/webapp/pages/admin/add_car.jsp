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
    <title><fmt:message key="car.add.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" class="form-horizontal">
        <div class="form-group">
            <label for="m" class="control-label col-xs-4">
                <fmt:message key="car.add.model" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <select id="m"
                        class="form-control"
                        name="<fmt:message key="param.car.model" bundle="${par}"/>"
                        required>
                    <c:forEach items="${requestScope.attr_model_list}" var="model">
                        <option value="${model.id}">${model.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label for="p" class="control-label col-xs-4">
                <fmt:message key="car.add.plate" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="p"
                       type="text"
                       name="<fmt:message key="param.car.plate" bundle="${par}"/>"
                       class="form-control"
                       pattern="[А-Я]{2}\d{4}[А-Я]{2}"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="c" class="control-label col-xs-4">
                <fmt:message key="car.add.color" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="c"
                       type="text"
                       name="<fmt:message key="param.car.color" bundle="${par}"/>"
                       class="form-control"
                       pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                       minlength="3"
                       maxlength="30"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="pr" class="control-label col-xs-4">
                <fmt:message key="car.add.price" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="pr" type="number"
                       name="<fmt:message key="param.car.price" bundle="${par}"/>"
                       class="form-control"
                       min="0.01"
                       max="1000"
                       step="0.01"
                       required>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       class="btn btn-default"
                       value="<fmt:message key="car.add.submit" bundle="${info}"/>">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="add_car">
    </form>
</div>
</body>
</html>
