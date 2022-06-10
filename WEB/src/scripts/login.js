    var audio = new Audio('assets/audio/error_sound.mp3');

    $(document).ready(function() {
        toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-top-right",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
}

        $(function () {
        $("#signIn").click(function (event) {
            var username = $("#inputUsername").val();
            var password = $("#inputPassword").val();
        {
            $.ajax({
            type: "POST",
            url: "ajax_calls/sign_in_user.php",
            data: {username:username,password:password},
            dataType:'json',
            
            beforeSend (){
              $('#signIn').attr('disabled', 'disabled');
              $('#signIn').html('SIGNING IN...')
            },
            success:function(response){
                $('#signIn').attr('disabled', false);
                $('#signIn').html('SIGN IN');
                var len = response.length;

                for( var i = 0; i<len; i++){
                    var login_result = response[i]['login_result'];

                    if(login_result == "success"){

                        window.location = 'pages/dashboard.php'

                    }else if(login_result == "wrong_password"){

                        toastr.error("Sign in failed! Incorrect Password")
                        audio.play();

                    }else if(login_result == "unknownAccount"){
                        toastr.error("Account doesn't exist!")
                        audio.play();
                    }

              
                }


            }
            });

            }
        });
    });
});


function showPassword() {
    var x = document.getElementById("inputPassword");
        if (x.type === "password") {
             x.type = "text";
        } else {
            x.type = "password";
        }
}

