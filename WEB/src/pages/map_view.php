
<?php 

session_start();
require_once '../db_config.php';

if(!isset($_SESSION['username'])){
  header("location:../index.php");
}

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
<script src='https://api.mapbox.com/mapbox-gl-js/v2.8.1/mapbox-gl.js'></script>
<link href='https://api.mapbox.com/mapbox-gl-js/v2.8.1/mapbox-gl.css' rel='stylesheet' />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script> 
<link rel="stylesheet" href="../assets/css/pace.css">

<style>
  #map {position: absolute; 
        top: 0; 
        bottom: 0; 
        width: 100%;}

  .marker {
        background-image: url('../assets/icons/marker.png');
        background-size: cover;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        cursor: pointer;
    }

  .mapboxgl-popup {
      max-width: 200px;
  }
</style>


</head>
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
        <a class="nav-link text-white" href="../pages/reports.php">
          <div class="text-white text-center me-2 d-flex align-items-center justify-content-center">
            <i class="material-icons opacity-10">report</i>
          </div>
          <span class="nav-link-text ms-1">Reports</span>
        </a>
      </li>

      <li class="nav-item">
        <a class="nav-link text-white active bg-gradient-dark" href="../pages/map_view.php">
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
          <li class="breadcrumb-item text-sm"><a class="opacity-5 text-dark" href="javascript:;">Pages</a></li>
          <li class="breadcrumb-item text-sm text-white active" aria-current="page">Map</li>
        </ol>
        <h6 class="font-weight-bolder mb-0">Map</h6>

        
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
</div>

</main>

<div id="locations" style="display:none" >
  <?php
      $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);

      $sql = "SELECT * FROM url_reports";
      $dbquery = mysqli_query($conn, $sql);

      if (!$dbquery) {
          echo 'A SQL error occured.\n';
          exit;
      }

      $geojson = array(
        'type'  => 'FeatureCollection',
        'features'  => array() 
      );
        ini_set('serialize_precision', -1);
        while($row = mysqli_fetch_assoc($dbquery)){
                $properties = $row;
                unset($properties['lng']);
                unset($properties['lat']);
                $feature = array(
                            'type'  => 'Feature',
                            'geometry'  => array(
                                    'type'  => 'Point',
                                    'coordinates' => array((float)
                                        $row["lng"], (float)
                                        $row["lat"]
                                      )
                                    ),
                            'properties'  => $properties
                );
                array_push($geojson['features'], $feature);
        }
        echo json_encode($geojson, JSON_PRETTY_PRINT);
        $conn = NULL;

  ?>
  </div>

<div id='map'></div>

<script>
mapboxgl.accessToken ="pk.eyJ1IjoiZGV2c3lkMTEiLCJhIjoiY2wyYWx5MnRtMDZiYzNqbm9vdnFvMzczMiJ9.U6Ysks0cXq8TZNeSeM-UyQ";

var map = new mapboxgl.Map({
  container: 'map',
  style: 'mapbox://styles/mapbox/dark-v10',
  center: [121.7740, 12.8797],
  zoom: 5
});

var geojson = <?php echo json_encode($geojson,JSON_NUMERIC_CHECK); ?>;

  // add markers to map
  geojson.features.forEach(function(marker) {

    // create a HTML element for each feature
    var el = document.createElement('div');
    el.className = 'marker';

    var finalImageUrl =marker.properties.imageurl.replace(/\\/g, '');


    // make a marker for each feature and add to the map
    new mapboxgl.Marker(el)
      .setLngLat(marker.geometry.coordinates)
      .setPopup(new mapboxgl.Popup({ offset: 25 }) // add popups
      .setHTML('<strong>Reporter: </strong>' + marker.properties.reporter_name + '<br>' + '<strong>Category: </strong>' + marker.properties.category_name + '<br>' + '<strong>Status: </strong>'
      + marker.properties.status + "<br>" +  '<strong>Source: </strong>' + marker.properties.url_source + '<br>' +  '<img src= "' + finalImageUrl + '" width="200" height="250" >'))
      .addTo(map);
      });   
</script>

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
</body>

</html>