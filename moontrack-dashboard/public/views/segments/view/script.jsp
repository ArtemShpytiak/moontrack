<script>
	(function() {
		postData(segmentMetricUrl + "${segment.getId()}/arpdau")
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#arpdau-metric");
			})
			.then(function() {
				return postData(
					segmentMetricUrl + "${segment.getId()}/daily_revenue"
				);
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#dailyrevenue-metric");
			})
			.then(function() {
				return postData(segmentMetricUrl + "${segment.getId()}/dau");
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#dau-metric");
			})
			.then(function() {
				return postData(
					segmentMetricUrl + "${segment.getId()}/devices"
				);
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#devices-metric");
			})
			.then(function() {
				return postData(segmentMetricUrl + "${segment.getId()}/dpu");
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#dpu-metric");
			})
			.then(function() {
				return postData(segmentMetricUrl + "${segment.getId()}/logins");
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#logins-metric");
			})
			.then(function() {
				return postData(segmentMetricUrl + "${segment.getId()}/oses");
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#oses-metric");
			})
			.then(function() {
				return postData(
					segmentMetricUrl + "${segment.getId()}/platforms"
				);
			})
			.then(data => {
				console.log(data);
				setUserChartWidget(data, "#platforms-metric");
			})
			.catch(error => {
				console.log(error);
			});
	})();

	function postData(url = ``, data = {}) {
		return fetch(url, {
			method: "POST",
			mode: "cors",
			cache: "no-cache",
			credentials: "same-origin",
			headers: {
				"Content-Type": "application/json"
			},
			redirect: "follow",
			referrer: "no-referrer",
			body: JSON.stringify(data)
		}).then(response => response.json());
	}
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
