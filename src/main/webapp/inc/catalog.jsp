<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>
<form method="get" action="locations">
    <input type="submit" value="<fmt:message key="locations.href" bundle="${info}"/>">
</form>

<form method="get" action="cars">
    <input type="submit" value="<fmt:message key="cars.href" bundle="${info}"/>">
</form>

<form method="get" action="models">
    <input type="submit" value="<fmt:message key="models.href" bundle="${info}"/>">
</form>
