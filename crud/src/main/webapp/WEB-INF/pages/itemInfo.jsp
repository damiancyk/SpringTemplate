<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="box-title">
			<ol class="breadcrumb">
				<li><i class="fa fa-wrench"></i> <a href="itemView"> <fmt:message
							key="menu.items"></fmt:message> </a></li>
				<li class="active"><fmt:message key="breadcrumb.info" />
				</li>
			</ol>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<table class="table">
					<tr>
						<td><fmt:message key="form.name" />
						</td>
						<td>${item.name}</td>
					</tr>
					<tr>
						<td><fmt:message key="form.createDate" />
						</td>
						<td><fmt:formatDate value="${item.createDate}" /></td>
					</tr>
				</table>
			</div>

		</div>
	</div>

</div>