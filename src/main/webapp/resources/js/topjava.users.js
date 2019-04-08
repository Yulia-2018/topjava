// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );
});

let ajaxUrlUser = "ajax/admin/users/";

function updateTable() {
    updateTableCommon();
}

function enable(checkbox, id) {
    let isEnabled = checkbox.prop('checked');
    let request = $.ajax({
        type: "POST",
        url: ajaxUrlUser + id,
        data: {enabled: isEnabled}
    });
    request.done(function () {
        if (isEnabled === true) {
            $('tr#'+id).animate({opacity: '1.0'});
            successNoty("Recording activated");
        } else {
            $('tr#'+id).animate({opacity: '0.3'});
            successNoty("Recording deactivated");
        }
    });
    request.fail(function () {
        checkbox.checked = !isEnabled;
    });
}