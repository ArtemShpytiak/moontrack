<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<form id="segment-edit-form">
			<div class="box box-default" id="box-container">
				<div class="box-header with-border">
					<h3 class="box-title">Edit segment</h3>
				</div>
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
							value="${segment.name}"
							.
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
							<option selected value="${game_selected.id}"
								>${game_selected.name}</option
							>
							<c:forEach var="g" items="${games}">
								<option value="${g.id}">${g.name}</option>
							</c:forEach>
						</select>
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
							<c:forEach
								var="country"
								items="${countries_selected}"
							>
								<option selected value="${country.code}"
									>${country.name}</option
								>
							</c:forEach>
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
							<c:forEach var="os" items="${oses_selected}">
								<option selected value="${os.code}"
									>${os.name}</option
								>
							</c:forEach>
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
							<c:forEach
								var="platform"
								items="${platforms_selected}"
							>
								<option selected value="${platform.code}"
									>${platform.name}</option
								>
							</c:forEach>
							<c:forEach var="platform" items="${platforms}">
								<option value="${platform.code}"
									>${platform.name}</option
								>
							</c:forEach>
						</select>
					</div>

					<div class="form-group">
						<label class="">
							<div
								class="icheckbox_flat-green"
								aria-checked="false"
								aria-disabled="false"
								style="position: relative;"
							>
								<input
									type="checkbox"
									name="payinguser"
									class="flat-red"
									style="position: absolute; opacity: 0;"
								/>
								<ins
									class="iCheck-helper"
									style="position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background: rgb(255, 255, 255); border: 0px; opacity: 0;"
								></ins>
							</div>
							Paying User
						</label>
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
						<label for="moneyspent">Money spent range:</label>
						<!-- <input
                  type="number"
                  class="form-control"
                  id="moneyspent"
                  name="moneyspentfrom"
                  placeholder="from"
                /> -->
						<c:if test="${empty segment.filter.spentMoneyTo}">
							<input
								type="number"
								class="form-control"
								id="moneyspent"
								name="moneyspentto"
								placeholder="to"
							/>
						</c:if>
						<c:if test="${not empty segment.filter.spentMoneyTo}">
							<input
								type="number"
								class="form-control"
								id="moneyspent"
								name="moneyspentto"
								placeholder="to"
							/>
						</c:if>
					</div>
				</div>

				<div id="moneyspent-root"></div>

				<div class="box-footer">
					<button
						type="button"
						class="btn btn-default pull-left"
						onclick="onCancelSegmentEdit()"
					>
						Cancel
					</button>
					<button
						type="button"
						class="btn btn-info pull-right"
						onclick="onSubmitSegmentEdit()"
					>
						Save
					</button>
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

<script>
	function onSubmitSegmentEdit() {
		showoverlay();
		senddata(
			"segments/edit/${segment.id}/submit",
			$("#segment-edit-form").serializeArray(),
			onsuccess
		);
	}

	function onCancelSegmentEdit() {
		location.href = "#/segments";
	}
	function onsuccess() {
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
