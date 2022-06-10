<?php

include_once 'db_config.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $user = $_POST['user'];
    $scanResult = $_POST['scanResult'];
    $timestamp = $_POST['timestamp'];
    

    $con = mysqli_connect(DB_HOST,DB_USER,DB_PASS,DB_NAME);


    $sqli = "INSERT INTO scan_logs (user,scan_result,timestamp) 
    VALUES ('$user','$scanResult','$timestamp')"; 

    if(mysqli_query($con, $sqli)){

        echo "Successfully submitted";

    }else{
        echo $con -> error;
    }

    mysqli_close($con);

}



?>