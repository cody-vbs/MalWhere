<?php 
session_start();
require_once '../db_config.php';

$dbname = DB_NAME;
$pdo = new PDO("mysql:host=localhost;dbname=$dbname", DB_USER, DB_PASS);
$conn = new PDO("mysql:host=localhost;dbname=$dbname", DB_USER, DB_PASS);
$username = filter_var($_POST['username'], FILTER_SANITIZE_STRING);
$password = filter_var($_POST['password'], FILTER_SANITIZE_STRING);


$stmt= $pdo->prepare('SELECT * FROM admins WHERE uname = :uname LIMIT 1');
$stmt->execute([ 'uname' => $username ]);

$resultCheck = $stmt ->rowCount() ;


if(empty($username) || empty($password)){
    $login_response[] = array("login_result" => "empty_fields");

}else{
    if($resultCheck > 0){
        foreach($stmt as $row){
            if(md5($password )== $row['pass']){
    
                $login_response[] = array("login_result" => "success");
                $_SESSION['username'] = $_POST['username'];
                
    
            }else{
                $login_response[] = array("login_result" => "wrong_password");
    
            }
        }
    }else{
        $login_response[] = array("login_result" => "unknownAccount");
    
    }
}

echo json_encode($login_response);

?>