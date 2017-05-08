<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<form method="get" action="controller/locale" class="form-inline">
    <div class="form-group row">
        <div class="col-sm-2">
            <select id="lang" name="<fmt:message key="param.locale" bundle="${par}"/>" required class="form-control ">
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
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input class="btn btn-default" type="submit"
                   value="<fmt:message key="locale.change.submit" bundle="${info}"/>">
        </div>
    </div>
</form>