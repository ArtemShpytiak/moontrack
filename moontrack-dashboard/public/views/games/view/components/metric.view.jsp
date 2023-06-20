<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<section class="content">
	<div class="row">
		<div class="col-md-6">
			<%-- general form elements disabled --%>
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">Add metric</h3>
				</div>
				<%-- /.box-header --%>
				<div class="box-body">
					<form role="form" action="index" method="post">

						<div class="form-group">
							<label>Game ID</label>
							<select class="form-control" name="gameIds">
								<c:forTokens items="<%=request.getParameter(\"gameIds\")%>"
									delims=" " var="gameId">
									<option>${gameId}</option>
								</c:forTokens>
							</select>
						</div>
						<div class="form-group">
							<label>Metric type</label>
							<select class="form-control" disabled>
								<option>Daily Revenue</option>
								<option>option 2</option>
								<option>option 3</option>
							</select>
						</div>

						<div class="col-xs-4">
							<button type="submit" value="Login"
								class="btn btn-success btn-block">Submit</button>
						</div>

					</form>
				</div>
				<%-- /.box-body --%>
			</div>
			<%-- /.box --%>
		</div>
	</div>
</section>