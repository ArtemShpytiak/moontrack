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
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="${pageContext.request.contextPath}">Moon<b>Track</b></a>
		</div>
		<%-- /.login-logo --%>
		<div class="login-box-body">
			<p class="login-box-msg">Sign in to start your session</p>

			<form action="login" method="post">
				<div class="form-group has-feedback">
					<input type="text" name="username" class="form-control" placeholder="Login">
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" name="password" class="form-control" placeholder="Password">
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label> <input type="checkbox"> Remember Me
							</label>
						</div>
					</div>
					<%-- /.col --%>
					<div class="col-xs-4">
						<button name="submit" value="submit" class="btn btn-primary btn-block btn-flat">
							Sign In
						</button>
					</div>
					<%-- /.col --%>
				</div>
			</form>

			<!-- 
			<a href="${pageContext.request.contextPath}/register"
				class="text-center">Register a new membership</a>
			 -->

		</div>
		<%-- /.login-box-body --%>
	</div>
	<%-- /.login-box --%>
	
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
