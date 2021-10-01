function readURL(input, targetId) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            console.log(e.target.result, document.getElementById(targetId))
            document.getElementById(targetId).src = e.target.result
        };

        reader.readAsDataURL(input.files[0]);
    }
}