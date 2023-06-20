<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>MoonTrack | Log in</title>
<jsp:include page="/public/views/common/head.jsp" />

<%-- iCheck --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/iCheck/square/blue.css">
</head>
<body class="hold-transition register-page">
	<div class="register-box">
		<div class="register-logo">
			<a href="${pageContext.request.contextPath}">Moon<b>Track</b></a>
		</div>
		<%-- /.register-logo --%>
		<div class="register-box-body">
			<p class="register-box-msg">Register a new membership</p>

			<form action="register" method="post">
				<div class="form-group has-feedback">
					<input type="text" name="username" class="form-control" placeholder="Full name">
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="text" name="login" class="form-control" placeholder="Login">
					<span class="glyphicon glyphicon-tag form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="email" name="email" class="form-control" placeholder="Email">
					<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" name="password" class="form-control" placeholder="Password">
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" name="password-retype" class="form-control"
						placeholder="Retype password"> <span
						class="glyphicon glyphicon-log-in form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label>
								<input type="checkbox"> I agree to the <a href="#">terms</a>
							</label>
						</div>
					</div>
					<%-- /.col --%>
					<div class="col-xs-4">
						<button name="submit" value="submit" class="btn btn-primary btn-block btn-flat">
							Register
						</button>
					</div>
					<%-- /.col --%>
				</div>
			</form>
			<a href="login" class="text-center">I already have a membership</a>

		</div>
		<%-- /.register-box-body --%>
	</div>
	<%-- /.register-box --%>
	
	<jsp:include page="/public/views/common/js.jsp" />
	
	<%-- iCheck --%>
	<script
		src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/iCheck/icheck.min.js"></script>
	<script>
		$(function() {
			$('input').iCheck({
				checkboxClass : 'icheckbox_square-blue',
				radioClass : 'iradio_square-blue',
				increaseArea : '20%' /* optional */
			});
		});
	</script>
</body>
</html>
