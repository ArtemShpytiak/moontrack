<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
	<div class="col-md-10 col-md-offset-1" id="${param.metric}">
		<div class="box box-info">
			<div class="box-header with-border">
				<h3 class="box-title"></h3>
			</div>
			<div class="box-body chart-responsive">
				<div
					class="chart"
					id="${param.chart}"
					style="width: auto; height: 300px;"
				></div>
			</div>
			<div class="overlay">
				<i class="fa fa-refresh fa-spin"></i>
			</div>
		</div>
	</div>
</div>
