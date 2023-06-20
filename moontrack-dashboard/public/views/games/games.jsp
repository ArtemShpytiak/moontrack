<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3 ">
		<button
			class="btn btn-block btn-primary margin-bottom"
			onclick="location.href='#/games/add';"
		>
			Add
		</button>
	</div>
</div>

<div class="row">
	<c:forEach var="game" items="${games}">
		<div class="col-md-6 col-md-offset-3">
			<div class="box box-primary">
				<div class="box-header">
					<h3>${game.name}</h3>
				</div>
				<div class="box-body pad table-responsive">
					<div class="btn-group pull-right">
						<button
							class="btn btn-warning"
							onclick="location.href='#/games/edit/${game.id}';"
							>Edit</button
						>
						<!-- <button
							class="btn btn btn-success"
							onclick="location.href='#/games/view/${game.id}';"
							>View</button
						>
						<button
							class="btn btn btn-danger"
							onclick="location.href='#/games/archive/${game.id}';"
							>Archive</button
						> -->
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</div>
