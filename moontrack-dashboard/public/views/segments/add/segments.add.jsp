<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<form id="segment-add-form">
			<div class="box box-default" id="box-container">
				<div class="box-header with-border">
					<h3 class="box-title">New Segment</h3>
				</div>

				<!-- /.box-header -->
				<div class="box-body">
					<div class="form-group">
						<label for="name">Name</label>
						<input
							type="text"
							class="form-control"
							id="name"
							name="name"
							placeholder="Name"
							required
						/>
					</div>
					<div class="form-group">
						<label>Game</label>
						<select
							name="game"
							class="form-control select2 select2-hidden-accessible"
							data-placeholder="Select a Game"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
						>
							<c:forEach var="game" items="${games}">
								<option value="${game.id}">${game.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label>Date range:</label>

						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input
								name="daterange"
								type="text"
								class="form-control pull-right"
								id="daterange"
							/>
						</div>
					</div>
					<div class="form-group">
						<label>Countries</label>
						<select
							name="country"
							class="form-control select2 select2-hidden-accessible"
							multiple=""
							data-placeholder="Select a Country"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
						>
							<c:forEach var="country" items="${countries}">
								<option value="${country.code}"
									>${country.name}</option
								>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label>OS</label>
						<select
							name="os"
							class="form-control select2 select2-hidden-accessible"
							multiple=""
							data-placeholder="Select an OS"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
						>
							<c:forEach var="os" items="${oses}">
								<option value="${os.code}">${os.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label>Platform</label>
						<select
							name="platform"
							class="form-control select2 select2-hidden-accessible"
							multiple=""
							data-placeholder="Select a Platform"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
						>
							<c:forEach var="platform" items="${platforms}">
								<option value="${platform.code}"
									>${platform.name}</option
								>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label>Realm</label>
						<select
							name="realms"
							class="form-control select2 select2-hidden-accessible"
							multiple=""
							data-placeholder="Select a realm"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
							id="realms"
							disabled
						>
							<!-- <c:forEach var="realm" items="${realms}">
                  <option value="${realm.realm}">${realm.realm}</option>
                </c:forEach> -->
						</select>
					</div>
					<div class="form-group">
						<label>Device</label>
						<select
							name="devices"
							class="form-control select2 select2-hidden-accessible"
							multiple=""
							data-placeholder="Select a device"
							style="width: 100%;"
							tabindex="-1"
							aria-hidden="true"
							id="devices"
							disabled
						>
							<!-- <c:forEach var="device" items="${devices}">
                    <option value="${device.name}">${device.name}</option>
                  </c:forEach> -->
						</select>
					</div>
					<div class="form-group">
						<label>Registration range:</label>

						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input
								name="registration"
								type="text"
								class="form-control pull-right"
								id="registration"
							/>
						</div>
					</div>

					<div class="form-group">
						<div class="checkbox">
							<label>
								<input
									type="checkbox"
									name="iaps"
									id="input-iaps"
								/>
								IAPs
							</label>
						</div>
					</div>
					<div id="moneyspent-root"></div>

					<div class="box-footer">
						<button
							type="button"
							class="btn btn-default pull-left"
							onclick="onCancelSegmentAdd();"
						>
							Cancel
						</button>
						<button
							type="button"
							onclick="onSubmitSegmentAdd();"
							class="btn btn-info pull-right"
						>
							Save
						</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<!-- moneyspent template -->
<div class="row" id="moneyspent-child" hidden>
	<div class="col-md-10 col-md-offset-1">
		<div class="form-group">
			<div class="checkbox">
				<label>
					<input
						type="checkbox"
						name="payinguser"
						id="input-paying"
					/>
					Paying user
				</label>
			</div>
		</div>
		<div class="form-group">
			<label for="moneyspent">Money spent range:</label>
			<div class="row">
				<div class="col-md-6">
					<input
						type="number"
						class="form-control"
						id="moneyspentfrom"
						name="moneyspentfrom"
						placeholder="from"
					/>
				</div>
				<div class="col-md-6">
					<input
						type="number"
						class="form-control"
						id="moneyspentto"
						name="moneyspentto"
						placeholder="to"
					/>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="overlay" id="overlay" hidden>
	<i class="fa fa-refresh fa-spin"></i>
</div>

<script>
	function onSubmitSegmentAdd() {
		showoverlay();
		senddata(
			"segments/add/submit",
			$("#segment-add-form").serializeArray(),
			onsuccess
		);
	}
	function onCancelSegmentAdd() {
		location.href = "#/segments";
	}

	function onsuccess(data) {
		hideoverlay();
		location.href = "#/segments";
	}

	$(".select2").select2();

	$(document)
		.find("#registration")
		.daterangepicker({
			autoUpdateInput: false,
			locale: {
				cancelLabel: "Clear"
			}
		});

	$(document)
		.find("#registration")
		.on("apply.daterangepicker", function(ev, picker) {
			$(this).val(
				picker.startDate.format("MM/DD/YYYY") +
					" - " +
					picker.endDate.format("MM/DD/YYYY")
			);
		});

	$(document)
		.find("#registration")
		.on("cancel.daterangepicker", function(ev, picker) {
			$(this).val("");
		});

	$("#daterange").daterangepicker({
		autoUpdateInput: false,
		locale: {
			cancelLabel: "Clear"
		}
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

	function setIAPsCheckbox() {
		$("#input-iaps").change(function() {
			if ($(this).prop("checked") == true) {
				let root = $("#moneyspent-root");
				let child = $(document)
					.find("#moneyspent-child")
					.clone();
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
