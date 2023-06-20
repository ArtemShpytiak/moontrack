<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<c:forEach var="game" items="${games}">
		<div class="col-md-6 col-md-offset-3 margin-bottom">
			<div class="panel panel-primary margin-bottom-none">
				<div class="panel-heading clearfix">
					<h4
						class="panel-title pull-left"
						style="padding-top: 7.5px;"
						>${game.name}</h4
					>
					<div class="btn-group pull-right">
						<button
							class="btn btn-block btn-primary margin-bottom"
							onclick="location.href='#/abtests/${game.id}/add';"
						>
							Add
						</button>
					</div>
				</div>
			</div>
			<c:forEach var="abtest" items="${game.getActiveAbtests()}">
				<div class="box margin-bottom-none">
					<div class="box-header">
						<h3 class="box-title">${abtest.name}</h3>
					</div>
					<div class="box-body pad table-responsive">
						<p>Starts: ${abtest.getStartDateFormatted()}</p>
						<p>Ends: ${abtest.getFinishDateFormatted()}</p>
						<div class="btn-group pull-right">
							<!-- <button
								class="btn btn-warning"
								onclick="location.href='#/abtests/edit/${abtest.id}';"
								>Edit</button
							> -->
							<button
								class="btn btn btn-success"
								onclick="location.href='#/abtests/view/${abtest.id}';"
								>View</button
							>
							<button
								class="btn btn btn-danger"
								onclick="location.href='#/abtests/archive/${abtest.id}';"
								>Archive</button
							>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>
