<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-lg-3 col-xs-6">
	<%-- info box style 2--%>
	<h4>${uiBean.getCurrentInfobox().getName()}</h4>
	<div class="box">
		<div class="box-header ${uiBean.getCurrentInfobox().getBackground()}">
			<h3 class="box-title">All platforms: ${uiBean.getCurrentInfobox().getStringValue()}</h3>
		</div>
		<%-- /.box-header --%>
		<div class="box-body no-padding">
			<table class="table table-condensed">
				<tr>
					<th></th>
					<th style="width: 350px"></th>
					<th></th>
				</tr>
				<c:forEach items="${uiBean.getCurrentInfobox().getRows()}" var="row">
					<tr>
						<td align="center">
							<i class="fa ${row.getIcon()} fa-2x" style="color: #${uiBean.getCurrentInfobox().getColor()}">
								${row.getLetterIcon()}
							</i>
						</td>
						<td>
							<div class="progress progress-xxs">
								<div
									class="progress-bar ${uiBean.getCurrentInfobox().getProgressbar()}"
									style="width: ${row.getPercentage()}%"></div>
							</div>
							<span
								class="progress-description"
								style="color:#${uiBean.getCurrentInfobox().getColor()}">
								${row.getStringValue()}
							</span>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<%-- /.box-body --%>
	</div>
	<%-- /.box --%>
</div>