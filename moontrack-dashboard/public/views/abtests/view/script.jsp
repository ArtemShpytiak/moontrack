<script>
	(function() {
		postData(abtestMetricUrl + "${abtest.getId()}/users")
			.then(data => {
				setUserChartWidget(data, "#users-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/logins");
			})
			.then(data => {
				setUserChartWidget(data, "#logins-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/arpdau");
			})
			.then(data => {
				setUserChartWidget(data, "#arpdau-metric");
			})
			.then(function() {
				return postData(
					abtestMetricUrl + "${abtest.getId()}/revenue_daily"
				);
			})
			.then(data => {
				setUserChartWidget(data, "#dailyrevenue-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/dau");
			})
			.then(data => {
				setUserChartWidget(data, "#dau-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/dpu");
			})
			.then(data => {
				setUserChartWidget(data, "#dpu-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/devices");
			})
			.then(data => {
				setUserChartWidget(data, "#devices-metric");
			})
			.then(function() {
				return postData(abtestMetricUrl + "${abtest.getId()}/oses");
			})
			.then(data => {
				setUserChartWidget(data, "#oses-metric");
			})
			.then(function() {
				return postData(
					abtestMetricUrl + "${abtest.getId()}/platforms"
				);
			})
			.then(data => {
				setUserChartWidget(data, "#platforms-metric");
			})
			.catch(error => {
				console.log(error);
			});
	})();

	function setUserChartWidget(data, id) {
		if (data.chart.data.length == 0) {
			data.chart.data = [{ a: "1", y: "No Data" }];
		}

		if (data.chartType === "Bar") {
			new Morris.Bar(data.chart);
		} else if (data.chartType === "Line") {
			new Morris.Line(data.chart);
		}
		let widget = $(document).find(id);
		widget.find(".box-title").text(data.metricName);
		widget.find(".overlay").remove();
	}
	function addMetric() {
		$("#add-metric").modal("show");
	}
</script>
