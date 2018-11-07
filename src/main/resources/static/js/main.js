/**
 * Created by Исмаил on 21.10.2018.
 */
jQuery(document).ready(function(){

    $('input[type="file"]').on('change', function (event, files, label) {
        var file_name = this.value;
        alert("Файл выбран: " + file_name);
    });


    $('.file_load_form').submit(function () {
        var form = $(this);
        var field = [];
        alert("Empty field founded");
    });

});

