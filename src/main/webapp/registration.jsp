<%--
  Created by IntelliJ IDEA.
  User: vitaly
  Date: 2017-04-27
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<form method="post" action="CarRental">
    <label>
        login
        <input type="text" name="param_user_login" required>
    </label>
    <label>
        password
        <input type="password" name="param_user_password" required>
    </label>
    <label>
        full name
        <input type="text" name="param_user_full_name" required>
    </label>
    <label>
        birth date
        <input type="datetime-local" name="param_user_birth_date" required>
    </label>
    <label>
        passport number
        <input type="text" name="param_user_passport_number" required>
    </label>
    <label>
        driver licence number
        <input type="text" name="param_user_driver_licence_number" required>
    </label>

    <input type="hidden" name="param_command" value="registration">
    <input type="submit">
</form>
<a href="index.jsp">index</a>
</body>
</html>
