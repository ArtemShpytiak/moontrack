const GET_ = "${pageContext.request.contextPath}/api/${game.id}/metrics/get";
const ADD_ = "${pageContext.request.contextPath}/api/${game.id}/metrics/add";
const REMOVE_ =
  "${pageContext.request.contextPath}/api/${game.id}/metrics/remove/";

$(document).ready(function() {
  $("#metrics").select2({
    dropdownParent: $("#add-panel")
  });
  $("#games").select2({
    dropdownParent: $("#add-panel")
  });
  $("#countries").select2({
    dropdownParent: $("#add-panel")
  });
  $("#oses").select2({
    dropdownParent: $("#add-panel")
  });
  $("#platforms").select2({
    dropdownParent: $("#add-panel")
  });
  $("#realms").select2({
    dropdownParent: $("#add-panel")
  });
  $("#devices").select2({
    dropdownParent: $("#add-panel")
  });

  $("#registration").daterangepicker({
    autoUpdateInput: false,
    locale: {
      cancelLabel: "Clear"
    }
  });

  $("#registration").on("apply.daterangepicker", function(ev, picker) {
    $(this).val(
      picker.startDate.format("MM/DD/YYYY") +
        " - " +
        picker.endDate.format("MM/DD/YYYY")
    );
  });

  $("#registration").on("cancel.daterangepicker", function(ev, picker) {
    $(this).val("");
  });

  setIAPsCheckbox();

  $(".hl-bg").click(function() {
    $("#add-panel").modal("show");
  });

  let root = $(document).find("#metric-container");
  let len = root.children().length - 1;
  for (let i = 1; i < len; i++) {
    root
      .children()
      .eq(i)
      .attr("hidden", false);
  }

  postData(GET_)
    .then(data => {
      let root = $(document).find("#metric-container");
      let len = root.children().length - 1;
      for (let i = 1, j = 0; j < len; i++, j++) {
        const child = root.children().eq(i);
        $(child).attr("hidden", false);
        data[j].chart["element"] = "chart-" + data[j].widgetId;
        setWidgetBoxData(child, data[j]);
      }
    })
    .catch(error => {
      // console.log(error);
    });

  $("div#add-panel")
    .find("#confirm-button")
    .click(function() {
      const form = $("div#add-panel");
      const tempMetricId = new Date().getTime();

      let box = $("#template-metric-box").clone();
      $(box).attr("hidden", false);
      $(box).attr("id", tempMetricId);
      box.appendTo("#metric-container");

      $("div#add-panel").modal("hide");

      let iaps = {
        paying: null,
        moneyfrom: null,
        moneyto: null
      };

      if (
        $(form)
          .find("#input-iaps")
          .is(":checked")
      ) {
        iaps.paying = $(form)
          .find("#input-paying")
          .is(":checked");
        if (iaps.paying) {
          iaps.moneyfrom = $(form)
            .find("#moneyspentfrom")
            .val();
          iaps.moneyto = $(form)
            .find("#moneyspentto")
            .val();
        }
      }

      let filter = {
        selectedMetricType: form.find("#metrics").val(),
        tempMetricId: tempMetricId,
        game: $("#games").val(),
        countries: $("#countries").val(),
        oses: $("#oses").val(),
        platforms: $("#platforms").val(),
        realms: $("#realms").val(),
        devices: $("#devices").val(),
        registration: $("#registration").val(),
        paying: iaps.paying,
        moneyfrom: iaps.moneyfrom,
        moneyto: iaps.moneyto
      };

      postData(ADD_, { filter })
        .then(data => {
          const widgetBox = $(document).find("#" + data[0].tempMetricId);

          if (widgetBox.length) {
            data[0].chart.element = "chart-" + data[0].widgetId;
            console.log("data[0] = " + JSON.stringify(data[0]));
            setWidgetBoxData(widgetBox, data[0]);
          } else {
            console.log("widget w id no found: " + data[0].tempMetricId);
          }
        }) // JSON-string from `response.json()` call
        .catch(error => {
          // console.error(error);
        });
    });
});

function setWidgetBoxData(widgetBox, data) {
  widgetBox.find(".chart").attr("id", "chart-" + data.widgetId);

  if (data.chartType === "Bar") {
    new Morris.Bar(data.chart);
  } else if (data.chartType === "Line") {
    new Morris.Line(data.chart);
  }

  let btnFilter = widgetBox.find("#btn-filter-widget");
  btnFilter.data("id", data.widgetId);
  // btnFilter.on("click", () => {
  //   $("#filter-panel").modal("show");
  // });

  let btnRemove = widgetBox.find("#btn-remove-widget");
  btnRemove.data("id", data.widgetId);
  btnRemove.on("click", () => {
    widgetBox.boxWidget("remove");
    postData(REMOVE_ + data.widgetId)
      .then(data => {
        // console.log(data);
      })
      .catch(error => {
        // console.log(error);
      });
  });
  widgetBox.attr("id", data.widgetId);
  widgetBox.find(".box-title").text(data.metricName);
  widgetBox.find(".overlay").remove();
}

function postData(url = ``, data = {}) {
  // Default options are marked with *
  return fetch(url, {
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    mode: "cors", // no-cors, cors, *same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, *same-origin, omit
    headers: {
      "Content-Type": "application/json"
      // "Content-Type": "application/x-www-form-urlencoded",
    },
    redirect: "follow", // manual, *follow, error
    referrer: "no-referrer", // no-referrer, *client
    body: JSON.stringify(data) // body data type must match "Content-Type" header
  }).then(response => response.json()); // parses JSON response into native Javascript objects
}

function setIAPsCheckbox() {
  $("#input-iaps").change(function() {
    if ($(this).prop("checked") == true) {
      let root = $("#moneyspent-root");
      let template = $(document).find("#moneyspent-child");
      let child = template.clone();
      child.appendTo(root);
      child.attr("hidden", false);
      setPayingCheckbox(child.find("#input-paying"));
    } else if ($(this).prop("checked") == false) {
      $("#moneyspent-root").empty();
    }
  });
}
function setPayingCheckbox(checkbox) {
  $(checkbox).change(function() {
    if ($(this).prop("checked") == true) {
    } else if ($(this).prop("checked") == false) {
    }
  });
}
