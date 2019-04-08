// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
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
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

let ajaxUrlMeal = "ajax/profile/meals/";

function updateTable() {
    updateFilteredTable();
}

let filterForm = $('#filter');

function clearFilter() {
    filterForm.find(":input").val("");
    updateTableCommon();
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrlMeal + 'filter',
        data: filterForm.serialize()
    }).done(function (data) {
        drawTable(data);
    });
}