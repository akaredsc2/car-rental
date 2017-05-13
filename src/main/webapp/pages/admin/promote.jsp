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
    <title><fmt:message key="promote.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<div class="center width-50">
    <form method="post" action="rental" class="form-horizontal">
        <div class="form-group">
            <label for="u" class="control-label col-xs-4">
                <fmt:message key="promote.client" bundle="${info}"/>
            </label>


            <div class="col-xs-6">
                <select id="u"
                        class="form-control"
                        name="<fmt:message key="param.user.id" bundle="${par}"/>"
                        required>
                    <c:forEach items="${requestScope.attr_user_list}" var="client">
                        <option value="${client.id}">
                            <c:out value="${client.login}"/>,
                            <c:out value="${client.fullName}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-offset-4 col-xs-6">
                <input type="submit"
                       class="btn btn-default"
                       value="<fmt:message key="promote.submit" bundle="${info}"/>">
            </div>
        </div>

        <input type="hidden"
               name="command"
               value="promote">
    </form>
</div>
</body>
</html>
