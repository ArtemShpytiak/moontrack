<%@page import="com.moonmana.moontrack.dto.DashboardCacheDTO"%> <%
DashboardCacheDTO installs =
((DashboardCacheDTO)request.getAttribute("installs"));%>

<div class="col-lg-3 col-xs-6">
	<h4>Total installs</h4>
	<div class="box">
		<div class="box-header bg-aqua">
			<h3 class="box-title">
				All platforms: ${installs.getInstallsTotalFormatted()}
			</h3>
		</div>

		<div class="box-body no-padding">
			<table class="table table-condensed">
				<tbody>
					<tr>
						<th></th>
						<th style="width: 350px"></th>
						<th></th>
					</tr>

					<tr>
						<td align="center">
							<i class="fa fa-apple fa-2x" style="color: #00c0ef">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsIosPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${installs.getInstallsIosFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa  fa-2x" style="color: #00c0ef">
								X
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsXPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${dashcache.getInstallsXFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-android fa-2x"
								style="color: #00c0ef"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsAndroidPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${dashcache.getInstallsAndroidFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-facebook fa-2x"
								style="color: #00c0ef"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsFbPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${dashcache.getInstallsFbFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa ion-social-windows fa-2x"
								style="color: #00c0ef"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsWinPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${dashcache.getInstallsWinFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa fa-vk fa-2x" style="color: #00c0ef">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-aqua"
									style="width: ${installs.getInstallsVkPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00c0ef"
							>
								${dashcache.getInstallsVkFormatted()}
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
