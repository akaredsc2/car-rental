<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<form method="get" action="CarRental">
    <label>
        <select name="<fmt:message key="param.locale" bundle="${par}"/>" required>
            <option value="en_US">ENG</option>
            <option value="ru_RU">RUS</option>
            <option value="uk_UA">UKR</option>
        </select>
    </label>
    <input type="hidden" name="<fmt:message key="param.command" bundle="${par}"/>" value="change_locale">
    <input type="submit" value="<fmt:message key="locale.change.submit" bundle="${info}"/>">
</form>