<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-lg-3 col-xs-6">
	<%-- small box --%>
	<c:set var="background" value="<%=request.getParameter(\"background\")%>" />
	<c:set var="icon" value="<%=request.getParameter(\"icon\")%>" />
	<c:set var="name" value="<%=request.getParameter(\"name\")%>" />
	<c:set var="value" value="<%=request.getParameter(\"value\")%>" />
	<div class="small-box ${background}">
		<div class="inner">
			<h3>${value}</h3>
			<p>${name}</p>
		</div>
		<div class="icon">
			<i class="${icon}"></i>
		</div>
		<a class="small-box-footer"></a>
	</div>
</div>