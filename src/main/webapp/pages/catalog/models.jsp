<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="models.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<jsp:include page="/inc/catalog.jsp"/>

<c:if test="${sessionScope.session_user.role=='ADMIN'}">
    <a href="<c:url value="/pages/admin/add_model.jsp"/>"><fmt:message key="model.add.href" bundle="${info}"/></a><br>
</c:if>

<c:forEach items="${requestScope.attr_model_list}" var="model">
    <c:out value="${model.name}"/>
    <c:out value="${model.photoUrl}"/>
    <c:out value="${model.doorCount}"/>
    <c:out value="${model.seatCount}"/>
    <c:out value="${model.horsePowerCount}"/><br>
    <form method="get" action="controller/cars">
        <input type="hidden" name="<fmt:message key="param.model.id" bundle="${par}"/>" value="${model.id}">
        <input type="submit" value="<fmt:message key="models.cars.href" bundle="${info}"/>">
    </form>
</c:forEach>

<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
