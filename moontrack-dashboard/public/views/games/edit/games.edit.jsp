<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<form id="game-edit-form">
			<div class="box box-info" id="box-container">
				<div class="box-header with-border">
					<h3 class="box-title">Edit Game</h3>
				</div>
				<div class="box-body">
					<div class="form-group">
						<label for="gamename" class="col-sm-2 control-label"
							>Game Name</label
						>

						<div class="col-sm-10">
							<input
								type="text"
								class="form-control"
								id="gamename"
								name="gamename"
								placeholder="Name"
								required
								value="${game.name}"
							/>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<button
						type="button"
						class="btn btn-default pull-left"
						onclick="onCancelGameEdit()"
					>
						Cancel
					</button>
					<button
						type="button"
						class="btn btn-info pull-right"
						onclick="onSubmitGameEdit()"
					>
						Save
					</button>
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	function onSubmitGameEdit() {
		senddata(
			"games/edit/${game.id}/submit",
			$("#game-edit-form").serializeArray(),
			onsuccess
		);
	}
	function onCancelGameEdit() {
		location.href = "#/games";
	}
	function onsuccess() {
		location.href = "#/games";
	}
</script>
