<div class="row">
	<div class="col-lg-3 col-xs-6">
		<div class="small-box bg-aqua">
			<div class="inner">
				<h3>${dashcache.getRegisteredUsersToday()}</h3>
				<p>New users today</p>
			</div>
			<div class="icon">
				<i class="ion-person-add"></i>
			</div>
			<a class="small-box-footer"></a>
		</div>
	</div>

	<div class="col-lg-3 col-xs-6">
		<div class="small-box bg-green">
			<div class="inner">
				<h3>
					$${dashcache.getRevenueTodayFormatted()}
				</h3>
				<p>Revenue today</p>
			</div>
			<div class="icon">
				<i class="ion-stats-bars"></i>
			</div>
			<a class="small-box-footer"></a>
		</div>
	</div>

	<div class="col-lg-3 col-xs-6">
		<div class="small-box bg-yellow">
			<div class="inner">
				<h3>
					${dashcache.getRetention1d()}%
					${dashcache.getRetention7d()}%
					${dashcache.getRetention30d()}%
				</h3>
				<p>Retention 1d / 7d / 30d</p>
			</div>
			<div class="icon">
				<i class="ion-person-add"></i>
			</div>
			<a class="small-box-footer"></a>
		</div>
	</div>

	<div class="col-lg-3 col-xs-6">
		<div class="small-box bg-red">
			<div class="inner">
				<h3>
					$${dashcache.getArpdauFormatted()}
				</h3>
				<p>ARPDAU</p>
			</div>
			<div class="icon">
				<i class="ion-pie-graph"></i>
			</div>
			<a class="small-box-footer"></a>
		</div>
	</div>
</div>
