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
    <title><fmt:message key="model.add.title" bundle="${info}"/></title>

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
                <fmt:message key="model.add.photo" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="ph"
                       type="file"
                       class="form-control"
                       name="<fmt:message key="param.model.photo" bundle="${par}"/>">
            </div>
        </div>

        <div class="form-group">
            <label for="n" class="control-label col-xs-4">
                <fmt:message key="model.add.name" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <div class="tip">
                    <span class="tiptext">
                        <fmt:message key="correct.format" bundle="${info}"/><br>
                        <fmt:message key="correct.model" bundle="${info}"/>
                    </span>

                    <input id="n"
                           type="text"
                           class="form-control"
                           name="<fmt:message key="param.model.name" bundle="${par}"/>"
                           pattern="<fmt:message key="correct.model" bundle="${info}"/>"
                           minlength="4"
                           maxlength="30"
                           required>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="d" class="control-label col-xs-4">
                <fmt:message key="model.add.door" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="d"
                       type="number"
                       class="form-control"
                       name="<fmt:message key="param.model.door" bundle="${par}"/>"
                       min="1"
                       max="10"
                       step="1"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="s" class="control-label col-xs-4">
                <fmt:message key="model.add.seat" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="s"
                       type="number"
                       class="form-control"
                       name="<fmt:message key="param.model.seat" bundle="${par}"/>"
                       min="1"
                       max="100"
                       step="1"
                       required>
            </div>
        </div>

        <div class="form-group">
            <label for="h" class="control-label col-xs-4">
                <fmt:message key="model.add.horse" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <input id="h" type="number"
                       name="<fmt:message key="param.model.horse" bundle="${par}"/>"
                       min="1"
                       class="form-control"
                       max="3000"
                       step="1"
                       required>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       class="btn btn-default"
                       value="<fmt:message key="model.add.submit"
               bundle="${info}"/>">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="add_model">
    </form>
</div>
</body>
</html>
