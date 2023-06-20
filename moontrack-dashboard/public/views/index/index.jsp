<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html class="gr__adminlte_io" style="height: auto; min-height: 100%;">
	<head>
		<title>MoonTrack</title>
		<link
			rel="icon"
			type="image/png"
			href="${pageContext.request.contextPath}/public/img/favicon.png"
		/>

		<!-- Tell the browser to be responsive to screen width -->
		<meta
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
			name="viewport"
		/>
		<!-- Bootstrap 3.3.7 -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap/dist/css/bootstrap.min.css"
		/>
		<!-- Font Awesome -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/font-awesome/css/font-awesome.min.css"
		/>
		<!-- Ionicons -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/Ionicons/css/ionicons.min.css"
		/>
		<!-- daterange picker -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-daterangepicker/daterangepicker.css"
		/>
		<!-- bootstrap datepicker -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css"
		/>
		<!-- iCheck for checkboxes and radio inputs -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/iCheck/all.css"
		/>
		<!-- Bootstrap Color Picker -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css"
		/>
		<!-- Bootstrap time Picker -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/timepicker/bootstrap-timepicker.min.css"
		/>
		<!-- Select2 -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/select2/dist/css/select2.min.css"
		/>
		<!-- Theme style -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/dist/css/AdminLTE.min.css"
		/>
		<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/dist/css/skins/_all-skins.min.css"
		/>
		<!-- Google Font -->
		<link
			rel="stylesheet"
			href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"
		/>
		<link
			rel="stylesheet"
			href="${pageContext.request.contextPath}/public/views/index/index.css"
		/>
	</head>
	<body class="hold-transition skin-purple sidebar-mini">
		<!-- Site wrapper -->
		<div class="wrapper" id="main">
			<header class="main-header">
				<!-- Logo -->
				<a href="${pageContext.request.contextPath}" class="logo">
					<span class="logo-mini">M<b>T</b></span>
					<span class="logo-lg">Moon<b>Track</b></span>
				</a>
				<!-- Header Navbar: style can be found in header.less -->
				<nav class="navbar navbar-static-top">
					<!-- Sidebar toggle button-->
					<a
						href="#"
						class="sidebar-toggle"
						data-toggle="push-menu"
						role="button"
					>
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</a>

					<div class="navbar-custom-menu">
						<ul class="nav navbar-nav">
							<!-- Messages: style can be found in dropdown.less-->
							<li class="dropdown messages-menu">
								<a
									href="#"
									class="dropdown-toggle"
									data-toggle="dropdown"
								>
									<i class="fa fa-envelope-o"></i>
									<span class="label label-success">4</span>
								</a>
								<ul class="dropdown-menu">
									<li class="header">You have 4 messages</li>
									<li>
										<!-- inner menu: contains the actual data -->
										<ul class="menu">
											<li>
												<!-- start message -->
												<a href="#">
													<div class="pull-left">
														<img
															src="${pageContext.request.contextPath}/public/img/jolly-roger.png"
															class="img-circle"
															alt="User Image"
														/>
													</div>
													<h4>
														Support Team
														<small
															><i
																class="fa fa-clock-o"
															></i>
															5 mins</small
														>
													</h4>
													<p>
														Why not buy a new
														awesome theme?
													</p>
												</a>
											</li>
											<!-- end message -->
										</ul>
									</li>
									<li class="footer">
										<a href="#">See All Messages</a>
									</li>
								</ul>
							</li>
							<!-- Notifications: style can be found in dropdown.less -->
							<li class="dropdown notifications-menu">
								<a
									href="#"
									class="dropdown-toggle"
									data-toggle="dropdown"
								>
									<i class="fa fa-bell-o"></i>
									<span class="label label-warning">10</span>
								</a>
								<ul class="dropdown-menu">
									<li class="header">
										You have 10 notifications
									</li>
									<li>
										<!-- inner menu: contains the actual data -->
										<ul class="menu">
											<li>
												<a href="#">
													<i
														class="fa fa-users text-aqua"
													></i>
													5 new members joined today
												</a>
											</li>
										</ul>
									</li>
									<li class="footer">
										<a href="#">View all</a>
									</li>
								</ul>
							</li>
							<!-- Tasks: style can be found in dropdown.less -->
							<li class="dropdown tasks-menu">
								<a
									href="#"
									class="dropdown-toggle"
									data-toggle="dropdown"
								>
									<i class="fa fa-flag-o"></i>
									<span class="label label-danger">9</span>
								</a>
								<ul class="dropdown-menu">
									<li class="header">You have 9 tasks</li>
									<li>
										<!-- inner menu: contains the actual data -->
										<ul class="menu">
											<li>
												<!-- Task item -->
												<a href="#">
													<h3>
														Design some buttons
														<small
															class="pull-right"
															>20%</small
														>
													</h3>
													<div class="progress xs">
														<div
															class="progress-bar progress-bar-aqua"
															style="width: 20%"
															role="progressbar"
															aria-valuenow="20"
															aria-valuemin="0"
															aria-valuemax="100"
														>
															<span
																class="sr-only"
																>20%
																Complete</span
															>
														</div>
													</div>
												</a>
											</li>
											<!-- end task item -->
										</ul>
									</li>
									<li class="footer">
										<a href="#">View all tasks</a>
									</li>
								</ul>
							</li>
							<!-- User Account: style can be found in dropdown.less -->
							<li class="dropdown user user-menu">
								<a
									href="#"
									class="dropdown-toggle"
									data-toggle="dropdown"
								>
									<img
										src="${pageContext.request.contextPath}/public/img/jolly-roger.png"
										class="user-image"
										alt="User Image"
									/>
									<span class="hidden-xs">${uiBean.getUser().getUsername()}</span
									>
								</a>
								<ul class="dropdown-menu">
									<!-- User image -->
									<li class="user-header">
										<img
											src="${pageContext.request.contextPath}/public/img/jolly-roger.png"
											class="img-circle"
											alt="User Image"
										/>

										<p>
											${uiBean.getUser().getUsername()} - Web Developer
											<small>Member since Nov. 2012</small>
										</p>
									</li>
									<!-- Menu Body -->
									<li class="user-body">
										<div class="row">
											<div class="col-xs-4 text-center">
												<a href="#">Followers</a>
											</div>
											<div class="col-xs-4 text-center">
												<a href="#">Sales</a>
											</div>
											<div class="col-xs-4 text-center">
												<a href="#">Friends</a>
											</div>
										</div>
										<!-- /.row -->
									</li>
									<!-- Menu Footer-->
									<li class="user-footer">
										<div class="pull-left">
											<a
												href="#"
												class="btn btn-default btn-flat"
												>Profile</a
											>
										</div>
										<div class="pull-right">
											<a href="${pageContext.request.contextPath}/logout"
												class="btn btn-default btn-flat"> Sign out </a>
										</div>
									</li>
								</ul>
							</li>
							<!-- Control Sidebar Toggle Button -->
							<li>
								<a href="#" data-toggle="control-sidebar"
									><i class="fa fa-gears"></i
								></a>
							</li>
						</ul>
					</div>
				</nav>
			</header>

			<!-- =============================================== -->

			<!-- Left side column. contains the sidebar -->
			<aside class="main-sidebar">
				<!-- sidebar: style can be found in sidebar.less -->
				<section class="sidebar">
					<!-- Sidebar user panel -->
					<div class="user-panel">
						<div class="pull-left image">
							<img
								src="${pageContext.request.contextPath}/public/img/jolly-roger.png"
								class="img-circle"
								alt="User Image"
							/>
						</div>
						<div class="pull-left info">
							<p>${uiBean.getUser().getUsername()}</p>
							<a href="#">
								<i class="fa fa-circle text-success"></i>
								Online
							</a>
						</div>
					</div>
					<!-- search form -->
					<form action="#" method="get" class="sidebar-form">
						<div class="input-group">
							<input
								type="text"
								name="q"
								class="form-control"
								placeholder="Search..."
							/>
							<span class="input-group-btn">
								<button
									type="submit"
									name="search"
									id="search-btn"
									class="btn btn-flat"
								>
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div>
					</form>
					<!-- /.search form -->
					<!-- sidebar menu: : style can be found in sidebar.less -->
					<ul class="sidebar-menu" data-widget="tree">
						<li class="header">MAIN NAVIGATION</li>
						<li class="treeview" style="height: auto;">
							<a href="#/">
								<i class="fa fa-dashboard"></i>
								<span>Dashboard</span>
								<span class="pull-right-container">
									<i class="fa fa-angle-left pull-right"></i>
								</span>
							</a>
							<ul class="treeview-menu">
								<li>
									<a href="#/dashboard"
										><i class="fa fa-circle-o"></i
										>General</a
									>
								</li>
								<c:forEach items="${games}" var="game">
									<li
										><a
											href="#/dashboard/games/${game.getId()}"
											><i class="fa fa-circle-o"></i
											>${game.getName()}</a
										></li
									>
								</c:forEach>
							</ul>
						</li>
						<li id="nav-games">
							<a href="#/games">
								<i class="fa fa-gamepad"></i>
								<span>Games</span>
							</a>
						</li>
						<li id="nav-abtests">
							<a href="#/abtests"
								><i class="fa fa-flask"></i>
								<span>A/B Tests</span></a
							>
						</li>
						<li id="nav-segments">
							<a href="#/segments"
								><i class="fa fa-pie-chart"></i>
								<span>Segments</span></a
							>
						</li>
						<!-- 
						<li id="nav-events">
							<a href="#/events"
								><i class="fa fa-dot-circle-o"></i>
								<span>Events</span></a
							>
						</li>
						 -->
					</ul>
				</section>
				<!-- /.sidebar -->
			</aside>

			<!-- =============================================== -->

			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
					<h1 id="header-title"></h1>
				</section>

				<!-- Main content -->
				<section class="content" id="page-content"></section>
				<!-- /.content -->
			</div>
			<!-- /.content-wrapper -->

			<footer class="main-footer">
				<div class="pull-right hidden-xs"><b>Version</b> 2.1.1</div>
				<strong
					>Copyright &copy; 2018-2019
					<a href="https://http://moonmana.com">Moonmana</a>.</strong
				>
				All rights reserved.
			</footer>

			<!-- Control Sidebar -->
			<aside class="control-sidebar control-sidebar-dark">
				<!-- Create the tabs -->
				<ul class="nav nav-tabs nav-justified control-sidebar-tabs">
					<li>
						<a href="#control-sidebar-home-tab" data-toggle="tab"
							><i class="fa fa-home"></i
						></a>
					</li>

					<li>
						<a
							href="#control-sidebar-settings-tab"
							data-toggle="tab"
							><i class="fa fa-gears"></i
						></a>
					</li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
					<!-- Home tab content -->
					<div class="tab-pane" id="control-sidebar-home-tab">
						<h3 class="control-sidebar-heading">Recent Activity</h3>
						<ul class="control-sidebar-menu">
							<li>
								<a href="javascript:void(0)">
									<i
										class="menu-icon fa fa-birthday-cake bg-red"
									></i>

									<div class="menu-info">
										<h4 class="control-sidebar-subheading">
											Langdon's Birthday
										</h4>

										<p>Will be 23 on April 24th</p>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<i
										class="menu-icon fa fa-user bg-yellow"
									></i>

									<div class="menu-info">
										<h4 class="control-sidebar-subheading">
											Frodo Updated His Profile
										</h4>

										<p>New phone +1(800)555-1234</p>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<i
										class="menu-icon fa fa-envelope-o bg-light-blue"
									></i>

									<div class="menu-info">
										<h4 class="control-sidebar-subheading">
											Nora Joined Mailing List
										</h4>

										<p>nora@example.com</p>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<i
										class="menu-icon fa fa-file-code-o bg-green"
									></i>

									<div class="menu-info">
										<h4 class="control-sidebar-subheading">
											Cron Job 254 Executed
										</h4>

										<p>Execution time 5 seconds</p>
									</div>
								</a>
							</li>
						</ul>
						<!-- /.control-sidebar-menu -->

						<h3 class="control-sidebar-heading">Tasks Progress</h3>
						<ul class="control-sidebar-menu">
							<li>
								<a href="javascript:void(0)">
									<h4 class="control-sidebar-subheading">
										Custom Template Design
										<span
											class="label label-danger pull-right"
											>70%</span
										>
									</h4>

									<div class="progress progress-xxs">
										<div
											class="progress-bar progress-bar-danger"
											style="width: 70%"
										></div>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<h4 class="control-sidebar-subheading">
										Update Resume
										<span
											class="label label-success pull-right"
											>95%</span
										>
									</h4>

									<div class="progress progress-xxs">
										<div
											class="progress-bar progress-bar-success"
											style="width: 95%"
										></div>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<h4 class="control-sidebar-subheading">
										Laravel Integration
										<span
											class="label label-warning pull-right"
											>50%</span
										>
									</h4>

									<div class="progress progress-xxs">
										<div
											class="progress-bar progress-bar-warning"
											style="width: 50%"
										></div>
									</div>
								</a>
							</li>
							<li>
								<a href="javascript:void(0)">
									<h4 class="control-sidebar-subheading">
										Back End Framework
										<span
											class="label label-primary pull-right"
											>68%</span
										>
									</h4>

									<div class="progress progress-xxs">
										<div
											class="progress-bar progress-bar-primary"
											style="width: 68%"
										></div>
									</div>
								</a>
							</li>
						</ul>
						<!-- /.control-sidebar-menu -->
					</div>
					<!-- /.tab-pane -->
					<!-- Stats tab content -->
					<div class="tab-pane" id="control-sidebar-stats-tab">
						Stats Tab Content
					</div>
					<!-- /.tab-pane -->
					<!-- Settings tab content -->
					<div class="tab-pane" id="control-sidebar-settings-tab">
						<form method="post">
							<h3 class="control-sidebar-heading">
								General Settings
							</h3>

							<div class="form-group">

								<c:if test="${uiBean.getUser().isAdmin()}">
									<label class="control-sidebar-subheading">
										Register new user
										<a href="${pageContext.request.contextPath}/register"
											class="text-green pull-right" >
											<i class="fa fa-user-plus"></i>
										</a>
									</label>
								</c:if>

								<label class="control-sidebar-subheading">
									Report panel usage
									<input
										type="checkbox"
										class="pull-right"
										checked
									/>
								</label>

								<p>
									Some information about this general settings
									option
								</p>
							</div>
							<!-- /.form-group -->

							<div class="form-group">
								<label class="control-sidebar-subheading">
									Allow mail redirect
									<input
										type="checkbox"
										class="pull-right"
										checked
									/>
								</label>

								<p>
									Other sets of options are available
								</p>
							</div>
							<!-- /.form-group -->

							<div class="form-group">
								<label class="control-sidebar-subheading">
									Expose author name in posts
									<input
										type="checkbox"
										class="pull-right"
										checked
									/>
								</label>

								<p>
									Allow the user to show his name in blog
									posts
								</p>
							</div>
							<!-- /.form-group -->

							<h3 class="control-sidebar-heading">
								Chat Settings
							</h3>

							<div class="form-group">
								<label class="control-sidebar-subheading">
									Show me as online
									<input
										type="checkbox"
										class="pull-right"
										checked
									/>
								</label>
							</div>
							<!-- /.form-group -->

							<div class="form-group">
								<label class="control-sidebar-subheading">
									Turn off notifications
									<input type="checkbox" class="pull-right" />
								</label>
							</div>
							<!-- /.form-group -->

							<div class="form-group">
								<label class="control-sidebar-subheading">
									Delete chat history
									<a
										href="javascript:void(0)"
										class="text-red pull-right"
										><i class="fa fa-trash-o"></i
									></a>
								</label>
							</div>
							<!-- /.form-group -->
						</form>
					</div>
					<!-- /.tab-pane -->
				</div>
			</aside>
			<!-- /.control-sidebar -->
			<!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
			<div class="control-sidebar-bg"></div>
		</div>
		<!-- ./wrapper -->

		<!-- jQuery 3 -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/jquery/dist/jquery.min.js"></script>
		<!-- Bootstrap 3.3.7 -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
		<!-- Select2 -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/select2/dist/js/select2.full.min.js"></script>
		<!-- InputMask -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/input-mask/jquery.inputmask.js"></script>
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/input-mask/jquery.inputmask.extensions.js"></script>
		<!-- date-range-picker -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/moment/min/moment.min.js"></script>
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
		<!-- bootstrap datepicker -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
		<!-- bootstrap color picker -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
		<!-- bootstrap time picker -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/timepicker/bootstrap-timepicker.min.js"></script>
		<!-- SlimScroll -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
		<!-- iCheck 1.0.1 -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/plugins/iCheck/icheck.min.js"></script>
		<!-- FastClick -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/fastclick/lib/fastclick.js"></script>
		<!-- AdminLTE App -->
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/dist/js/adminlte.min.js"></script>
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/raphael/raphael.min.js"></script>
		<script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/morris.js/morris.min.js"></script>
		<script
			src="${pageContext.request.contextPath}/public/js/sammyjs/lib/sammy.js"
			type="text/javascript"
			charset="utf-8"
		></script>

		<jsp:include page="/public/js/main.jsp" />
		<script>
			$(document).ready(function() {
				$(".sidebar-menu").tree();
			});
		</script>
	</body>
</html>
