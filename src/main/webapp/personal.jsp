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

<div class="center width-50">
    <form class="form-horizontal">
        <div class="form-group">
            <label for="l" class="control-label col-xs-4">
                <fmt:message key="personal.login" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="l"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.login}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="n" class="control-label col-xs-4">
                <fmt:message key="personal.name" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="n"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.fullName}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="b" class="control-label col-xs-4">
                <fmt:message key="personal.birthday" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="b"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.birthDate}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="p" class="control-label col-xs-4">
                <fmt:message key="personal.passport" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="p"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.passportNumber}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="d" class="control-label col-xs-4">
                <fmt:message key="personal.driver" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="d"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.driverLicenceNumber}"/>"
                       readonly>
            </div>
        </div>

        <div class="form-group">
            <label for="r" class="control-label col-xs-4">
                <fmt:message key="personal.role" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="r"
                       type="text"
                       class="form-control"
                       value="<c:out value="${sessionScope.session_user.role}"/>"
                       readonly>
            </div>
        </div>
    </form>

    <form method="post" action="rental" class="form-horizontal">
        <div class="form-group">
            <label for="old" class="control-label col-xs-4">
                <fmt:message key="personal.change.old" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="old"
                       type="password"
                       class="form-control"
                       name="<fmt:message key="param.pass.old" bundle="${par}"/>"
                       required
                       pattern="\w{4,10}">
            </div>
        </div>

        <div class="form-group">
            <label for="new" class="control-label col-xs-4">
                <fmt:message key="personal.change.new" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="new"
                       type="password"
                       class="form-control"
                       name="<fmt:message key="param.pass.new" bundle="${par}"/>"
                       required
                       pattern="\w{4,10}">
            </div>
        </div>

        <div class="form-group">
            <label for="repeat" class="control-label col-xs-4">
                <fmt:message key="personal.change.repeat" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="repeat"
                       type="password"
                       class="form-control"
                       name="<fmt:message key="param.pass.repeat" bundle="${par}"/>"
                       required
                       pattern="\w{4,10}">
            </div>
        </div>

        <input type="hidden"
               name="<fmt:message key="param.user.id" bundle="${par}"/>"
               value="${sessionScope.session_user.id}">

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input class="btn btn-default"
                       type="submit"
                       value="<fmt:message key="personal.change.submit" bundle="${info}"/>">
            </div>
        </div>
        <input type="hidden" name="command" value="change_password">
    </form>

    <c:if test="${sessionScope.session_user.role == 'ADMIN'}">
        <form action="rental" method="get" class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-offset-4 col-xs-6">
                    <input class="btn btn-default"
                           type="submit"
                           value="<fmt:message key="personal.promote.submit" bundle="${info}"/>">
                </div>
            </div>

            <input type="hidden"
                   name="command"
                   value="promote">
        </form>
    </c:if>
</div>
</body>
</html>
