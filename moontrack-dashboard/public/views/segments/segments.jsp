<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3 ">
		<button
			class="btn btn-block btn-primary margin-bottom"
			onclick="location.href='#/segments/add';"
		>
			Add
		</button>
	</div>
</div>

<div class="row">
	<div class="col-md-6 col-md-offset-3 margin-bottom">
		<c:forEach var="segment" items="${segments}">
			<div class="box margin-bottom-none">
				<div class="box-header">
					<h3 class="box-title">${segment.name}</h3>
				</div>
				<div class="box-body pad table-responsive">
					<div class="btn-group pull-right">
						<!-- <button
							class="btn btn-warning"
							onclick="location.href='#/segments/edit/${segment.id}';"
							>Edit</button
						> -->
						<button
							class="btn btn btn-success"
							onclick="location.href='#/segments/view/${segment.id}';"
							>View</button
						>
						<button
							class="btn btn btn-danger"
							onclick="location.href='#/segments/archive/${segment.id}';"
							>Archive</button
						>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
