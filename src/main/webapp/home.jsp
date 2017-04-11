<%--
  Created by IntelliJ IDEA.
  User: vitaly
  Date: 2017-04-11
  Time: 21:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
Hello, ${sessionScope.user.id}<br>
${sessionScope.user.login}<br>
${sessionScope.user.password}<br>
${sessionScope.user.fullName}<br>
${sessionScope.user.birthDate}<br>
${sessionScope.user.passportNumber}<br>
${sessionScope.user.driverLicenceNumber}<br>
${sessionScope.user.role}<br>
</body>
</html>
