
<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras"
	prefix="tilesx"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>crud</title>
<link rel="shortcut icon" href="resources/img/favicon.ico" />

<script src="resources/js/jquery/jquery-1.11.3.min.js"></script>
<script src="resources/js/angular/angular.min.js"></script>
<script src="resources/js/angular/angular-resource.min.js"></script>
<script src="resources/js/angular/app/app.js"></script>
<script src="resources/js/angular/app/table.js"></script>

<link href="resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
</head>

<body>
	<%--
	<jsp:include page="globalMessages.jsp" />
 --%>

	<div id="wrapper">
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<tiles:insertAttribute name="body" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
