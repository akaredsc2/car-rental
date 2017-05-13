<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<html>
<head>
    <title><fmt:message key="locations.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <a href="<c:url value="/pages/admin/add_location.jsp"/>"><fmt:message key="location.add.href" bundle="${info}"/></a><br>
</c:if>

<c:forEach items="${requestScope.attr_location_list}" var="location">
    <div class="row panel panel-default">
        <address class="form-group col-xs-4">

            <img src="<c:out value="${location.photoUrl}"/>" width="100" height="100">

            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="location.add.state" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${location.state}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="location.add.city" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${location.city}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="location.add.street" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${location.street}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="location.add.building" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${location.building}"/>
                </div>
            </div>
        </address>

        <form method="get" action="rental">
            <input type="hidden" name="command" value="cars">
            <input type="hidden" name="<fmt:message key="param.location.id" bundle="${par}"/>" value="${location.id}">
            <div class="form-group col-xs-6">
                <input class="btn btn-default" type="submit"
                       value="<fmt:message key="locations.cars.href" bundle="${info}"/>">
            </div>
        </form>

        <form method="post" action="rental" enctype="multipart/form-data">
            <input type="hidden" name="command" value="update_location">
            <input type="file" name="<fmt:message key="param.location.photo" bundle="${par}"/>">
            <input type="hidden" name="<fmt:message key="param.location.id" bundle="${par}"/>" value="${location.id}">
            <div class="form-group col-xs-6">
                <input class="btn btn-default" type="submit"
                       value="<fmt:message key="locations.update.href" bundle="${info}"/>">
            </div>
        </form>
    </div>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
