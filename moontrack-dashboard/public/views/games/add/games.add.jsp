<div class="row">
	<div class="col-md-6 col-md-offset-3">
		<form id="game-add-form">
			<div class="box box-info">
				<div class="box-header with-border">
					<h3 class="box-title">Define New Game</h3>
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
							/>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<button
						type="button"
						class="btn btn-default pull-left"
						onclick="onCancelGameAdd()"
					>
						Cancel
					</button>
					<button
						type="button"
						class="btn btn-info pull-right"
						onclick="onSubmitGameAdd()"
					>
						Create
					</button>
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	function onSubmitGameAdd() {
		senddata(
			"games/add/submit",
			$("#game-add-form").serializeArray(),
			onsuccess
		);
	}
	function onCancelGameAdd() {
		location.href = "#/games";
	}
	function onsuccess() {
		location.href = "#/games";
	}
</script>
