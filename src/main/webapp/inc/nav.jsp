<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:set var="currPage" value="${pageContext.request.servletPath}"/>

<c:if test="${not empty sessionScope.session_user}">
    <nav class="container">
        <div class="col-xs-3">
            <a href="<c:url value="/home.jsp"/>"
                    <c:choose>
                        <c:when test="${fn:contains(currPage, 'home')}">
                            class="btn btn-primary"
                        </c:when>
                        <c:otherwise>
                            class="btn btn-link"
                        </c:otherwise>
                    </c:choose>
            >
                <fmt:message key="home.href" bundle="${info}"/>
            </a>
        </div>

        <div class="col-xs-3">
            <form method="get" action="rental">
                <input type="hidden"
                       name="command"
                       value="locations">

                <input type="submit"
                <c:choose>
                    <c:when test="${fn:contains(currPage, 'catalog')}">
                       class="btn btn-primary"
                    </c:when>
                    <c:otherwise>
                       class="btn btn-link"
                    </c:otherwise>
                </c:choose>
                       value="<fmt:message key="catalog.href" bundle="${info}"/>">
            </form>
        </div>

        <div class="col-xs-3">
            <form method="get" action="rental">
                <input type="hidden"
                       name="command"
                       value="reservations">

                <input type="submit"
                <c:choose>
                    <c:when test="${fn:contains(currPage, 'reservations')}">
                       class="btn btn-primary"
                    </c:when>
                    <c:otherwise>
                       class="btn btn-link"
                    </c:otherwise>
                </c:choose>
                       value="<fmt:message key="reservations.href" bundle="${info}"/>">
            </form>
        </div>

        <div class="col-xs-3">
            <a href="<c:url value="/personal.jsp"/>"
                    <c:choose>
                        <c:when test="${fn:contains(currPage, 'personal')}">
                            class="btn btn-primary"
                        </c:when>
                        <c:otherwise>
                            class="btn btn-link"
                        </c:otherwise>
                    </c:choose>
            >
                <fmt:message key="personal.href" bundle="${info}"/>
            </a>
        </div>
    </nav>
</c:if>

