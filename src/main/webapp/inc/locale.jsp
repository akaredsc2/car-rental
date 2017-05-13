<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<form method="get" action="rental">
    <select id="lang"
            name="<fmt:message key="param.locale" bundle="${par}"/>"
            class="selectpicker"
            data-width="fit"
            required>
        <option value="en_US"
                data-content="<img width='20' height='20' src='<c:url value="/img/America.png"/>'>"
                <c:if test="${sessionScope.session_locale == 'en_US'}">
                    selected="selected"
                </c:if> >
        </option>
        <option value="ru_RU"
                data-content="<img width='20' height='20' src='<c:url value="/img/Russia.png"/>'>"
                <c:if test="${sessionScope.session_locale == 'ru_RU'}">
                    selected="selected"
                </c:if> >
        </option>
        <option value="uk_UA"
                data-content="<img width='20' height='20' src='<c:url value="/img/Ukraine.png"/>'>"
                <c:if test="${sessionScope.session_locale == 'uk_UA'}">
                    selected="selected"
                </c:if> >
        </option>
    </select>

    <input type="hidden"
           name="command"
           value="locale">

    <input type="submit"
           value="<fmt:message key="locale.change.submit" bundle="${info}"/>"
           class="btn btn-default">
</form>