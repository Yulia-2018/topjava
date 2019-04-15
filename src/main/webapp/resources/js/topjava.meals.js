const mealAjaxUrl = "ajax/profile/meals/";

$.ajaxSetup({
    converters: {
        "text json": function (textValue) {
            let parseJSON = jQuery.parseJSON(textValue);
            $(parseJSON).each(function () {
                this.dateTime = this.dateTime.substring(0, 10) + " " + this.dateTime.substring(11, 16);
            });
            return parseJSON;
        }
    }
});

jQuery('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d'
});
jQuery('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d'
});
jQuery('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i'
});
jQuery('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i'
});
jQuery('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
});