<script>
	$("#metrics").select2({
		dropdownParent: $("#add-panel")
	});
	$("#games").select2({
		dropdownParent: $("#add-panel")
	});
	$("#events").select2({
		dropdownParent: $("#add-panel")
	});
	$("#countries").select2({
		dropdownParent: $("#add-panel")
	});
	$("#oses").select2({
		dropdownParent: $("#add-panel")
	});
	$("#platforms").select2({
		dropdownParent: $("#add-panel")
	});
	$("#realms").select2({
		dropdownParent: $("#add-panel")
	});
	$("#devices").select2({
		dropdownParent: $("#add-panel")
	});

	$("#registration").daterangepicker({
		autoUpdateInput: false,
		locale: {
			cancelLabel: "Clear"
		}
	});

	$("#registration").on("apply.daterangepicker", function(ev, picker) {
		$(this).val(
			picker.startDate.format("MM/DD/YYYY") +
				" - " +
				picker.endDate.format("MM/DD/YYYY")
		);
	});

	$("#daterange").daterangepicker({
		autoUpdateInput: false,
		locale: {
			cancelLabel: "Clear"
		}
	});

	$("#registration").on("cancel.daterangepicker", function(ev, picker) {
		$(this).val("");
	});

	$("#daterange").on("apply.daterangepicker", function(ev, picker) {
		$(this).val(
			picker.startDate.format("MM/DD/YYYY") +
				" - " +
				picker.endDate.format("MM/DD/YYYY")
		);
	});

	$("#daterange").on("cancel.daterangepicker", function(ev, picker) {
		$(this).val("");
	});

	setIAPsCheckbox();

	$(".hl-bg").click(function() {
		$("#add-panel").modal("show");
	});

	metrics.onchange = function() {
		if (metrics.value == "${eventType}") {
			document.getElementById("events-menu-item").hidden = false;
			document.getElementById("countries-menu-item").hidden = true;
			document.getElementById("oses-menu-item").hidden = true;
			document.getElementById("platforms-menu-item").hidden = true;
			document.getElementById("registrations-menu-item").hidden = true;
			document.getElementById("iaps-checkbox").hidden = true;

			if (games.value == "-") {
				document.getElementById("confirm-button").disabled = true;
			}
			else {
				document.getElementById("confirm-button").disabled = false;
			}
		}
		else {
			document.getElementById("events-menu-item").hidden = true;
			document.getElementById("countries-menu-item").hidden = false;
			document.getElementById("oses-menu-item").hidden = false;
			document.getElementById("platforms-menu-item").hidden = false;
			document.getElementById("registrations-menu-item").hidden = false;
			document.getElementById("iaps-checkbox").hidden = false;

			document.getElementById("confirm-button").disabled = false;
		}
	};

	games.onchange = function() {
		if (metrics.value == "${eventType}" && games.value == "-") {
			document.getElementById("confirm-button").disabled = true;
		}
		else {
			document.getElementById("confirm-button").disabled = false;
		}
	};

	(function() {
		let root = $(document).find("#metric-container");
		let len = root.children().length;
		for (let i = 1; i < len; i++) {
			root.children()
				.eq(i)
				.attr("hidden", false);
		}
	})();

	postData(
		"${pageContext.request.contextPath}" +
			"/dashboard/widgets/games/${game.getId()}/get"
	)
		.then(data => {
			data.forEach(function(item, index) {
				const box = $(document).find("#" + item.widgetId);
				setWidgetBoxData(box, item);
			});
		})
		.catch(error => {
			// console.log(error);
		});

	$("div#add-panel")
		.find("#confirm-button")
		.click(function() {
			const form = $("div#add-panel");
			const tempMetricId = new Date().getTime();

			let box = $("#template-metric-box").clone();
			$(box).attr("hidden", false);
			$(box).attr("id", tempMetricId);
			box.appendTo("#metric-container");

			$("div#add-panel").modal("hide");

			let iaps = {
				paying: null,
				moneyfrom: null,
				moneyto: null
			};

			if (
				$(form)
					.find("#input-iaps")
					.is(":checked")
			) {
				iaps.paying = $(form)
					.find("#input-paying")
					.is(":checked");
				if (iaps.paying) {
					iaps.moneyfrom = $(form)
						.find("#moneyspentfrom")
						.val();
					iaps.moneyto = $(form)
						.find("#moneyspentto")
						.val();
				}
			}

			let filter = {
				selectedMetricType: form.find("#metrics").val(),
				tempMetricId: tempMetricId,
				game: $("#games").val(),
				event: $("#events").val(),
				countries: $("#countries").val(),
				oses: $("#oses").val(),
				platforms: $("#platforms").val(),
				realms: $("#realms").val(),
				devices: $("#devices").val(),
				daterange: $("#daterange").val(),
				registration: $("#registration").val(),
				paying: iaps.paying,
				moneyfrom: iaps.moneyfrom,
				moneyto: iaps.moneyto
			};
			postData(
				"${pageContext.request.contextPath}/dashboard/widgets/games/${game.getId()}/add",
				{ filter }
			)
				.then(data => {
					const widgetBox = $(document).find("#" + data.tempMetricId);

					if (widgetBox.length) {
						setWidgetBoxData(widgetBox, data);
					} else {
						console.log(
							"widget w id not found: " + data.tempMetricId
						);
					}
				}) // JSON-string from `response.json()` call
				.catch(error => {
					// console.error(error);
				});
		});

	function setWidgetBoxData(widgetBox, data) {
		data.chart.data = data.chart.data.filter(
			value => Object.keys(value).length !== 0
		);

		if (typeof data.chart.yLabelFormat !== "undefined") {
			data.chart.yLabelFormat = eval(data.chart.yLabelFormat);
		}
		if (typeof data.chart.xLabelFormat !== "undefined") {
			data.chart.xLabelFormat = eval(data.chart.xLabelFormat);
		}
		widgetBox.find(".chart").attr("id", "chart-" + data.widgetId);

		if (data.chart.data.length == 0) {
			data.chart.data = [{ a: "0", y: "No Data" }];
		}

		if (data.chartType === "Bar") {
			new Morris.Bar(data.chart);
		} else if (data.chartType === "Line") {
			new Morris.Line(data.chart);
		}

		let btnFilter = widgetBox.find("#btn-filter-widget");
		btnFilter.data("id", data.widgetId);
		// btnFilter.on("click", () => {
		//   $("#filter-panel").modal("show");
		// });

		let btnRemove = widgetBox.find("#btn-remove-widget");
		btnRemove.data("id", data.widgetId);
		btnRemove.on("click", () => {
			widgetBox.boxWidget("remove");
			const url =
				"${pageContext.request.contextPath}/dashboard/widgets/games/${game.getId()}/remove/" +
				data.widgetId;
			console.log("remove: " + url);
			postData(url)
				.then(data => {
					// console.log(data);
				})
				.catch(error => {
					// console.log(error);
				});
		});
		widgetBox.attr("id", data.widgetId);
		widgetBox.find(".box-title").text(data.metricName);
		widgetBox.find(".overlay").remove();
	}

	function setIAPsCheckbox() {
		$("#input-iaps").change(function() {
			if ($(this).prop("checked") == true) {
				let root = $("#moneyspent-root");
				let template = $(document).find("#moneyspent-child");
				let child = template.clone();
				child.appendTo(root);
				child.attr("hidden", false);
				setPayingCheckbox(child.find("#input-paying"));
			} else if ($(this).prop("checked") == false) {
				$("#moneyspent-root").empty();
			}
		});
	}
	function setPayingCheckbox(checkbox) {
		$(checkbox).change(function() {
			if ($(this).prop("checked") == true) {
			} else if ($(this).prop("checked") == false) {
			}
		});
	}
</script>
