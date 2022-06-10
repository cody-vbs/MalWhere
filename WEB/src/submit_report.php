<?php

include_once 'db_config.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $image = $_POST['imageurl'];
    $category = $_POST['category'];
    $source = $_POST['url_source'];
    $reportCaseNumber = $_POST['caseNum'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    $status = "PENDING";
    $lat = floatval($_POST['lat']);
    $lng = floatval($_POST['lng']);
    $textUrl = $_POST['textUrl'];

    $con = mysqli_connect(DB_HOST,DB_USER,DB_PASS,DB_NAME);

    $sql = "SELECT id from url_reports ORDER BY id ASC";

    $res = mysqli_query($con,$sql);

    $id = 0;

    while($row = mysqli_fetch_array($res)){

        $id = $row['id'];

    }

    $p = "uploads/$id.jpeg";

    $path = ROOT_URL."$p";

    $sqli = "INSERT INTO url_reports (caseNum,reporter_name,category_name,url_source,email,imageurl,text_url,status,lat,lng)
     VALUES ('$reportCaseNumber','$name','$category','$source','$email','$path','$textUrl','$status','$lat','$lng')";

    if(mysqli_query($con, $sqli)){
        file_put_contents($p,base64_decode($image));

        echo "Successfully submitted";

    }else{
        echo $con -> error;
    }

    mysqli_close($con);

}



?>