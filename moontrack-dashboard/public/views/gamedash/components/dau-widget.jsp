<%@page import="com.moonmana.moontrack.dto.DashboardCacheDTO"%> <%
DashboardCacheDTO online = ((DashboardCacheDTO)request.getAttribute("online"));
%>

<div class="col-lg-3 col-xs-6">
	<h4>Online/DAU</h4>
	<div class="box">
		<div class="box-header bg-yellow">
			<h3 class="box-title">
				All platforms: ${online.getDauTotalFormatted()}
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
							<i class="fa fa-apple fa-2x" style="color: #f39c12">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauIosPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauIosFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa  fa-2x" style="color: #f39c12">
								X
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauXPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauXFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-android fa-2x"
								style="color: #f39c12"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauAndroidPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauAndroidFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-facebook fa-2x"
								style="color: #f39c12"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauFbPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauFbFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa ion-social-windows fa-2x"
								style="color: #f39c12"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauWinPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauWinFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa fa-vk fa-2x" style="color: #f39c12">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-yellow"
									style="width: ${online.getDauVkPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#f39c12"
							>
								${online.getDauVkFormatted()}
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
