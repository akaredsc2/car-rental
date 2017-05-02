<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<form method="get" action="locale">
    <label>
        <select name="<fmt:message key="param.locale" bundle="${par}"/>" required>
            <option value="en_US"
                    <c:if test="${sessionScope.session_locale == 'en_US'}">
                        selected="selected"
                    </c:if> >
                ENG
            </option>
            <option value="ru_RU"
                <c:if test="${sessionScope.session_locale == 'ru_RU'}">
                    selected="selected"
                </c:if> >
                RUS
            </option>
            <option value="uk_UA"
                <c:if test="${sessionScope.session_locale == 'uk_UA'}">
                    selected="selected"
                </c:if> >
                UKR
            </option>
        </select>
    </label>

    <input type="submit" value="<fmt:message key="locale.change.submit" bundle="${info}"/>">
</form>