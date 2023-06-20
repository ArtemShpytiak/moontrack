<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal fade" id="filter-panel" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button
          type="button"
          class="close"
          data-dismiss="modal"
          aria-label="Close"
        >
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">Add Filters</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="form-group">
            <label>Mertics</label>
            <select
              name="metrics"
              class="form-control"
              data-placeholder="Select a Metric"
              style="width: 100%;"
              id="metrics"
            >
              <c:forEach var="metric" items="${metrics}">
                <option value="${metric.getId()}">${metric.getName()}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button
          type="button"
          class="btn btn-default"
          id="cancel-button"
          data-dismiss="modal"
        >
          Cancel
        </button>
        <button type="button" class="btn btn-primary" id="confirm-button">
          Add
        </button>
      </div>
    </div>
  </div>
</div>
