<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="car.add.title" bundle="${info}"/></title>
</head>
<body>
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>
<form method="post" action="add_car">
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
        <input type="text" name="<fmt:message key="param.car.plate" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="car.add.color" bundle="${info}"/>
        <input type="text" name="<fmt:message key="param.car.color" bundle="${par}"/>" required>
    </label><br>
    <label>
        <fmt:message key="car.add.price" bundle="${info}"/>
        <input type="number" name="<fmt:message key="param.car.price" bundle="${par}"/>" min="1" max="1000" step="0.01"
               required>
    </label><br>

    <input type="submit" value="<fmt:message key="car.add.submit" bundle="${info}"/>">
</form>
<br>
<a href="<c:url value="/home.jsp"/>"><fmt:message key="home.href" bundle="${info}"/></a>
</body>
</html>
