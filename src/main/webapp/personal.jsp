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
    <title><fmt:message key="personal.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.login" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.login}"/>
    </div>
</div>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.name" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.fullName}"/>
    </div>
</div>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.birthday" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.birthDate}"/>
    </div>
</div>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.passport" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.passportNumber}"/>
    </div>
</div>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.driver" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.driverLicenceNumber}"/>
    </div>
</div>

<div class="row">
    <div class="col-sm-2 text-right">
        <fmt:message key="personal.role" bundle="${info}"/> :
    </div>

    <div class="col-sm-10">
        <c:out value="${sessionScope.session_user.role}"/>
    </div>
</div>

<form method="post" action="rental">
    <div class="form-group row">
        <label for="old" class="col-sm-2 col-form-label text-right">
            <fmt:message key="personal.change.old" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="old" type="password" name="<fmt:message key="param.pass.old" bundle="${par}"/>" required
                   pattern="\w{4,10}">
        </div>
    </div>

    <div class="form-group row">
        <label for="new" class="col-sm-2 col-form-label text-right">
            <fmt:message key="personal.change.new" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="new" type="password" name="<fmt:message key="param.pass.new" bundle="${par}"/>" required
                   pattern="\w{4,10}">
        </div>
    </div>

    <div class="form-group row">
        <label for="repeat" class="col-sm-2 col-form-label text-right">
            <fmt:message key="personal.change.repeat" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="repeat" type="password" name="<fmt:message key="param.pass.repeat" bundle="${par}"/>" required
                   pattern="\w{4,10}">
        </div>
    </div>

    <input type="hidden" name="<fmt:message key="param.user.id" bundle="${par}"/>"
           value="${sessionScope.session_user.id}">

    <div class="form-group row">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-default" type="submit"
                   value="<fmt:message key="personal.change.submit" bundle="${info}"/>">
        </div>
    </div>
    <input type="hidden" name="command" value="change_password">
</form>

<c:if test="${sessionScope.session_user.role == 'ADMIN'}">
    <form action="rental" method="get">
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-10">
                <input class="btn btn-default" type="submit"
                       value="<fmt:message key="personal.promote.submit" bundle="${info}"/>">
            </div>
        </div>
        <input type="hidden" name="command" value="promote">
    </form>
</c:if>
</body>
</html>
