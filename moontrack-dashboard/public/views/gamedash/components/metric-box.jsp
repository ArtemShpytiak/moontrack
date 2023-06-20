<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-md-6" id="${param["widget"]}" hidden>
	<div class="box box-info">
		<div class="box-header with-border">
			<h3 class="box-title"></h3>
			<div class="box-tools pull-right">
				<!-- <button
					type="button"
					class="btn btn-box-tool"
					id="btn-filter-widget"
				>
					<i class="fa fa-filter"></i>
				</button> -->
				<button
					type="button"
					class="btn btn-box-tool"
					data-widget="remove"
					id="btn-remove-widget"
				>
					<i class="fa fa-times"></i>
				</button>
			</div>
		</div>
		<div class="box-body chart-responsive">
			<div class="chart" style="height: 300px;"></div>
		</div>
		<div class="overlay">
			<i class="fa fa-refresh fa-spin"></i>
		</div>
	</div>
</div>
