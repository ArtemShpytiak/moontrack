<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal fade" id="add-metric" tabindex="-1" role="dialog">
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
        <h4 class="modal-title">Add Metric</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-12">
            <div class="form-group">
              <label>Mertics</label>
              <select
                name="metrics"
                class="form-control select2 select2-hidden-accessible"
                data-placeholder="Select a Metric"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="metrics"
              >
                <c:forEach var="metric" items="${metrics}">
                  <option value="${metric.getId()}">${metric.getName()}</option>
                </c:forEach>
              </select>
            </div>

            <div class="form-group">
              <label>Countries</label>
              <select
                name="country"
                class="form-control select2 select2-hidden-accessible"
                multiple=""
                data-placeholder="Select a Country"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="countries"
              >
                <c:forEach var="country" items="${countries}">
                  <option value="${country.code}">${country.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>OS</label>
              <select
                name="os"
                class="form-control select2 select2-hidden-accessible"
                multiple=""
                data-placeholder="Select an OS"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="oses"
              >
                <c:forEach var="os" items="${oses}">
                  <option value="${os.code}">${os.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Platform</label>
              <select
                name="platform"
                class="form-control select2 select2-hidden-accessible"
                multiple=""
                data-placeholder="Select a Platform"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="platforms"
              >
                <c:forEach var="platform" items="${platforms}">
                  <option value="${platform.code}">${platform.name}</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label>Realm</label>
              <select
                name="realms"
                class="form-control select2 select2-hidden-accessible"
                multiple=""
                data-placeholder="Select a realm"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="realms"
                disabled
              >
                <!-- <c:forEach var="realm" items="${realms}">
                <option value="${realm.realm}">${realm.realm}</option>
              </c:forEach> -->
              </select>
            </div>
            <div class="form-group">
              <label>Device</label>
              <select
                name="devices"
                class="form-control select2 select2-hidden-accessible"
                multiple=""
                data-placeholder="Select a device"
                style="width: 100%;"
                tabindex="-1"
                aria-hidden="true"
                id="devices"
                disabled
              >
                <!-- <c:forEach var="device" items="${devices}">
                  <option value="${device.name}">${device.name}</option>
                </c:forEach> -->
              </select>
            </div>
            <div class="form-group">
              <div class="checkbox">
                <label>
                  <input type="checkbox" name="iaps" id="input-iaps" />
                  IAPs
                </label>
              </div>
            </div>
            <div id="moneyspent-root"></div>
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
