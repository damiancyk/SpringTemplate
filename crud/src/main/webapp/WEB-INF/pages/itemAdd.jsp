<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="box-title">
			<ol class="breadcrumb">
				<li><i class="fa fa-wrench"></i> <a href="itemView"> <fmt:message
							key="menu.items"></fmt:message>
				</a></li>
				<li class="active"><c:if test="${item.idItem!=null}">
						<fmt:message key="breadcrumb.edit" />
					</c:if> <c:if test="${item.idItem==null}">
						<fmt:message key="breadcrumb.add" />
					</c:if></li>
			</ol>

		</div>
	</div>

	<div class="panel-body">
		<form:form method="post" action="" commandName="item"
			class="form form-aligned">
			<form:hidden path="idItem" />

			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<fmt:message key="form.name" />

						<form:input path="name" class="form-control"
							cssErrorClass="form-control error" />
						<form:errors path="name" cssClass="error" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<button type="submit" class="btn btn-primary">
							<fmt:message key="form.save" />
						</button>
					</div>
				</div>
			</div>

		</form:form>
	</div>

</div>
