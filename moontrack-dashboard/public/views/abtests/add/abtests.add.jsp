<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<form id="abtest-add-form">
			<div class="box box-info" id="box-container">
				<div class="box-header with-border">
					<h3 class="box-title">New A/B Test</h3>
				</div>
				<div class="box-body">
					<div class="form-group">
						<label for="abtestname">Name</label>
						<input
							type="text"
							class="form-control"
							id="abtestname"
							name="abtestname"
							placeholder="Name"
							required
						/>
					</div>

					<div class="form-group">
						<label>Game</label>
						<select
							class="form-control"
							id="game"
							name="game"
							,
							disabled
						>
							<option value="${game.id}" selected
								>${game.name}</option
							>
						</select>
					</div>

					<div class="form-group">
						<label>Range of activeness:</label>

						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input
								name="activeness"
								type="text"
								class="form-control pull-right"
								id="activeness"
								required
							/>
						</div>
					</div>

					<ul
						class="nav nav-tabs"
						id="group-selection"
						style="margin-bottom: 15px;"
					>
						<li class="active">
							<a data-toggle="tab" href="#filter">by filters</a>
						</li>
						<li>
							<a data-toggle="tab" href="#percentage"
								>by percentage</a
							>
						</li>
					</ul>
					<div class="tab-content">
						<div id="filter" class="tab-pane fade in active">
							<div id="tab-content-group"></div>
							<button
								type="button"
								class="btn btn-block btn-info"
								onclick="addFilterGroup()"
							>
								Add group by defining new filter
							</button>
							<!-- <p class="text-center">- OR -</p>
                <p class="text-center">Add group by choosing segment</p>
                <div class="form-group">
                  <select class="form-control">
                    <c:forEach var="segment" items="${segments}">
                      <option value="${segment.id}">${segment.name}</option>
                    </c:forEach>
                  </select>
                  <button
                    type="button"
                    class="btn btn-block btn-info"
                    onclick="addSegmentGroup()"
                  >
                    Add segment
                  </button>
                </div> -->
						</div>
						<div id="percentage" class="tab-pane">
							<div id="tab-content-group"></div>
							<button
								type="button"
								class="btn btn-block btn-info"
								onclick="addGroupByPercentage()"
							>
								Add group by defining percentage
							</button>
						</div>
					</div>
					<div class="box-footer">
						<button
							type="button"
							class="btn btn-default pull-left"
							onclick="onCancelAbtestAdd()"
						>
							Cancel
						</button>
						<button
							type="button"
							onclick="onSubmitAbtestAdd()"
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

<!--template-percent-->
<div id="template-percent">
	<div id="box-group-percent" class="col-md-12" hidden>
		<div class="box box-success box-solid">
			<div class="box-header with-border">
				<h3 id="box-title-group" class="box-title"></h3>
				<div class="box-tools pull-right">
					<button
						type="button"
						class="btn btn-box-tool"
						onclick="collapse()"
					>
						<i class="fa fa-minus"></i>
					</button>
					<button
						type="button"
						class="btn btn-box-tool"
						id="box-btn-remove"
						onclick="removeGroupByPercentage(this)"
					>
						<i class="fa fa-times"></i>
					</button>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label>Name</label>
							<input
								id="input-group-name"
								type="text"
								class="form-control"
								name="percentage-name"
								placeholder="Name"
								required
							/>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label>Percent</label>
							<input
								id="input-percent-value"
								type="number"
								step="any"
								min="0"
								max="100"
								value="0"
								class="form-control"
								name="percentage"
								placeholder="Percent"
								required
							/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!--template-filter-->
<div id="box-group-filter" class="col-md-12" hidden>
	<div class="box box-success box-solid">
		<div class="box-header with-border">
			<h3 id="box-title-group" class="box-title"></h3>
			<div class="box-tools pull-right">
				<button
					type="button"
					class="btn btn-box-tool"
					onclick="collapse()"
				>
					<i class="fa fa-minus"></i>
				</button>
				<button
					type="button"
					class="btn btn-box-tool"
					id="box-btn-remove"
					onclick="removeFilter(this)"
				>
					<i class="fa fa-times"></i>
				</button>
			</div>
		</div>
		<div class="box-body">
			<div class="form-group">
				<label>Name</label>
				<input
					type="text"
					class="form-control"
					id="input-group-name"
					name=""
					placeholder="Name"
					required=""
				/>
			</div>
			<div class="form-group" id="form-group-country">
				<label>Country</label>
			</div>
			<div class="form-group" id="form-group-os">
				<label>OS</label>
			</div>
			<div class="form-group" id="form-group-platform">
				<label>Platform</label>
			</div>
			<div class="form-group" id="form-group-realm">
				<label>Realm</label>
			</div>
			<div class="form-group" id="form-group-device">
				<label>Device</label>
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
						<input type="checkbox" name="iaps" id="input-iaps" />
						IAPs
					</label>
				</div>
			</div>
			<div id="moneyspent-root"></div>
		</div>
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

<!--select2 templates-->
<div hidden id="select-templates">
	<select
		name="countries"
		class="form-control select2 select2-hidden-accessible"
		multiple=""
		data-placeholder="Select a Country"
		style="width: 100%;"
		tabindex="-1"
		aria-hidden="true"
		id="countries"
	>
		<c:forEach var="country" items="${countries}">
			<option value="${country.code}">${country.name}</option>
		</c:forEach>
	</select>
	<select
		name="oses"
		class="form-control select2 select2-hidden-accessible"
		multiple=""
		data-placeholder="Select an OS"
		style="width: 100%;"
		tabindex="-1"
		aria-hidden="true"
		id="oses"
	>
		<c:forEach var="os" items="${oses}">
			<option value="${os.code}">${os.name}</option>
		</c:forEach>
	</select>
	<select
		name="platforms"
		class="form-control select2 select2-hidden-accessible"
		multiple=""
		data-placeholder="Select a platform"
		style="width: 100%;"
		tabindex="-1"
		aria-hidden="true"
		id="platforms"
		hidden
	>
		<c:forEach var="platform" items="${platforms}">
			<option value="${platform.code}">${platform.name}</option>
		</c:forEach>
	</select>
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

<div class="overlay" id="overlay" hidden>
	<i class="fa fa-refresh fa-spin"></i>
</div>

<jsp:include page="/public/views/abtests/add/script.jsp" />
