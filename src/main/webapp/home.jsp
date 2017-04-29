<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="home.title" bundle="${info}"/></title>
</head>
<body>
<c:import url="inc/welcome.jsp"/>
<a href="add_car_model.jsp"><fmt:message key="model.add.href" bundle="${info}"/></a><br>
<a href="add_location.jsp"><fmt:message key="location.add.href" bundle="${info}"/></a><br>
<form method="get" action="CarRental">
    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="prepare_add_car">
    <input type="submit" value="<fmt:message key="car.add.href" bundle="${info}"/>">
</form>
</body>
</html>
