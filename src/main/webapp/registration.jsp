<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="reg.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" class="form-horizontal">

        <div class="form-group">
            <label for="login" class="control-label col-xs-4">
                <fmt:message key="reg.login" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="login"
                       type="text"
                       name="<fmt:message key="param.user.login" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.login" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="[а-яА-ЯіІїЇєЄёЁ\w]{4,30}">
            </div>
        </div>

        <div class="form-group">
            <label for="password" class="control-label col-xs-4">
                <fmt:message key="reg.password" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="password"
                       type="password"
                       name="<fmt:message key="param.user.password" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.password" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="\w{4,30}">
            </div>
        </div>

        <div class="form-group">
            <label for="repeat" class="control-label col-xs-4">
                <fmt:message key="reg.repeatPassword" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="repeat"
                       type="password"
                       name="<fmt:message key="param.pass.repeat" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.repeatPassword" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="\w{4,30}">
            </div>
        </div>

        <div class="form-group">
            <label for="name" class="control-label col-xs-4">
                <fmt:message key="reg.name" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="name"
                       type="text"
                       name="<fmt:message key="param.user.name" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.name" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="([A-ZА-ЯІЇЄ][a-zа-яіїє]+)(\s([A-ZА-ЯІЇЄ][a-zа-яіїє]+))+"
                       minlength="4"
                       maxlength="60">
            </div>
        </div>

        <div class="form-group">
            <label for="birth" class="control-label col-xs-4">
                <fmt:message key="reg.birthday" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="birth"
                       type="date"
                       name="<fmt:message key="param.user.birthday" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.birthday" bundle="${info}"/>"
                       class="form-control"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="passport" class="control-label col-xs-4">
                <fmt:message key="reg.passport" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="passport"
                       type="text"
                       name="<fmt:message key="param.user.passport" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.passport" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="([А-Я]){2}\d{6}|\d{10}">
            </div>
        </div>

        <div class="form-group">
            <label for="driver" class="control-label col-xs-4">
                <fmt:message key="reg.driver" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="driver"
                       type="text"
                       name="<fmt:message key="param.user.driver" bundle="${par}"/>"
                       placeholder="<fmt:message key="reg.driver" bundle="${info}"/>"
                       class="form-control"
                       required
                       pattern="([А-Я]){3}\d{6}|\d{2}([А-Я]){2}\d{6}">
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       value="<fmt:message key="reg.submit" bundle="${info}"/>"
                       class="btn btn-default">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="registration">
    </form>
    <a href="<c:url value="/index.jsp"/>" class="btn btn-link col-xs-offset-4">
        <fmt:message key="signIn.href" bundle="${info}"/>
    </a>
</div>
</body>
</html>
