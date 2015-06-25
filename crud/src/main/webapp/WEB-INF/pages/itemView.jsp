<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="row-fluid">
	<div class="box col-md-12">
		<div class="panel-heading">
			<ol class="breadcrumb">
				<li><i class="fa fa-stethoscope"></i> <a href="itemView"> <fmt:message
							key="menu.items"></fmt:message> </a>
				</li>
				<li class="active"><fmt:message key="breadcrumb.view" /></li>
			</ol>
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<ul class="nav navbar-nav">
						<li><a href="itemAdd"><i class="fa fa-plus-square-o"></i>
								<fmt:message key="menu.add" /> </a>
						</li>
						<li><a href="javascript:url('itemEdit-||ID||')"><i
								class="fa fa-pencil-square-o"></i> <fmt:message key="menu.edit" />
						</a>
						</li>
						<li><a href="javascript:url('itemInfo-||ID||')"><i
								class="fa fa-info"></i> <fmt:message key="menu.info" /></a>
						</li>
						<li><a href="javascript:urlConfirm('itemDelete-||ID||')"><i
								class="fa fa-trash-o"></i> <fmt:message key="menu.delete" /></a>
						</li>
					</ul>
				</div>
			</nav>
		</div>

		<div class="panel-body">
			<div ng-app="app">
				<div ng-controller="TableCtrl">
					<table ng-table="tableParams" show-filter="true"
						class="table table-striped table-hover ng-table-responsive"
						export-csv="csv" data-url='itemView.json'>
						<tr ng-repeat="row in $data" table-row>
							<td header="'ng-table/headers/checkbox.html'" class="check"><input
								type="checkbox" class="check"
								data-id-position="{{row.idItem}}"></td>
							<td data-title="'Nazwa'" sortable="'name'"
								filter="{ 'name': 'text' }" ng-bind-html="renderHtml(row.nameDecorated)"></td>
							<td data-title="'Data utworzenia'" sortable="'createDate'"
								filter="{ 'createDate': 'text' }"
								ng-bind-html="renderHtml(row.createDateDecorated)"></td>
						</tr>
					</table>
					<jsp:include page="checkAll.jsp" />
				</div>
			</div>

		</div>
	</div>


</div>
