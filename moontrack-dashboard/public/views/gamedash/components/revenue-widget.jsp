<%@page import="com.moonmana.moontrack.dto.DashboardCacheDTO"%> <%
DashboardCacheDTO revenue =
((DashboardCacheDTO)request.getAttribute("revenue")); %>

<div class="col-lg-3 col-xs-6">
	<h4>Total revenue</h4>
	<div class="box">
		<div class="box-header bg-green">
			<h3 class="box-title">
				All platforms: $${revenue.getRevenueTotalFormatted()}
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
							<i class="fa fa-apple fa-2x" style="color: #00a65a">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueIosPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueIosFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa  fa-2x" style="color: #00a65a">
								X
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueXPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueXFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-android fa-2x"
								style="color: #00a65a"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueAndroidPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueAndroidFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa fa-facebook fa-2x"
								style="color: #00a65a"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueFbPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueFbFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i
								class="fa ion-social-windows fa-2x"
								style="color: #00a65a"
							>
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueWinPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueWinFormatted()}
							</span>
						</td>
					</tr>

					<tr>
						<td align="center">
							<i class="fa fa-vk fa-2x" style="color: #00a65a">
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar progress-bar-green"
									style="width: ${revenue.getRevenueVkPercentage()}%"
								></div>
							</div>
							<span
								class="progress-description"
								style="color:#00a65a"
							>
								$${revenue.getRevenueVkFormatted()}
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
