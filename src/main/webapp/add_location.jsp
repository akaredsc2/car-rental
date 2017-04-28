<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="location.add.title" bundle="${info}"/></title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        <fmt:message key="location.add.state" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.state" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="location.add.city" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.city" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="location.add.street" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.street" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="location.add.building" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.location.building" bundle="${par}"/>" required>
    </label><br>

    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="add_location">
    <input type="submit" value="<fmt:message key="location.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="home.jsp"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>

