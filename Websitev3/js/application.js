$(document).ready(function() {
    // Fancybox with media helper
    $(".fancybox").fancybox({
        helpers : {
            media : {}
        }
    });
    
    // Validation
    $("#theform").validate(
    {
        rules: {
            username: {
                minlength: 2,
                required: true
            },
            email: {
                required: true,
                email: true
            },
            password: {
                minlength: 6,
                required: true
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
        }
    });
                
    // Back-to-top
    var offset = 220;
    var duration = 500;
    jQuery(window).scroll(function() {
        if (jQuery(this).scrollTop() > offset) {
            jQuery('.back-to-top').fadeIn(duration);
        } else {
            jQuery('.back-to-top').fadeOut(duration);
        }
    });
    
    $('.back-to-top').click(function(event) {
        event.preventDefault();
        jQuery('html, body').animate({
            scrollTop: 0
        }, duration);
        return false;
    })
    
    // Local copy of session
    $userData = null;
    $.post("get_user_data.php", function(result) {
        result = JSON.parse(result);
        if (result != null) {
            $userData = result;
        }
    });

    // Datatables
    $("#table").dataTable ( {
        "bDeferRender": true,
        "sPaginationType": "full_numbers",
        "bAutoWidth": false,
        "order": [[ 1, "asc"]],
        "aoColumns": [
            {
                "bSortable": false, 
                "bSearchable": false,
                "sWidth": "14%"
            },
            { "sWidth": "20%" },
            {
                "bSearchable": false,
                "sWidth": "14%"
            },
            { "sWidth": "12%" },
            { "sWidth": "10%" },
            { "sWidth": "10%" },
            { "sWidth": "10%" },
            { "sWidth": "10%" }
        ],
        "fnDrawCallback": function (oSettings) {
            $('.raty').raty({
                score: function() {
                    return $(this).attr("value");
                },
            
                width    : false,
                readOnly : true,
                starHalf : 'images/half_desert3.png',
                iconRange: [
                    { range: 1, on: 'images/colored_pott.png', off: 'images/pot.png' },
                    { range: 2, on: 'images/colored_saladd.png', off: 'images/salad.png' },
                    { range: 3, on: 'images/colored_turkeyy.png', off: 'images/turkey.png' },
                    { range: 4, on: 'images/colored_piee.png', off: 'images/pie.png' },
                    { range: 5, on: 'images/colored_desertt.png', off: 'images/desert.png' }
                ]
            });

            // Disable rate button for anonymous users
            if ($userData == null) {
                $(".addButton").hide();
                $(".rateButton").hide();
            }
            // Disable rate button for courses already rated by logged in user
            else {
                $(".addButton").show();
                $(".rateButton").show();

                for (var courseId in $userData.ratedCourses) {
                    $("#" + courseId).hide();
                    $("#" + courseId).prop("disabled", true);
                }

                for (var courseId in $userData.wishlist) {
                    $("#wish" + courseId).hide();
                    $("#wish" + courseId).prop("disabled", true);
                }
            }
        }
    });

    $.get("get_courses.php", function(result) {
        $("#table").dataTable().fnAddData(JSON.parse(result));
    });

    $('.raty').raty({
        score: function() {
            return $(this).attr("value");
        },
    
        width    : false,
        readOnly : true,
        starHalf : 'images/half_desert3.png',
        iconRange: [
                    { range: 1, on: 'images/colored_pott.png', off: 'images/pot.png' },
                    { range: 2, on: 'images/colored_saladd.png', off: 'images/salad.png' },
                    { range: 3, on: 'images/colored_turkeyy.png', off: 'images/turkey.png' },
                    { range: 4, on: 'images/colored_piee.png', off: 'images/pie.png' },
                    { range: 5, on: 'images/colored_desertt.png', off: 'images/desert.png' }
        ]
    });
});

function prepareRate(courseButton) {
    // My hackish way to keep track of which rate button is clicked on the coursePage
    $("#confirmRateButton").prop("courseId", courseButton.prop("id"));
    $("#confirmRateButton").prop("disabled", true);

    $("#raty-in-modal").raty({
        iconRange: [
                    { range: 1, on: 'images/colored_pott.png', off: 'images/pot.png' },
                    { range: 2, on: 'images/colored_saladd.png', off: 'images/salad.png' },
                    { range: 3, on: 'images/colored_turkeyy.png', off: 'images/turkey.png' },
                    { range: 4, on: 'images/colored_piee.png', off: 'images/pie.png' },
                    { range: 5, on: 'images/colored_desertt.png', off: 'images/desert.png' }
        ],
        score   : 0,
        width   : 120,

        click: function(score, evt) {
            $("#confirmRateButton").prop("disabled", false);
        }
    });
}

function rateCourse() {
    var courseId = $("#confirmRateButton").prop('courseId');
    var rating = $("#raty-in-modal input[name=score]").prop('value');
    
    $.post("rate_course.php", { courseId: courseId, rating: rating }, function(newRating) {
        newRating = JSON.parse(newRating);
        $userData.ratedCourses[courseId] = newRating;
        $("#" + courseId).hide();
        $("#" + courseId).prop("disabled", true);
        $("#raty" + courseId).attr("value", newRating.rating);
        $("#raty" + courseId).html("<span>" + newRating.rating + "</span>");
        $("#raty" + courseId).raty({
            score: function() {
                return $(this).attr("value");
            },

            width    : false,
            readOnly : true,
            starHalf : 'images/half_desert3.png',
            iconRange: [
                    { range: 1, on: 'images/colored_pott.png', off: 'images/pot.png' },
                    { range: 2, on: 'images/colored_saladd.png', off: 'images/salad.png' },
                    { range: 3, on: 'images/colored_turkeyy.png', off: 'images/turkey.png' },
                    { range: 4, on: 'images/colored_piee.png', off: 'images/pie.png' },
                    { range: 5, on: 'images/colored_desertt.png', off: 'images/desert.png' }
            ]
        });
    });
}

function deleteRatedCourses() {
    var rowRating = $("#rated tr[courseId]");
    var checkedCourses = [];

    $("#rated input[type=checkbox]").each(function(index, checkbox) {
        if ($(checkbox).is(':checked')) {
            var checkedRow = rowRating[index];
            checkedCourses.push($(checkedRow).attr('courseId'));
            $(checkedRow).remove();
        }
    });

    if (checkedCourses.length > 0) {
        $.post("delete_rating.php", { deleted_course: checkedCourses });
    }
}

function addCourse(addButton) {
    var courseId = addButton.attr("courseId");

    $.post("add_wishlist.php", { courseId: courseId }, function(addedCourse) {
        addedCourse = JSON.parse(addedCourse);
        addButton.hide();
        $userData.wishlist[courseId] = addedCourse;
    });
}

function deleteWishList() {
    var rowRating = $("#wish tr[courseId]");
    var checkedCourses = [];

    $("#wish input[type=checkbox]").each(function(index, checkbox) {
        if ($(checkbox).is(':checked')) {
            var checkedRow = rowRating[index];
            checkedCourses.push($(checkedRow).attr('courseId'));
            $(checkedRow).remove();
        }
    });

    if (checkedCourses.length > 0) {
        $.post("delete_wishlist.php", { deleted_course: checkedCourses });
    }
}
