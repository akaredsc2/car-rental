<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="models.title" bundle="${info}"/></title>
    <link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/custom.css'/>" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <a href="<c:url value="/pages/admin/add_model.jsp"/>"><fmt:message key="model.add.href" bundle="${info}"/></a><br>
</c:if>

<c:forEach items="${requestScope.attr_model_list}" var="model">

    <div class="row panel panel-default">
        <c:out value="${model.photoUrl}"/>

        <div class="form-group col-xs-4">
            <div class="row">
                    <div class="col-xs-4 text-right">
                    <fmt:message key="model.add.name" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${model.name}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="model.add.door" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${model.doorCount}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="model.add.seat" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${model.seatCount}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 text-right">
                    <fmt:message key="model.add.horse" bundle="${info}"/> :
                </div>

                <div class="col-xs-4">
                    <c:out value="${model.horsePowerCount}"/>
                </div>
            </div>
        </div>

        <form method="get" action="cars">
            <input type="hidden" name="<fmt:message key="param.model.id" bundle="${par}"/>" value="${model.id}">

            <div class="form-group col-xs-6">
                <input class="btn btn-default" type="submit" value="<fmt:message key="models.cars.href" bundle="${info}"/>">
            </div>
        </form>
    </div>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
