<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3 ">
		<div class="box box-default">
			<div class="box-header with-border">
				<i class="fa fa-warning"></i>

				<h3 class="box-title">Archiving</h3>
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<div class="callout callout-danger">
					<h4>Warning!</h4>

					<p>Are you sure you want to archive game?</p>
				</div>
				<button
					type="button"
					class="btn btn-default pull-left"
					onclick="onCancelGameArchive()"
				>
					Cancel
				</button>
				<button
					type="button"
					class="btn btn-danger pull-right"
					onclick="onSubmitGameArchive();"
				>
					Archivate
				</button>
			</div>
			<!-- /.box-body -->
		</div>
	</div>
</div>

<script>
	function onSubmitGameArchive() {
		senddata("games/archive/${game.id}/submit", null, onsuccess);
	}
	function onCancelGameArchive() {
		location.href = "#/games";
	}
	function onsuccess() {
		location.href = "#/games";
	}
</script>
