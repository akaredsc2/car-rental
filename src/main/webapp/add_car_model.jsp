<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="model.add.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="add_model">
    <label>
        <fmt:message key="model.add.name" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.model.name" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="model.add.door" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.door" bundle="${par}"/>" min="1" max="100" step="1"
               required>
    </label><br>
    <label>
        <fmt:message key="model.add.seat" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.seat" bundle="${par}"/>" min="1" max="100" step="1"
               required>
    </label><br>
    <label>
        <fmt:message key="model.add.horse" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.model.horse" bundle="${par}"/>" min="1" max="3000" step="1"
               required>
    </label><br>

    <input type="submit" value="<fmt:message key="model.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="home.jsp"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
