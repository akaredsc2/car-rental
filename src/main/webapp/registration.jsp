<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="reg.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<form method="post" action="controller/registration">
    <div class="form-group row">
        <label for="login" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.login" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="login" type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
        </div>
    </div>

    <div class="form-group row">
        <label for="password" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.password" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="password" type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>"
                   required>
        </div>
    </div>

    <div class="form-group row">
        <label for="repeat" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.repeatPassword" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="repeat" type="password" name="<fmt:message key="param.pass.repeat" bundle="${par}"/>"
                   required>
        </div>
    </div>

    <div class="form-group row">
        <label for="name" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.name" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="name" type="text" name="<fmt:message key="param.user.name" bundle="${par}"/>" required>
        </div>
    </div>

    <div class="form-group row">
        <label for="birth" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.birthday" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="birth" type="datetime-local" name="<fmt:message key="param.user.birthday" bundle="${par}"/>"
                   required>
        </div>
    </div>

    <div class="form-group row">
        <label for="passport" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.passport" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="passport" type="text" name="<fmt:message key="param.user.passport" bundle="${par}"/>" required>
        </div>
    </div>

    <div class="form-group row">
        <label for="driver" class="col-sm-2 col-form-label text-right">
            <fmt:message key="reg.driver" bundle="${info}"/>
        </label>

        <div class="col-sm-10">
            <input id="driver" type="text" name="<fmt:message key="param.user.driver" bundle="${par}"/>" required>
        </div>
    </div>

    <div class="form-group row">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-default" type="submit" value="<fmt:message key="reg.submit" bundle="${info}"/>">
        </div>
    </div>
</form>
<a class="col-sm-offset-2 col-sm-10" href="<c:url value="/index.jsp"/>"><fmt:message key="signIn.href"
                                                                                     bundle="${info}"/></a>
</body>
</html>
