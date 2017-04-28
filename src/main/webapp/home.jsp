<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="uk_UA"/>
<fmt:setBundle basename="info" var="info"/>

<html>
<head>
    <title><fmt:message key="home.title" bundle="${info}"/></title>
</head>
<body>
<c:import url="inc/welcome.jsp"/>
</body>
</html>
