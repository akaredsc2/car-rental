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
    <div class="container debug-border width-75">
        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.id" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${bill.id}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.description" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${bill.description}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.amount" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                    <%--<c:out value="${bill.cashAmount}"/>--%>$
                <custom:number number="${bill.cashAmount}" locale="${sessionScope.session_locale}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.time" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${bill.creationDateTime}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.paid" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${bill.paid}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-4">
                <fmt:message key="bill.confirmed" bundle="${info}"/>
            </div>

            <div class="col-xs-4">
                <c:out value="${bill.confirmed}"/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-8"></div>
            <div class="col-xs-4">
                <c:if test="${sessionScope.session_user.role=='CLIENT' and not bill.paid}">
                    <form method="post" action="rental" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="pay">

                        <input type="hidden"
                               name="<fmt:message key="param.bill.id" bundle="${par}"/>"
                               value="${bill.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="bill.pay" bundle="${info}"/>">
                    </form>
                </c:if>

                <c:if test="${sessionScope.session_user.role=='ADMIN' and bill.paid and not bill.confirmed}">
                    <form method="post" action="rental" class="form-inline">
                        <input type="hidden"
                               name="command"
                               value="confirm">

                        <input type="hidden"
                               name="<fmt:message key="param.bill.id" bundle="${par}"/>"
                               value="${bill.id}">

                        <input type="submit"
                               class="btn btn-default"
                               value="<fmt:message key="bill.confirm" bundle="${info}"/>">
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</c:forEach>
</body>
</html>