<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}" >
    <c:redirect url="home.jsp"/>
</c:if>
<html>
<head>
    <title><fmt:message key="signIn.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<form method="post" action="sign_in">
    <label>
        <fmt:message key="signIn.login" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.user.login" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="signIn.password" bundle="${info}"/>
        <input type="password" name="<fmt:message key="param.user.password" bundle="${par}"/>" required>
    </label><br>

    <input type="submit" value="<fmt:message key="signIn.submit" bundle="${info}"/>">
</form>
<a href="<c:url value="/registration.jsp"/>"><fmt:message key="reg.href" bundle="${info}"/></a>
</body>
</html>
