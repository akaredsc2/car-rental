<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${!empty sessionScope.session_user}">
    <form method="post" action="controller/sign_out" class="form-inline">
        <fmt:message key="welcome.greetings" bundle="${info}"/>
        <c:out value="${sessionScope.session_user.fullName}"/> !

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input class="btn btn-default" type="submit" value="<fmt:message key="welcome.leave" bundle="${info}"/>"/>
            </div>
        </div>
    </form>
</c:if>
