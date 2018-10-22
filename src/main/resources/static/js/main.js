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
        alert("dsajsfdas");
        var field = [];
        alert("Empty field founded");
        // form.find('input[data-validate]').each(function () {
        //     field.push('input[data-validate]');
        //     var value = $(this).val(),
        //         line = $(this).closest('.some-form__line');
        //     for (var i = 0; i < field.length; i++) {
        //         if (!value) {
        //
        //             setTimeout(function () {
        //                 line.removeClass('some-form__line-required')
        //             }.bind(this), 2000);
        //             event.preventDefault();
        //         }
        //     }
        // });
    });

});

