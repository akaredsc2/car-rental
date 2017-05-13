<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<fmt:setBundle basename="parameters" var="par"/>

<fmt:setLocale value="${sessionScope.session_locale}"/>
<fmt:setBundle basename="info" var="info"/>

<c:if test="${empty sessionScope.session_user}">
    <c:redirect url="/pages/error/403.jsp"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="bills.title" bundle="${info}"/></title>

    <jsp:include page="/inc/css.jsp"/>
    <jsp:include page="/inc/js.jsp"/>
</head>
<body class="center width-75">
<jsp:include page="/inc/header.jsp"/>
<jsp:include page="/inc/nav.jsp"/>

<c:forEach items="${requestScope.attr_bill_list}" var="bill">
    <fmt:message key="bill.id" bundle="${info}"/> : <c:out value="${bill.id}"/><br>
    <fmt:message key="bill.description" bundle="${info}"/> : <c:out value="${bill.description}"/><br>
    <fmt:message key="bill.amount" bundle="${info}"/> : <%--<c:out value="${bill.cashAmount}"/>--%>
    <custom:number number="${bill.cashAmount}" locale="${sessionScope.session_locale}"/><br>
    <fmt:message key="bill.time" bundle="${info}"/> : <c:out value="${bill.creationDateTime}"/><br>
    <fmt:message key="bill.paid" bundle="${info}"/> : <c:out value="${bill.paid}"/><br>
    <fmt:message key="bill.confirmed" bundle="${info}"/> : <c:out value="${bill.confirmed}"/><br>

    <c:if test="${sessionScope.session_user.role=='CLIENT' and not bill.paid}">
        <form method="post" action="rental">
            <input type="hidden" name="command" value="pay">
            <input type="hidden" name="<fmt:message key="param.bill.id" bundle="${par}"/>" value="${bill.id}">
            <input type="submit" value="<fmt:message key="bill.pay" bundle="${info}"/>">
        </form>
    </c:if>

    <c:if test="${sessionScope.session_user.role=='ADMIN' and bill.paid and not bill.confirmed}">
        <form method="post" action="rental">
            <input type="hidden" name="command" value="confirm">
            <input type="hidden" name="<fmt:message key="param.bill.id" bundle="${par}"/>" value="${bill.id}">
            <input type="submit" value="<fmt:message key="bill.confirm" bundle="${info}"/>">
        </form>
    </c:if>

    <form method="get" action="rental">
        <input type="hidden" name="command" value="reservations">
        <input type="submit" value="<fmt:message key="reservations.href" bundle="${info}"/>">
    </form>
</c:forEach>
</body>
</html>