<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
	<div id="metric-container">
		<div class="col-md-6">
			<div class="box box-default">
				<div class="box-header with-border">
					<h3></h3>
				</div>
				<div class="hl-bg">
					<div class="box-body" style="height: 320px;">
						<i class="fa fa-fw fa-plus"></i>
						<span>Add new metric</span>
					</div>
				</div>
			</div>
		</div>

		<c:forEach items="${widgets}" var="widget">
			<jsp:include page="/public/views/dash/components/metric-box.jsp"
				><jsp:param name="widget" value="${widget.getId()}" />
			</jsp:include>
		</c:forEach>
	</div>
</div>

<jsp:include page="/public/views/gamedash/components/modal.new.metric.jsp" />
<jsp:include page="/public/views/gamedash/components/modal.filters.jsp" />
<jsp:include page="/public/views/gamedash/components/template-metric-box.jsp" />
<jsp:include page="/public/views/gamedash/components/template-money-range.jsp" />
