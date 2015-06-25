
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<nav id="header" class="navbar navbar-fixed-top" role="navigation">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="index">Index</a>
	</div>

	<ul class="nav navbar-right top-nav">
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown"><i class="fa fa-user"></i>${operator.email}
				<b class="caret"></b> </a>
			<ul class="dropdown-menu">
				<li class="divider"></li>
				<li><a href="logout"><i class="fa fa-fw fa-power-off"></i>
						Wyloguj</a>
				</li>
			</ul>
		</li>
	</ul>
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav side-nav">
			<li class="${menuactive=='item'?'active':''}"><a
				href="itemView"><i class="fa fa-fw fa-wrench"></i> <fmt:message
						key="menu.items"></fmt:message> </a>
			</li>
		</ul>
	</div>
</nav>
