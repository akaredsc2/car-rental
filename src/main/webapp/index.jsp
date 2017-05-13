<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>
<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}">
    <c:redirect url="/home.jsp"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="signIn.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" class="form-horizontal">
        <div class="form-group">
            <label for="login" class="control-label col-xs-4">
                <fmt:message key="signIn.login" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <div class="tip">
                    <span class="tiptext">
                        <fmt:message key="correct.format" bundle="${info}"/><br>
                        <fmt:message key="correct.login" bundle="${info}"/>
                    </span>

                    <input id="login"
                           type="text"
                           class="form-control"
                           name="<fmt:message key="param.user.login" bundle="${par}"/>"
                           placeholder="<fmt:message key="signIn.login" bundle="${info}"/>"
                           required
                           pattern="<fmt:message key="correct.login" bundle="${info}"/>">
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="pass" class="control-label col-xs-4">
                <fmt:message key="signIn.password" bundle="${info}"/>
            </label>

            <div class="col-xs-6">
                <div class="tip">
                    <span class="tiptext">
                        <fmt:message key="correct.format" bundle="${info}"/><br>
                        <fmt:message key="correct.password" bundle="${info}"/>
                    </span>

                    <input id="pass"
                           type="password"
                           class="form-control"
                           name="<fmt:message key="param.user.password" bundle="${par}"/>"
                           placeholder="<fmt:message key="signIn.password" bundle="${info}"/>"
                           required
                           pattern="<fmt:message key="correct.password" bundle="${info}"/>">
                </div>
            </div>
        </div>


        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       value="<fmt:message key="signIn.submit" bundle="${info}"/>"
                       class="btn btn-default">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="sign_in">
    </form>

    <a href="<c:url value="/registration.jsp"/>" class="btn btn-link col-xs-offset-4">
        <fmt:message key="reg.href" bundle="${info}"/>
    </a>
</div>
</body>
</html>
