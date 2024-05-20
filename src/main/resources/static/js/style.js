function confirmAction(action) {
    return confirm("Are you sure you want to " + action + "?");
}
$(document).ready(function() {
    $(".sort-buttons a").click(function() {
        $(".sort-buttons a").removeClass("active");
        $(this).addClass("active");
    });
});
