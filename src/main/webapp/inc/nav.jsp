<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}">
    <nav>
        <a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
        <form method="get" action="locations">
            <input type="submit" value="<fmt:message key="catalog.href" bundle="${info}"/>">
        </form>
        <form method="get" action="reservations">
            <input type="submit" value="<fmt:message key="reservations.href" bundle="${info}"/>">
        </form>
        <a href="<c:url value="/notifications.jsp"/>"><fmt:message key="notifications.href" bundle="${info}"/></a>
        <a href="<c:url value="/personal.jsp"/>"><fmt:message key="personal.href" bundle="${info}"/></a>
    </nav>
</c:if>

