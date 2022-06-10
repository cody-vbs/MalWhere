<?php 

  session_start();
  require_once "../db_config.php";

  if(!isset($_SESSION['username'])){
      header("location:../index.php");
  }

  $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
  $query  = "select category_name, COUNT(*) from url_reports group by category_name";
  $resultArea1 = mysqli_query($conn,$query);
  $resultArea2= mysqli_query($conn,$query);

  $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
  $query  = "select url_source, COUNT(*) from url_reports group by url_source";
  $resultBar1 = mysqli_query($conn,$query);
  $resultBar2= mysqli_query($conn,$query);


  $queryCountReports  = "select * FROM url_reports";
  $result = mysqli_query($conn,$queryCountReports);
  $count = mysqli_num_rows($result);


  // get total scans
  $queryTotalScan = "select * from scan_logs";
  $resultScanQuery = mysqli_query($conn,$queryTotalScan);
  $totalScan = mysqli_num_rows($resultScanQuery);

  // get total unique users
  $queryTotalUsers = "select * from scan_logs group by user";
  $resultUsersQuery = mysqli_query($conn,$queryTotalUsers);
  $totalUsers = mysqli_num_rows($resultUsersQuery);

  // get total reports
  $queryTotalReports = "select * from url_reports";
  $resultReportsQuery = mysqli_query($conn,$queryTotalReports);
  $totalReports = mysqli_num_rows($resultReportsQuery);

  // get total malicious and benign
  $queryTotalPieData = "select scan_result , COUNT(*) from scan_logs group by scan_result";
  $resultPie1 = mysqli_query($conn,$queryTotalPieData);
  $resultPie2 = mysqli_query($conn,$queryTotalPieData);


  

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

</head>

<body class="g-sidenav-show  bg-gradient-dark"">
  <aside class="sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3   bg-gradient-dark" id="sidenav-main">
    <div class="sidenav-header">
      <i class="fas fa-times p-3 cursor-pointer text-white opacity-5 position-absolute end-0 top-0 d-none d-xl-none" aria-hidden="true" id="iconSidenav"></i>
      <a class="navbar-brand m-0" href=" ../pages/dashboard.php" >
        <img src="../assets/icons/logo.png" class="navbar-brand-img h-100" alt="main_logo">
        <span class="ms-1 font-weight-bold text-white">MalWhere</span>
      </a>
    </div>
    <hr class="horizontal light mt-0 mb-2">
    <div class="collapse navbar-collapse  w-auto " id="sidenav-collapse-main">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link text-white active bg-gradient-dark" href="../pages/dashboard.php">
            <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
              <i class="material-icons opacity-10">dashboard</i>
            </div>
            <span class="nav-link-text ms-1">Dashboard</span>
          </a>
        </li>
        
        <li class="nav-item">
          <a class="nav-link text-white" href="../pages/reports.php">
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
          <a class="nav-link text-white" href="#" data-toggle="modal" data-target = "#signoutModal">
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
            <li class="breadcrumb-item text-sm text-white active" aria-current="page">Dashboard</li>
          </ol>
          <h6 class="font-weight-bolder mb-0 text-white">Dashboard</h6>

          
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
          
        </div>
      </div>
    </nav>
    <!-- End Navbar -->
    <div class="container-fluid py-4">
      <div class="row">
        <div class="col-xl-4 col-sm-6 mb-xl-0 mb-6">
          <div class="card">
            <div class="card-header p-3 pt-2 bg-gradient-dark">
              <div class="icon icon-lg icon-shape bg-gradient-dark shadow-dark text-center border-radius-xl mt-n4 position-absolute">
                <i class="material-icons opacity-10">sensors</i>
              </div>
              <div class="text-end pt-1">
                <p class="text-sm mb-0 text-capitalize">Total Scans</p>
                <h4 class="mb-0"><?php echo $totalScan ?> </h4>
              </div>
            </div>
            <hr class="dark horizontal my-0">
            <div class="card-footer p-3 bg-gradient-dark">
            </div>
          </div>
        </div>
        <div class="col-xl-4 col-sm-6 mb-xl-0 mb-6">
          <div class="card">
            <div class="card-header p-3 pt-2 bg-gradient-dark">
              <div class="icon icon-lg icon-shape bg-gradient-primary shadow-primary text-center border-radius-xl mt-n4 position-absolute">
                <i class="material-icons opacity-10">bookmark</i>
              </div>
              <div class="text-end pt-1">
                <p class="text-sm mb-0 text-capitalize">Total Reports</p>
                <h4 class="mb-0"><?php echo $totalReports ?></h4>
              </div>
            </div>
            <hr class="dark horizontal my-0">
            <div class="card-footer p-3 bg-gradient-dark">
            </div>
          </div>
        </div>
        <div class="col-xl-4 col-sm-6 mb-xl-0 mb-6">
          <div class="card">
            <div class="card-header p-3 pt-2 bg-gradient-dark">
              <div class="icon icon-lg icon-shape bg-gradient-success shadow-success text-center border-radius-xl mt-n4 position-absolute">
                <i class="material-icons opacity-10">person</i>
              </div>
              <div class="text-end pt-1">
                <p class="text-sm mb-0 text-capitalize">Total Users</p>
                <h4 class="mb-0"><?php echo $totalUsers ?></h4>
              </div>
            </div>
            <hr class="dark horizontal my-0 ">
            <div class="card-footer p-3 bg-gradient-dark">
            </div>
          </div>
        </div>
        
      </div><br><br><br><br>

      <div class="card mb-3 shadow p-3 mb-5 bg-white rounded bg-gradient-dark">
                <div class="card-header bg-dark text-white">
                  <i class="fas fa-chart-bar"></i>
                  URL Reports Per Source</div>
                <div class="card-body">
                  <canvas id="myBarChart" width="100%" height="50"></canvas>
                </div>
            <div class="card-footer small text-muted" id="ct2"></div>
          </div>


      <!-- charts -->
      <div class="row">
            <div class="col-lg-8">
              <div class="card mb-3 shadow p-3 mb-5 bg-white rounded bg-gradient-dark">
                <div class="card-header bg-dark text-white">
                  <i class="fas fa-chart-bar"></i>
                  URL Reports Per Category</div>
                <div class="card-body">
                  <canvas id="myAreaChart" width="100%" height="50"></canvas>
                </div>
                <div class="card-footer small text-muted" id="ct2"></div>
              </div>
            </div>
            <div class="col-lg-4">
              <div class="card mb-3 shadow p-3 mb-5 bg-white rounded bg-gradient-dark">
                <div class="card-header bg-dark text-white">
                  <i class="fas fa-chart-pie"></i>
                  URL Scan Results (Malicious vs Benign)</div>
                <div class="card-body">
                  <canvas id="myPieChart1" width="100%" height="100"></canvas>
                </div>
                <div class="card-footer small text-muted" id="ct3"></div>
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


  <script>
// Set new default font family and font color to mimic Bootstrap's default styling
// Area Chart Example
var ctx = document.getElementById("myAreaChart");
var myLineChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [<?php while ($row = mysqli_fetch_array($resultArea1)) { echo '"' . $row['category_name']. '",';}?>],
    datasets: [{
      label: "Reports",
      lineTension: 0.3,
      backgroundColor: "rgba(2,117,216,0.2)",
      borderColor: "rgba(2,117,216,1)",
      pointRadius: 5,
      pointBackgroundColor: "rgba(2,117,216,1)",
      pointBorderColor: "rgba(255,255,255,0.8)",
      pointHoverRadius: 5,
      pointHoverBackgroundColor: "rgba(2,117,216,1)",
      pointHitRadius: 50,
      pointBorderWidth: 2,
      data:[<?php while ($row = mysqli_fetch_array($resultArea2)) { echo '"' . $row['COUNT(*)'] . '",';}?>],
    }],
  },
  options: {
    scales: {
      xAxes: [{
        time: {
          unit: 'date'
        },
        gridLines: {
          display: false
        },
        ticks: {
          maxTicksLimit: 7
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max:50,
          maxTicksLimit: <?php echo $count ?>,
        },
        gridLines: {
          color: "rgba(0, 0, 0, .125)",
        }
      }],
    },
    legend: {
      display: false
    }
  }
});
</script>


<script>
  var ctx = document.getElementById("myBarChart");
  var myLineChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: [<?php while ($row = mysqli_fetch_array($resultBar1)) { echo '"' . $row['url_source'] . '",';}?>],
      datasets: [{
        label: "No. of Reports",
        backgroundColor: "#007AC7",
        borderColor: "rgba(2,117,216,1)",
        data: [<?php while ($row = mysqli_fetch_array($resultBar2)) { echo '"' . $row['COUNT(*)'] . '",';}?>],
      },

    
    ],

    },
    options: {
      scales: {
        xAxes: [{
          time: {
            unit: 'month'
          },
          gridLines: {
            display: false
          },
          ticks: {
            maxTicksLimit: 6
          }
        }],
        yAxes: [{
          ticks: {
            min: 0,
            max: <?php echo $count ?>,
            maxTicksLimit: 5
          },
          gridLines: {
            display: true
          }
        }],
      },
      legend: {
        display: true
      }
    }
  });
   </script> <!-- end of bar chart -->

<script>
    var ctx = document.getElementById("myPieChart1");
    var myPieChart = new Chart(ctx, {
      type: 'pie',
    data: {
      labels:  [<?php while ($row = mysqli_fetch_array($resultPie1)) { echo '"' . $row['scan_result']. '",';}?>],
      datasets: [{
        data: [<?php while ($row = mysqli_fetch_array($resultPie2)) { echo '"' . $row['COUNT(*)'] . '",';}?>],
        backgroundColor: ['#008140','#AB0501' ],
      }],
    },
  });
   </script>



</body>

</html>