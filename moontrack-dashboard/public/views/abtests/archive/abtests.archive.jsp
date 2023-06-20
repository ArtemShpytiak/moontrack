<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3 ">
		<div class="box box-default">
			<div class="box-header with-border">
				<i class="fa fa-warning"></i>

				<h3 class="box-title">Archiving</h3>
			</div>
			<div class="box-body">
				<div class="callout callout-danger">
					<h4>Warning!</h4>

					<p>Are you sure you want to archive abtest?</p>
				</div>
				<button
					type="button"
					class="btn btn-default pull-left"
					onclick="onCancelAbtestArhive()"
				>
					Cancel
				</button>
				<button
					type="button"
					onclick="onSubmitAbtestArchive()"
					class="btn btn-danger pull-right"
				>
					Archivate
				</button>
			</div>
		</div>
	</div>
</div>

<script>
	function onSubmitAbtestArchive() {
		senddata("abtests/archive/${abtest.id}/submit", null, onsuccess);
	}
	function onCancelAbtestArhive() {
		location.href = "#/abtests";
	}
	function onsuccess() {
		location.href = "#/abtests";
	}
</script>
