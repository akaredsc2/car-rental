<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="promote.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="promote">
    <label>
        <fmt:message key="promote.client" bundle="${info}"/>
        <select name="<fmt:message key="param.user.id" bundle="${par}"/>" required>
            <c:forEach items="${requestScope.attr_user_list}" var="client">
                <option value="${client.id}">
                    <c:out value="${client.login}"/>
                    <c:out value="${client.fullName}"/>
                </option>
            </c:forEach>
        </select>
    </label><br>

    <input type="submit" value="<fmt:message key="promote.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
