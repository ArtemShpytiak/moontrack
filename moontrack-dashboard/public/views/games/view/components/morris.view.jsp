<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <%-- Morris charts --%>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/morris.js/morris.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/public/views/games/view/index.css"
    />
  </head>
  <body>
    <%-- Main content --%>
    <div class="row">
      <div id="metric-container">
        <div class="col-md-6">
          <%-- ADDING CHART --%>
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

        <c:forEach items="${game.getWidgets()}" var="widget">
          <jsp:include page="template-metric-box.jsp" />
        </c:forEach>
      </div>
    </div>
    <%-- /.row --%> <%-- /.content --%> <%-- jQuery 3 --%>
    <script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/jquery/dist/jquery.min.js"></script>

    <%-- Morris.js charts --%>
    <script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/raphael/raphael.min.js"></script>
    <script src="${pageContext.request.contextPath}/webjars/AdminLTE/2.4.2/bower_components/morris.js/morris.min.js"></script>

    <%-- page script --%>

    <jsp:include page="modal.new.metric.jsp" />
    <jsp:include page="modal.filters.jsp" />
    <jsp:include page="template-metric-box.jsp" />
    <jsp:include page="template-money-range.jsp" />
  </body>
</html>
