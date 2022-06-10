 
$(document).ready( function () {
 var counter = 0;
 var audio = new Audio('../assets/audio/error_sound.mp3');

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


  var table =  $('#reportsTable').DataTable({
       language: {
       searchPlaceholder: "Search report..."
   },
   filterDropDown: {										
                       columns: [
                           { 
                               idx: 3
             },
             
                       ],
                       bootstrap: true
                   },
     "lengthChange": false,
     "ordering": false,
   
   });    

   $("#approveBtn").click(function(){

     if(counter == 0){
         toastr.error("Please select a row");
         audio.play();
     }
     

   });

   $("#rejectBtn").click(function(){

     if(counter == 0){
         toastr.error("Please select a row");
         audio.play();
     }


});

   $('#reportsTable tbody').on('click', 'tr', function () {
       var data = table.row( this ).data();
       $("#reportsTable tbody tr").removeClass('row_selected');        
       $(this).addClass('row_selected');


       var reportID = data[0];
       var email = data[5];
       var url = data[7];
       var caseNum = data[1];
       var category = data[2];

       counter++;


       $("#approveBtn").click(function(){
         Swal.fire({
         title: 'Confirm Benign URL',
         text: "Are you sure you want to perform this action?",
         icon: 'warning',
         showCancelButton: true,
         confirmButtonColor: '#FF5252',
         cancelButtonColor: '#CFD4D7',
         confirmButtonText: 'Approve'
       }).then((result) => {
         if (result.isConfirmed) {

          $.ajax({
             type:"POST",
             url:"../ajax_calls/benign_url.php",
             data:{reportID:reportID,email:email,url:url,caseNum:caseNum,category:category},
             dataType:"text",
             success:function(){

               Swal.fire({
                   title: 'Success',
                   text: " Operation was successful",
                   icon: 'success',
                   confirmButtonColor: '#FF5252',
                   confirmButtonText: 'Ok'
                   }).then((result) => {
                   if (result.value) {
                   location.reload();
                   }
                   })

             }
          });

         }

       })
       });

       $("#rejectBtn").click(function(){
         Swal.fire({
         title: 'Confirm Malicious URL',
         text: "Are you sure you want to perform this action?",
         icon: 'warning',
         showCancelButton: true,
         confirmButtonColor: '#FF5252',
         cancelButtonColor: '#CFD4D7',
         confirmButtonText: 'Ok'
       }).then((result) => {
         if (result.isConfirmed) {

           $.ajax({
             type:"POST",
             url:"../ajax_calls/malicious_url.php",
             data:{reportID:reportID,email:email,url:url,caseNum:caseNum,category:category},
             dataType:"text",
             success:function(){

               Swal.fire({
                   title: 'Success',
                   text: "Operation was successful",
                   icon: 'success',
                   confirmButtonColor: '#FF5252',
                   confirmButtonText: 'Ok'
                   }).then((result) => {
                   if (result.value) {
                   location.reload();
                   }
                   })

             }
          });
         }
         


       })
       });

       
       

   
       
   });



});




$(function() {
       $('.pop').on('click', function() {
           $('.imagepreview').attr('src', $(this).find('img').attr('src'));
           $('#imagemodal').modal('show');   
       });		
});

// script for notification if new record is inserted 

var count = -1;
  setInterval(function(){    
      $.ajax({
          type : "POST",
          url : "../ajax_calls/notify.php",
          success : function(response){
              if (count != -1 && count != response) 
               
              Swal.fire({
              title: 'MalWhere',
              text: 'New report/s has been added.',
              icon: 'warning',
              confirmButtonColor: '#FF5252',
              confirmButtonText: 'Got it!',
            }).then((result) => {
              if (result.value) {
              location.reload();
              } 
            })
              count = response;
          }
      });
  },1000);
  

