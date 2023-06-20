<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<div class="box box-info">
			<div class="box-header with-border">
				<h3 class="box-title">Edit ${abtest.name}</h3>
			</div>
			<form id="abtest-edit-form">
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
							value="${abtest.name}"
						/>
					</div>

					<div class="form-group">
						<label>Game</label>
						<input
							type="text"
							class="form-control"
							placeholder="${game.name}"
							disabled
						/>
					</div>

					<div class="form-group">
						<label for="percentage">Percentage</label>
						<input
							type="number"
							step="any"
							min="0"
							max="100"
							value="${abtest.percentage}"
							class="form-control"
							name="percentage"
						/>
					</div>

					<div class="form-group">
						<label for="segmentList">Segment</label>
						<select
							id="segmentList"
							name="segment"
							class="form-control"
						>
							<c:choose>
								<c:when test="${selectedSegment == null}">
									<option value="" selected
										>Without segment</option
									>
								</c:when>
								<c:otherwise>
									<option value="">Without segment</option>
								</c:otherwise>
							</c:choose>
							<c:forEach var="segment" items="${segmentList}">
								<c:choose>
									<c:when
										test="${segment.id == selectedSegment.id}"
									>
										<option value="${segment.id}" selected
											>${segment.name}</option
										>
									</c:when>
									<c:otherwise>
										<option value="${segment.id}"
											>${segment.name}</option
										>
									</c:otherwise>
								</c:choose>
							</c:forEach>
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
							/>
						</div>
						<!-- /.input group -->
					</div>
				</div>
				<!-- /.box-body -->
				<div class="box-footer">
					<input
						type="button"
						class="btn btn-default pull-left"
						onclick="onCancelAbtestEdit()"
						value="Cancel"
					/>
					<button
						type="button"
						class="btn btn-info pull-right"
						onclick="onSubmitAbtestEdit()"
					>
						Save
					</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	function onSubmitAbtestEdit() {
		senddata(
			"abtests/edit/${abtest.id}/submit",
			$("#abtest-edit-form").serializeArray(),
			onsuccess
		);
	}
	function onCancelAbtestEdit() {
		location.href = "#/abtests";
	}
	function onsuccess() {
		location.href = "#/abtests";
	}
</script>
