<?php
session_start();
error_reporting(0);
date_default_timezone_set('Asia/Manila');
require_once '../db_config.php';
require_once("../excel_script/db-class.php");
require_once("../excel_script/xlsxwriter.class.php");

ini_set("display_errors", 1);
ini_set("log_errors", 1);
error_reporting(E_ALL & ~E_NOTICE); 

if(!isset($_SESSION['username'])){
  header("location:../index.php");
}


if(isset($_POST['export'])){

function getData() {
  $db = new MY_SQLDB();
  $sql = "SELECT text_url,status from url_reports where not status = 'PENDING' ";
  $rows = $db->get_rows($sql);
  $sheet_titles = $db->get_column_names();
  $data = array_merge(array(), $rows);
  array_unshift($data , $sheet_titles);
  $db->close_connection();
  return $data;
}

  $data = getData();
  $filename = "url_data"."_".date("Y-m-d").".xlsx";

  $writer = new XLSXWriter();
  $writer->writeSheet($data);
  $writer->writeToFile($filename);
  
  
  header('Content-disposition: attachment; filename="'.XLSXWriter::sanitize_filename($filename).'"');
  header("Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  header('Content-Transfer-Encoding: binary');
  header('Cache-Control: must-revalidate');
  header('Pragma: public');
  readfile($filename);
  exit(0);
      
}

include '../ajax_calls/get_report.php';


?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="apple-touch-icon" sizes="76x76" href="../assets/img/apple-icon.png">
  <link rel="icon" type="image/png" href="../assets/icons/logo.png">
  <title>
    MalWhere
  </title>
  <!--     Fonts and icons     -->
  <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,900|Roboto+Slab:400,700" />
  <!-- Nucleo Icons -->
  <link href="../assets/css/nucleo-icons.css" rel="stylesheet" />
  <link href="../assets/css/nucleo-svg.css" rel="stylesheet" />
  <!-- Font Awesome Icons -->
  <script src="https://kit.fontawesome.com/42d5adcbca.js" crossorigin="anonymous"></script>
  <!-- Material Icons -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Round" rel="stylesheet">
  <!-- CSS Files -->
  <link id="pagestyle" href="../assets/css/material-dashboard.css?v=3.0.2" rel="stylesheet" />
  <link rel="stylesheet" href="../assets/css/pace.css">
  <link rel="stylesheet" href="../assets/css/toastr.css">


  <style>

    tr.row_selected td{background-color:#3A3A40 !important;}


  </style>


</head>

<style>
  
  #reportsTable_filter{
    float:right;
  }
  .dataTables_length{
    float:left
    background:red
  }
  .pagination{
    float:right;
    border-radius:0px;
  }
  .page-item{
    border-radius:0px;
  }

  #PENDING{
    font-weight:bolder;
    color:white;
    background:#EE970E;
    padding:5px;
    border-radius:0.2rem;
    font-size:14px;
    }

    #BENIGN{
    font-weight:bolder;
    color:white;
    background:#4CAF50;
    padding:5px;
    border-radius:0.2rem;
    font-size:14px;
    }

    #MALICIOUS{
    font-weight:bolder;
    color:white;
    background:#F44335;
    padding:5px;
    border-radius:0.2rem;
    font-size:14px;
    }



</style>

<body class="g-sidenav-show  bg-gradient-dark">
  <aside class="sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3   bg-gradient-dark" id="sidenav-main">
    <div class="sidenav-header">
      <i class="fas fa-times p-3 cursor-pointer text-white opacity-5 position-absolute end-0 top-0 d-none d-xl-none" aria-hidden="true" id="iconSidenav"></i>
      <a class="navbar-brand m-0" href=" ../pages/dashboard.php">
        <img src="../assets/icons/logo.png" class="navbar-brand-img h-100" alt="main_logo">
        <span class="ms-1 font-weight-bold text-white">MalWhere</span>
      </a>
    </div>
    <hr class="horizontal light mt-0 mb-2">
    <div class="collapse navbar-collapse  w-auto " id="sidenav-collapse-main">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link text-white" href="../pages/dashboard.php">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">dashboard</i>
            </div>
            <span class="nav-link-text ms-1">Dashboard</span>
          </a>
        </li>
        
        <li class="nav-item">
          <a class="nav-link text-white active bg-gradient-dark" href="../pages/reports.php">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">report</i>
            </div>
            <span class="nav-link-text ms-1">Reports</span>
          </a>
        </li>

        <li class="nav-item">
        <a class="nav-link text-white" href="../pages/map_view.php">
          <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
            <i class="material-icons opacity-10">map</i>
          </div>
          <span class="nav-link-text ms-1">Map</span>
        </a>
      </li>

        <li class="nav-item">
          <a class="nav-link text-white" href="#" data-toggle="modal" data-target="#signoutModal">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">transit_enterexit</i>
            </div>
            <span class="nav-link-text ms-1">Logout</span>
          </a>
        </li>

      </ul>
    </div>
  </aside>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
    <!-- Navbar -->
    <nav class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl" id="navbarBlur" navbar-scroll="true">
      <div class="container-fluid py-1 px-3">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb bg-transparent mb-0 pb-0 pt-1 px-0 me-sm-6 me-5">
            <li class="breadcrumb-item text-sm"><a class="opacity-5 text-white" href="javascript:;">Pages</a></li>
            <li class="breadcrumb-item text-sm text-white active" aria-current="page">Reports</li>
          </ol>
          <h6 class="font-weight-bolder mb-0 text-white">Reports</h6>
        </nav>
        
       
          <ul class="navbar-nav  justify-content-end">
            
            <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
              <a href="javascript:;" class="nav-link text-body p-0" id="iconNavbarSidenav">
                <div class="sidenav-toggler-inner">
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                </div>
              </a>
            </li>

            </ul>
          
        </div>
      </div>
    </nav>
    <!-- End Navbar -->


   <div class="container-fluid py-4">


   <div class="card card-body bg-gradient-dark text-white">
            <div class="card-header bg-gradient-dark">
          
            <button class=" btn btn-success" id="approveBtn">Mark as Benign</button>
            <button class=" btn btn-danger" id="rejectBtn">Mark as Malicious</button>
        
          </div>
            <div style=" width: 100%; overflow-y: auto;">
            <table id="reportsTable" class="table text-white">
            <thead class="thead thead-dark bg-gradient-dark">
                <tr>
                <th style="display:none">ID</th>
                <th>Case #</th>
                <th>Category</th>
                <th>Source</th>
                <th>Status</th>
                <th style="display:none">Email</th>
                <th>Image</th>
                <th style="display:none">Text Url</th>
                </tr>
            </thead>
            <tbody>
            <?php foreach($reports as $report) :  ?>
                <tr>
                    <td style="display:none"><?= $report['id'];?></td>
                    <td><?= $report['caseNum']; ?></td>
                    <td><?= $report['category_name']; ?></td>
                    <td><?= $report['url_source']; ?></td>
                    <td><span id="<?= $report['status'] ?>"><?= $report['status'] ?></span></td>  
                    <td style="display:none"><?= $report['email']; ?></td>
                    <td>
                    <a href="#" class="pop">
                    <img id="img" data-toggle="tooltip" title="View image" class="border " width="60px" src="<?= $report['imageurl']; ?>" alt="">
                    </a>
                    </td>
                    <td style="display:none"><?= $report['text_url']; ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
         </div>
         </div><br>
         <form method="post"><button data-toggle="tooltip" title="Download report" class="btn btn-secondary float-right" name="export">
            Download</button></form>
   </div>
   

   
</main>

<!-- modal for image zoom -->
<div>
    <div class="modal fade" id="imagemodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">              
        <div class="modal-body">
          <img src="" class="imagepreview" style="width: 100%;" >
        </div>
      </div>
    </div>
  </div>
  </div>

   <!-- Signout Modal-->
   <div class="modal fade" id="signoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title text-white" id="exampleModalLabel">Ready to Leave?</h5>
            <button class="close" type="button" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true"><i class="fa fa-times-circle" aria-hidden="true"></i></span>
            </button>
          </div>
          <div class="modal-body">Select "Sign out" below if you are ready to end your current session.</div>
          <div class="modal-footer">
            <button class="btn btn-secondary" type="button" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>
            Cancel</button>
            <a class="btn btn-danger" href="../pages/signout.php" id="signout" ><i class="fa fa-sign-out" aria-hidden="true"></i>
            Sign out</a>
          </div>
        </div>
      </div>
    </div>

 <!-- Bootstrap core JavaScript-->
  <script src="../vendor/jquery/jquery.min.js"></script>
  <script src="../vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Core plugin JavaScript-->
  <script src="../vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Page level plugin JavaScript-->
  <script src="../vendor/chart.js/Chart.min.js"></script>
  <script src="../vendor/datatables/jquery.dataTables.js"></script>
  <script src="../vendor/datatables/dataTables.bootstrap4.js"></script>

     
  <!--   Core JS Files   -->
  <script src="../assets/js/core/popper.min.js"></script>
  <script src="../assets/js/core/bootstrap.min.js"></script>
  <script src="../assets/js/plugins/perfect-scrollbar.min.js"></script>
  <script src="../assets/js/plugins/smooth-scrollbar.min.js"></script>
  <script src="../assets/js/plugins/chartjs.min.js"></script>
  <!-- Github buttons -->
  <script async defer src="https://buttons.github.io/buttons.js"></script>
  <!-- Control Center for Material Dashboard: parallax effects, scripts for the example pages etc -->
  <script src="../assets/js/material-dashboard.min.js?v=3.0.2"></script>
  <script src="../assets/js/pace.js"></script>

  <!-- sweet alert dialog -->
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>

  <script src="../assets/js/toastr.js"></script>
  <script src="../scripts/reports.js"></script>


</body>

</html>