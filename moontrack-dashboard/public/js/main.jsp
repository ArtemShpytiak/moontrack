<script>
	$(document).ready(function() {
		var app = $.sammy("#main", function() {
			this.get("#/dashboard", function(context) {
				ondash();
			});
			this.get("#/dashboard/games/:id", function(context) {
				loadpage("dashboard/games/" + this.params["id"]);
			});
			this.get("#/abtests", function(context) {
				onabtests();
			});
			this.get("#/abtests/:id/add", function(context) {
				loadpage("abtests/" + this.params["id"] + "/add");
			});
			this.get("#/abtests/edit/:id", function(context) {
				loadpage("abtests/edit/" + this.params["id"]);
			});
			this.get("#/abtests/view/:id", function(context) {
				loadpage("abtests/view/" + this.params["id"]);
			});
			this.get("#/abtests/archive/:id", function(context) {
				loadpage("abtests/archive/" + this.params["id"]);
			});
			this.get("#/segments", function(context) {
				onsegments();
			});
			this.get("#/segments/add", function(context) {
				loadpage("segments/add");
			});
			this.get("#/segments/edit/:id", function(context) {
				loadpage("segments/edit/" + this.params["id"]);
			});
			this.get("#/segments/view/:id", function(context) {
				loadpage("segments/view/" + this.params["id"]);
			});
			this.get("#/segments/archive/:id", function(context) {
				loadpage("segments/archive/" + this.params["id"]);
			});
			this.get("#/games", function(context) {
				ongames();
			});
			this.get("#/games/add", function(context) {
				loadpage("games/add");
			});
			this.get("#/games/edit/:id", function(context) {
				loadpage("games/edit/" + this.params["id"]);
			});
			this.get("#/games/view/:id", function(context) {
				loadpage("games/view/" + this.params["id"]);
			});
			this.get("#/games/archive/:id", function(context) {
				loadpage("games/archive/" + this.params["id"]);
			});
		});
		$(function() {
			app.run("#/dashboard");
		});
	});

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

	const contextPath = "${pageContext.request.contextPath}/";
	const abtestMetricUrl =
		"${pageContext.request.contextPath}/abtests/metrics/get/";
	const segmentMetricUrl =
		"${pageContext.request.contextPath}/segments/metrics/get/";

	function loadpage(url) {
		$.ajax({
			url: contextPath + url,
			type: "POST",
			success: function(data) {
				$("#page-content").html(data);
			},
			error: function() {
				console.log("error");
			}
		});
	}
	function senddata(url, data, onsuccess) {
		$.ajax({
			url: contextPath + url,
			data: data,
			type: "POST",
			success: function(data) {
				onsuccess();
			},
			error: function() {
				alert("error");
			}
		});
	}
	function showoverlay() {
		let overlay = $(document)
			.find("#overlay")
			.clone();
		overlay.attr("hidden", false);
		$(document)
			.find("#box-container")
			.append(overlay);
	}

	function hideoverlay() {
		$(document)
			.find("#overlay")
			.remove();
	}

	// navigation

	function deactivateNavItem() {
		$("ul > li.active").removeClass("active");
	}

	function ondash() {
		deactivateNavItem();
		$("#nav-dash").addClass("active");
		loadpage("dashboard");
		changetitle("Dashboard");
	}

	function ongames() {
		deactivateNavItem();
		$("#nav-games").addClass("active");
		loadpage("games");
		changetitle("Games");
	}
	function onabtests() {
		deactivateNavItem();
		$("#nav-abtests").addClass("active");
		loadpage("abtests");
		changetitle("A/B Tests");
	}
	function onsegments() {
		deactivateNavItem();
		$("#nav-segments").addClass("active");
		loadpage("segments");
		changetitle("Segments");
	}

	function changetitle(title) {
		$("#header-title").text(title);
	}

	// === VARIABLES ===

	// abtests
	let countFilters = 0;
	let filterBoxes = [];

	let countPercents = 0;
	let percentBoxes = [];

	let template = null;
	let root;
	const MAX_GROUPS = 5;
	let abtest;

	class Abtest {
		name;
		game;
		activeness;
		groupsPercent = [];
		groupsFilter = [];
		groupsSegment = [];
	}
	class GroupPercent {
		name;
		percent;
	}
	class GroupFilter {
		name = "";
		countries = [];
		oses = [];
		platforms = [];
		realms = [];
		devices = [];
		registration;
		paying;
		moneyfrom;
		moneyto;
	}
	class GroupSegment {
		id = [];
	}
</script>
