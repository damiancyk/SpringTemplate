<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12" style="padding:100px;">
	<div class="col-md-3"></div>
	<div class="col-md-6">
		<div class="well">
			<div class="icons">
				<a href="index"><i class="halflings-icon home"></i> </a> <a href="#"><i
					class="halflings-icon cog"></i> </a>
			</div>

			<c:if test="${errorLogin!=null }">
				<div class="alert alert-error"
					style="margin-left: 10px; margin-right: 10px">Wystąpił bład
					podczas logowania...</div>

			</c:if>

			<c:if test="${logoutSuccess!=null }">
				<div class="alert alert-success"
					style="margin-left: 10px; margin-right: 10px">Poprawnie
					wylogowano!</div>
			</c:if>

			<c:if test="${success!=null }">
				<div class="alert alert-success"
					style="margin-left: 10px; margin-right: 10px">${success}</div>
			</c:if>

			<h2>Zaloguj się na swoje konto</h2>
			<form class="form-horizontal" action="j_spring_security_check"
				method="post">
				<div class="form-group">
					<span class="add-on"><i class="halflings-icon user"></i> </span>
					<div class="controls">
						<input class="form-control span10" name="j_username" id="username"
							type="text" placeholder="e-mail" value="admin" />
					</div>
				</div>

				<div class="form-group">
					<span class="add-on"><i class="halflings-icon lock"></i> </span>
					<div class="controls">
						<input class="form-control span10" name="j_password" id="password"
							type="password" placeholder='hasło' value="admin" />
					</div>
				</div>

				<div class="form-group">
					<label class="remember" for="remember_me"><input
						type="checkbox" id="remember_me" class="check"
						name="_spring_security_remember_me" />Pamiętaj mnie</label>
				</div>

				<div class="form-group">
					<div class="pull-left">
						<button type="submit" class="btn btn-primary">Login</button>
					</div>
					<div class="pull-right">
						<a href="reminderPassword">Zapomniałem hasła</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
