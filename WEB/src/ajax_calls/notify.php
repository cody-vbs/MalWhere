<?php
require_once '../db_config.php';

$conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);

$sql = "SELECT * FROM url_reports";
$qry = mysqli_query($conn,$sql);
$res = mysqli_num_rows($qry);   
echo $res;

?>