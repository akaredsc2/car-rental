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
Hello,
${sessionScope.session_attr_user.id}<br>
${sessionScope.session_attr_user.login}<br>
${sessionScope.session_attr_user.password}<br>
${sessionScope.session_attr_user.fullName}<br>
${sessionScope.session_attr_user.birthDate}<br>
${sessionScope.session_attr_user.passportNumber}<br>
${sessionScope.session_attr_user.driverLicenceNumber}<br>
${sessionScope.session_attr_user.role}<br>
</body>
</html>
