<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:set var="currPage" value="${pageContext.request.servletPath}"/>

<div class="container width-75">
    <div class="col-xs-4">
        <form method="get" action="rental">
            <input type="hidden"
                   name="command"
                   value="locations">
            <input type="submit"
            <c:choose>
                <c:when test="${fn:contains(currPage, 'locations')}">
                   class="btn btn-primary"
                </c:when>
                <c:otherwise>
                   class="btn btn-link"
                </c:otherwise>
            </c:choose>
                   value="<fmt:message key="locations.href" bundle="${info}"/>">
        </form>
    </div>

    <div class="col-xs-4">
        <form method="get" action="rental">
            <input type="hidden"
                   name="command"
                   value="cars">
            <input type="submit"
            <c:choose>
                <c:when test="${fn:contains(currPage, 'cars')}">
                   class="btn btn-primary"
                </c:when>
                <c:otherwise>
                   class="btn btn-link"
                </c:otherwise>
            </c:choose>
                   value="<fmt:message key="cars.href" bundle="${info}"/>">
        </form>
    </div>

    <div class="col-xs-4">
        <form method="get" action="rental">
            <input type="hidden"
                   name="command"
                   value="models">
            <input type="submit"
            <c:choose>
                <c:when test="${fn:contains(currPage, 'models')}">
                   class="btn btn-primary"
                </c:when>
                <c:otherwise>
                   class="btn btn-link"
                </c:otherwise>
            </c:choose>
                   value="<fmt:message key="models.href" bundle="${info}"/>">
        </form>
    </div>
</div>