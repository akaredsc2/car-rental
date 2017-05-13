<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user or sessionScope.session_user.role != 'ADMIN'}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="car.add.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="rental">
    <label>
        <fmt:message key="car.add.model" bundle="${info}"/>
        <select name="<fmt:message key="param.car.model" bundle="${par}"/>" required>
            <c:forEach items="${requestScope.attr_model_list}" var="model">
                <option value="${model.id}">${model.name}</option>
            </c:forEach>
        </select>
    </label><br>
    <label>
        <fmt:message key="car.add.plate" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.car.plate" bundle="${par}"/>"
               pattern="[А-Я]{2}\d{4}[А-Я]{2}"
               required>
    </label><br>
    <label>
        <fmt:message key="car.add.color" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.car.color" bundle="${par}"/>"
               pattern="[a-zA-Zа-яА-ЯіІїЇєЄ\d]+(\s+[a-zA-Zа-яА-ЯіІїЇєЄ\d]+)*"
               minlength="3"
               maxlength="30"
               required>
    </label><br>
    <label>
        <fmt:message key="car.add.price" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.car.price" bundle="${par}"/>"
               min="0.01" max="1000" step="0.01"
               required>
    </label><br>

    <input type="hidden" name="command" value="add_car">
    <input type="submit" value="<fmt:message key="car.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
