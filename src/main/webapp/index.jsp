<%--
  Created by IntelliJ IDEA.
  User: vitaly
  Date: 2017-04-11
  Time: 21:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        Login : <input type="text" name="user_login" required>
    </label>
    <label>
        Password : <input type="password" name="user_password" required>
    </label>
    <input type="hidden" name="command" value="log_in">
    <input type="submit">
</form>
</body>
</html>
