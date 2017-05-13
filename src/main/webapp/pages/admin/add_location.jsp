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
    <title><fmt:message key="location.add.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" enctype="multipart/form-data" class="form-horizontal">
        <div class="form-group">
            <label for="ph" class="control-label col-xs-4">
                <fmt:message key="location.add.photo" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="ph"
                       type="file"
                       class="form-control"
                       name="<fmt:message key="param.location.photo" bundle="${par}"/>">
            </div>
        </div>

        <div class="form-group">
            <label for="st" class="control-label col-xs-4">
                <fmt:message key="location.add.state" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="st"
                       type="text" name="<fmt:message key="param.location.state" bundle="${par}"/>"
                       class="form-control"
                       required
                       pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                       minlength="4"
                       maxlength="30">
            </div>
        </div>

        <div class="form-group">
            <label for="c" class="control-label col-xs-4">
                <fmt:message key="location.add.city" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="c"
                       type="text"
                       name="<fmt:message key="param.location.city" bundle="${par}"/>"
                       required
                       class="form-control"
                       pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                       minlength="4"
                       maxlength="30">
            </div>
        </div>

        <div class="form-group">
            <label for="str" class="control-label col-xs-4">
                <fmt:message key="location.add.street" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="str"
                       type="text"
                       name="<fmt:message key="param.location.street" bundle="${par}"/>"
                       required
                       class="form-control"
                       pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                       minlength="4"
                       maxlength="30">
            </div>
        </div>

        <div class="form-group">
            <label for="b" class="control-label col-xs-4">
                <fmt:message key="location.add.building" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="b"
                       type="text"
                       name="<fmt:message key="param.location.building" bundle="${par}"/>"
                       required
                       class="form-control"
                       pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
                       minlength="4"
                       maxlength="30">
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       class="btn btn-default"
                       value="<fmt:message key="location.add.submit" bundle="${info}"/>">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="add_location">
    </form>
</div>
</body>
</html>
