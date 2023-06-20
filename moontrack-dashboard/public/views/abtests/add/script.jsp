<script>
	countFilters = 0;
	filterBoxes = [];

	countPercents = 0;
	percentBoxes = [];

	template = null;
	root;
	abtest;

	$(".select2").select2();

	$("#activeness").daterangepicker({
		startDate: moment(),
		endDate: moment().add(14, "days")
	});

	template = $("#box-group-filter");
	root = $("#tab-content-group");

	function onCancelAbtestAdd() {
		location.href = "#/abtests";
	}

	function onSubmitAbtestAdd() {
		abtest = new Abtest();
		abtest.name = $("#abtestname").val();
		abtest.game = $("#gameid").val();
		abtest.activeness = $("#activeness").val();

		processFilterGroups(abtest);
		processPercentGroups(abtest);
		processSegmentGroup(abtest);

		console.log(abtest);

		// $("#cancel-btn").prop("disabled", true);
		// $("#save-btn").prop("disabled", true);

		showoverlay();
		postData(
			`${pageContext.request.contextPath}/abtests/${game.id}/add/submit`,
			{
				abtest: abtest
			}
		)
			.then(response => {
				loadpage("abtests");
			})
			.catch(error => {
				hideoverlay();
				// $("#cancel-btn").prop("disabled", false);
				// $("#save-btn").prop("disabled", false);
			});
		return false;
	}
	function processSegmentGroup(abtest) {}
	function processPercentGroups(abtest) {
		for (let i = 0; i < percentBoxes.length; i++) {
			let group = new GroupPercent();
			group.name = percentBoxes[i].find("#input-group-name").val();
			group.percent = percentBoxes[i].find("#input-percent-value").val();
			abtest.groupsPercent.push(group);
		}
	}
	function processFilterGroups(abtest) {
		for (let i = 0; i < filterBoxes.length; i++) {
			let box = filterBoxes[i];
			let group = new GroupFilter();
			abtest.groupsFilter.push(group);
			group.name = $(box)
				.find("#input-group-name")
				.val();
			group.countries = box.find("#countries").val();
			group.platforms = box.find("#platforms").val();
			group.oses = box.find("#oses").val();
			group.devices = box.find("#devices").val();
			group.realms = box.find("#realms").val();
			group.registration = $(box)
				.find("#registration")
				.val();

			if (
				$(box)
					.find("#input-iaps")
					.is(":checked")
			) {
				group.paying = $(box)
					.find("#input-paying")
					.is(":checked");
				if (group.paying) {
					group.moneyfrom = $(box)
						.find("#moneyspentfrom")
						.val();
					group.moneyto = $(box)
						.find("#moneyspentto")
						.val();
				} else {
					group.moneyfrom = null;
					group.moneyto = null;
				}
			} else {
				group.paying = null;
				group.moneyfrom = null;
				group.moneyto = null;
			}
		}
	}

	function addFilterGroup() {
		if (filterBoxes.length >= MAX_GROUPS) {
			alert("Max allowed groups are 5");
			return;
		}
		countFilters++;

		let clone = $(template).clone();
		$(clone).attr("hidden", false);
		$(clone).data("id", countFilters);
		$(root).append(clone);
		filterBoxes.push(clone);

		$(clone)
			.find("#registration")
			.daterangepicker({
				autoUpdateInput: false,
				locale: {
					cancelLabel: "Clear"
				}
			});

		$(clone)
			.find("#registration")
			.on("apply.daterangepicker", function(ev, picker) {
				$(this).val(
					picker.startDate.format("MM/DD/YYYY") +
						" - " +
						picker.endDate.format("MM/DD/YYYY")
				);
			});

		$(clone)
			.find("#registration")
			.on("cancel.daterangepicker", function(ev, picker) {
				$(this).val("");
			});

		setGroupTitle(clone, nextGroupName());
		setMultiselects(clone);
		// setPayingCheckbox(clone);
		setIAPsCheckbox(clone);
		setRemoveBtn(clone);
	}
	function setRemoveBtn(clone) {
		$(clone)
			.find("#box-btn-remove")
			.data("id", $(clone).data("id"));
	}
	function setGroupTitle(clone, groupName) {
		$(clone)
			.find("#box-title-group")
			.text(groupName);

		let input = $(clone).find("#input-group-name");
		$(input).val(groupName);
		$(input).on(
			"input",
			{ input: input, header: $(clone).find("#box-title-group") },
			handleInputOnGroupName
		);
	}
	function setIAPsCheckbox(clone) {
		$(clone)
			.find("#input-iaps")
			.change(function() {
				if ($(this).prop("checked") == true) {
					let root = $(clone).find("#moneyspent-root");
					let template = $(document).find("#moneyspent-child");
					let child = template.clone();
					child.appendTo(root);
					child.attr("hidden", false);
					setPayingCheckbox(child.find("#input-paying"));
				} else if ($(this).prop("checked") == false) {
					$(clone)
						.find("#moneyspent-root")
						.empty();
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
	function setMultiselects(clone) {
		setSelect2Input(clone, "country", "countries");
		setSelect2Input(clone, "os", "oses");
		setSelect2Input(clone, "platform", "platforms");
		setSelect2Input(clone, "device", "devices");
		setSelect2Input(clone, "realm", "realms");
	}
	function nextGroupName() {
		return "Group " + "#" + countFilters;
	}
	function setSelect2Input(host, type, types) {
		let src = $("#select-templates").find("#" + types);
		let clone = $(src)
			.select2("destroy")
			.clone();
		$(clone)
			.appendTo($(host).find("#" + "form-group-" + type))
			.select2();
		$(src).select2();
	}
	function collapse() {}
	function removeFilter(el) {
		let id = $(el).data("id");
		let len = filterBoxes.length;
		let root;
		for (let i = 0; i < len; i++) {
			let box = filterBoxes[i];
			if ($(box).data("id") === id) {
				root = box;
				filterBoxes.splice(i, 1);
			}
		}
		$(root).remove();
		console.log("removing group");
	}
	function handlePayingCheckbox(checkbox) {}
	function handleInputOnGroupName(e) {
		$(e.data.header).text($(e.data.input).val());
	}
	function addSegmentGroup() {}
	function addGroupByPercentage() {
		if (percentBoxes.length >= MAX_GROUPS) {
			alert("Max allowed groups are 5");
			return;
		}
		countPercents++;

		let root = $("#percentage").find("#tab-content-group");
		let source = $("#template-percent").find("#box-group-percent");
		let clone = $(source).clone();
		$(clone).attr("hidden", false);
		$(clone).data("id", countPercents);

		clone.appendTo($(root));
		percentBoxes.push(clone);

		setRemoveBtn(clone);
		setGroupTitle(clone, "Group #" + countPercents);
	}
	function removeGroupByPercentage(el) {
		let id = $(el).data("id");
		let len = percentBoxes.length;
		let root;
		for (let i = 0; i < len; i++) {
			let box = percentBoxes[i];
			if ($(box).data("id") === id) {
				root = box;
				percentBoxes.splice(i, 1);
			}
		}
		$(root).remove();
		console.log("removing group");
	}
	function validateForm() {
		let parent = $("percentage-group");
		let len = parent.children.length;
		let sum = 0.0;
		for (let i = 0; i < len; i++) {
			let value = parent.children[i].value;
			let parsed = Number.parseFloat(value);

			if (parsed === 0.0) {
				alert("Invalid value: " + parsed);
				return false;
			}
			sum += parsed;
		}
		if (sum > 100.0) {
			alert("Total percentage greater than 100.");
			return false;
		}
		return true;
	}
	function postData(url = ``, data = {}) {
		// Default options are marked with *
		return fetch(url, {
			method: "POST", // *GET, POST, PUT, DELETE, etc.
			mode: "cors", // no-cors, cors, *same-origin
			cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
			credentials: "same-origin", // include, *same-origin, omit
			headers: {
				"Content-Type": "application/json"
				// "Content-Type": "application/x-www-form-urlencoded",
			},
			redirect: "follow", // manual, *follow, error
			referrer: "no-referrer", // no-referrer, *client
			body: JSON.stringify(data) // body data type must match "Content-Type" header
		}); // parses JSON response into native Javascript objects
	}
</script>
