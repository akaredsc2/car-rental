<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}">
    <c:redirect url="home.jsp"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="signIn.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<form method="post" action="sign_in" class="form-horizontal custom-top">
    <div class="form-group row">
        <label for="login" class="col-sm-2 col-form-label text-right">
            <fmt:message key="signIn.login" bundle="${info}"/>
        </label>
        <div class="col-sm-10">
            <input id="login" type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required
                   pattern="[а-яА-ЯіІїЇєЄёЁ\w]{4,45}">
        </div>
    </div>

    <div class="form-group row">
        <label for="pass" class="col-sm-2 col-form-label text-right">
            <fmt:message key="signIn.password" bundle="${info}"/>
        </label>
        <div class="col-sm-10">
            <input id="pass" type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required
                   pattern="\w{4,10}">
        </div>
    </div>

    <div class="form-group row">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-default" type="submit" value="<fmt:message key="signIn.submit" bundle="${info}"/>">
        </div>
    </div>
</form>
<a class="col-sm-offset-2 col-sm-10" href="<c:url value="/registration.jsp"/>"><fmt:message key="reg.href"
                                                                                            bundle="${info}"/></a>
</body>
</html>
