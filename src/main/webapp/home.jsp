<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="home.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="inc/locale.jsp"/>
<jsp:include page="inc/welcome.jsp"/>
<a href="add_car_model.jsp"><fmt:message key="model.add.href" bundle="${info}"/></a><br>
<a href="add_location.jsp"><fmt:message key="location.add.href" bundle="${info}"/></a><br>
<form method="get" action="add_car">
    <input type="submit" value="<fmt:message key="car.add.href" bundle="${info}"/>">
</form>
</body>
</html>
