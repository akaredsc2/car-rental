<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="locations.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-8"></div>
            <div class="col-xs-4">
                <a href="<c:url value="/pages/admin/add_location.jsp"/>" class="btn btn-default">
                    <fmt:message key="location.add.href" bundle="${info}"/>
                </a>
            </div>
        </div>
    </div>
</c:if>

<c:forEach items="${requestScope.attr_location_list}" var="location">
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-4">
                <img class="img-thumbnail" src="<c:out value="${location.photoUrl}"/>">
            </div>

            <div class="col-xs-8">
                <div class="col-xs-6">
                    <fmt:message key="location.add.state" bundle="${info}"/>
                </div>

                <div class="col-xs-6">
                    <c:out value="${location.state}"/>
                </div>

                <div class="col-xs-6">
                    <fmt:message key="location.add.city" bundle="${info}"/>
                </div>

                <div class="col-xs-6">
                    <c:out value="${location.city}"/>
                </div>

                <div class="col-xs-6">
                    <fmt:message key="location.add.street" bundle="${info}"/>
                </div>

                <div class="col-xs-6">
                    <c:out value="${location.street}"/>
                </div>

                <div class="col-xs-6">
                    <fmt:message key="location.add.building" bundle="${info}"/>
                </div>

                <div class="col-xs-6">
                    <c:out value="${location.building}"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-8">
                <c:if test="${sessionScope.session_user.role=='ADMIN'}">
                    <form method="post" action="rental" enctype="multipart/form-data" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="update_location">

                        <div class="form-group">
                            <label for="photo">
                                <fmt:message key="location.add.photo" bundle="${info}"/>
                            </label>

                            <input id="photo"
                                   type="file"
                                   class="form-control"
                                   name="<fmt:message key="param.location.photo" bundle="${par}"/>">
                        </div>

                        <input type="hidden"
                               name="<fmt:message key="param.location.id" bundle="${par}"/>"
                               value="${location.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="locations.update.href" bundle="${info}"/>">
                    </form>
                </c:if>
            </div>

            <div class="col-xs-4">
                <form method="get" action="rental">
                    <input type="hidden"
                           name="command"
                           value="cars">

                    <input type="hidden"
                           name="<fmt:message key="param.location.id" bundle="${par}"/>"
                           value="${location.id}">

                    <input type="submit"
                           class="btn btn-default"
                           value="<fmt:message key="locations.cars.href" bundle="${info}"/>">
                </form>
            </div>
        </div>
    </div>
</c:forEach>
</body>
</html>
