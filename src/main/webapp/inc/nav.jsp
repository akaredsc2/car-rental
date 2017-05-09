<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${not empty sessionScope.session_user}">
    <nav class="btn-group btn-group-justified">
        <div class="row">
            <div class="col-xs-3">
                <a class="btn btn-default" href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
            </div>

            <div class="col-xs-3">
                <form method="get" action="locations">
                    <input class="btn btn-default" type="submit" value="<fmt:message key="catalog.href" bundle="${info}"/>">
                </form>
            </div>

            <div class="col-xs-3">
                <form method="get" action="reservations">
                    <input class="btn btn-default" type="submit" value="<fmt:message key="reservations.href" bundle="${info}"/>">
                </form>
            </div>

            <div class="col-xs-3">
                <a class="btn btn-default" href="<c:url value="/personal.jsp"/>"><fmt:message key="personal.href" bundle="${info}"/></a>
            </div>
        </div>
    </nav>
</c:if>

