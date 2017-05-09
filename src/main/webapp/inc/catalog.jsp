<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>
<div class="row">

    <div class="col-xs-4">
        <form method="get" action="locations">
            <input class="btn btn-default" type="submit" value="<fmt:message key="locations.href" bundle="${info}"/>">
        </form>
    </div>

    <div class="col-xs-4">
        <form method="get" action="cars">
            <input class="btn btn-default" type="submit" value="<fmt:message key="cars.href" bundle="${info}"/>">
        </form>
    </div>


    <div class="col-xs-4">
        <form method="get" action="models">
            <input class="btn btn-default" type="submit" value="<fmt:message key="models.href" bundle="${info}"/>">
        </form>
    </div>

</div>
