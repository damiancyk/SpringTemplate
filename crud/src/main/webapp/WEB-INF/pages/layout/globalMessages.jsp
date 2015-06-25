<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>

<%--komunikaty --%>
<c:if test="${success!=null}">
	<c:set var="successMsg">
		<fmt:message key="${success}"></fmt:message>
	</c:set>
</c:if>
<c:if test="${error!=null}">
	<c:set var="errorMsg">
		<fmt:message key="${error}"></fmt:message>
	</c:set>
</c:if>
<c:if test="${warning!=null}">
	<c:set var="warningMsg">
		<fmt:message key="${warning}"></fmt:message>
	</c:set>
</c:if>
<c:if test="${info!=null}">
	<c:set var="infoMsg">
		<fmt:message key="${info}"></fmt:message>
	</c:set>
</c:if>
<input id="idInputSuccessMessage" type="hidden" value="${successMsg}" />
<input id="idInputErrorMessage" type="hidden" value="${errorMsg}" />
<input id="idInputWarningMessage" type="hidden" value="${warningMsg}" />
<input id="idInputInfoMessage" type="hidden" value="${infoMsg}" />
