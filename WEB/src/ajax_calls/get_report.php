<?php

$conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
$query  = "SELECT * FROM url_reports";
$reports = array();
$result = mysqli_query($conn,$query);
while ($row = mysqli_fetch_assoc($result)) {
$reports[] = $row;
}


?>