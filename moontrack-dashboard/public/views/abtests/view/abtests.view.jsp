<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-10 col-md-offset-1 ">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">${abtest.name} [ID: ${abtest.id}]</h3>
			</div>
			<div class="box-body pad table-responsive">
				<p>Starts: ${abtest.getStartDateFormatted()}</p>
				<p>Ends: ${abtest.getFinishDateFormatted()}</p>

				<p>Groups:</p>
				<c:forEach items="${abtest.groups}" var="group">
					<p>
						${group.name} [ID: ${group.id}]
					</p>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

<c:forEach var="element" items="${elementids}">
	<jsp:include page="/public/views/abtests/view/components/metric-box.jsp">
		<jsp:param name="metric" value="${element[0]}" />
		<jsp:param name="chart" value="${element[1]}" />
	</jsp:include>
</c:forEach>

<jsp:include page="/public/views/abtests/view/script.jsp" />
