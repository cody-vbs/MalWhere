<?php
session_start();
require_once '../db_config.php';
require_once('../vendor/autoload.php');
ini_set("SMTP","files.000webhost.com");
ini_set("smtp_port","21");

use Nette\Mail\Message;
use Nette\Mail\SendmailMailer;

$mail = new Message;

$conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);


//get vals
$reportID = mysqli_real_escape_string($conn,$_POST['reportID']);
$reporter = mysqli_real_escape_string($conn,$_POST['reporter']);
$url = mysqli_real_escape_string($conn,$_POST['url']);
$caseNum = mysqli_real_escape_string($conn,$_POST['caseNum']);
$category = mysqli_real_escape_string($conn,$_POST['category']);
$email = mysqli_real_escape_string($conn,$_POST['email']);

$messageBody  = "<p>Report ID: $caseNum</p><p>Reported URL: $url</p><p>Classification: Malicious </p>
<p>The reported url is 'Benign'. We appreciate you for letting us know about this url.This information helped us improve MalWhere.</p>
<p>Til your next report</p><br><p>-MalWhere Team</p>
<p style='font-style:italic'>Note: Do not reply to this email. This is a system generated email</p>";


$query = "UPDATE url_reports SET status = 'BENIGN' where id = '$reportID'";

if(mysqli_query($conn,$query) == TRUE){
    echo "Approve was successful";
    
    $mail->setFrom('malwhereteam@gmail.com', 'MalWhere')
    	->addTo($email)
    	->setSubject('Thank you for your report')
        ->setHTMLBody($messageBody);
        
        $mailer = new Nette\Mail\SmtpMailer([
    	'host' => 'smtp.gmail.com',
    	'username' => 'malwhereteam@gmail.com',
    	'password' => '@MalWhere123',
    	'secure' => 'ssl',
      ]);
      $mailer->send($mail);


}else{
    echo $conn->error;
}

$conn -> close();


?>