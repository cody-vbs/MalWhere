package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.mateware.snacky.Snacky;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Reports extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks, LocationListener {
    private DrawerLayout drawer;
    private static final String TAG = "ReportsActivity";

    private static final String URL = new Adapter().SUBMIT_REPORT_URL_ONLINE;

    GoogleConfig googleConfig = new GoogleConfig();

    ImageView urlImage;
    Button submitBtn;
    MaterialSpinner categorySpinner,sourceSpinner;
    EditText description;
    FloatingActionButton fabAddImage;
    TextView capturedDateTime;

    Bitmap imageBitmap;
    String timeStamp,caseNumber,tempLat,tempLng;
    String longTxt;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences,guestSharedPreference;

    StringBuilder recognizedText;
    Uri imageUri;

    //location
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    String myURl = "";

    boolean hasImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        urlImage = findViewById(R.id.image_photo);
        submitBtn = findViewById(R.id.submit);
        categorySpinner = findViewById(R.id.categorySpinner);
        sourceSpinner = findViewById(R.id.sourceSpinner);
        fabAddImage = findViewById(R.id.fab_add);
        capturedDateTime = findViewById(R.id.capturedDate);

        //set the current item
        navigationView.getMenu().getItem(2).setChecked(true);

        googleConfig.configureGoogleClient(this);

        //sharedpreference
        sharedPreferences = getSharedPreferences(new Adapter().MainUserPresf,MODE_PRIVATE);
        guestSharedPreference = getSharedPreferences(new Adapter().MyGuestPresf, MODE_PRIVATE);

        initCategorySpinnerItems();
        initSourceSpinnerItems();

        TextView tvsArr [] = {capturedDateTime};

        MaterialSpinner materialSpinnerArr[] = {categorySpinner,sourceSpinner};

        //set custom font UI
        new CustomUI().setTextViewFontOnluFamily(this,tvsArr);
        new CustomUI().setSpinnerFont(this,materialSpinnerArr);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

        //get user location
        getLocation();

        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionOpenCam();

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Reports.this);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                validate();
            }
        });

    }

    //request permission methods
    private void requestPermissionOpenCam(){
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //permission granted
        } else {
            EasyPermissions.requestPermissions(this, "Please allow all permissions to use MalWhere",
                    123, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                imageUri = result.getUri();

                hasImage = true;

                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    urlImage.setImageBitmap(imageBitmap);

                    //get the captured time
                   timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                   capturedDateTime.setText("Captured on: " + timeStamp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //display error message
                Snacky.builder()
                        .setText(result.getError().toString())
                        .setIcon(R.drawable.ic_error_outline_black_24dp)
                        .error()
                        .show();
            }
        }

    }

    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        String encodeimg = Base64.encodeToString(imgBytes,Base64.DEFAULT);
        return encodeimg;
    }

    //method for input validation

    private void validate() {

        if(hasImage == false) {
            Snacky.builder()
                    .setView(getWindow().getDecorView().getRootView())
                    .setTextColor(getResources().getColor(R.color.white))
                    .setText("Please add an image!")
                    .warning()
                    .show();

        }else if (categorySpinner.getText().toString().equalsIgnoreCase("Select Category")) {
            Snacky.builder()
                    .setView(getWindow().getDecorView().getRootView())
                    .setTextColor(getResources().getColor(R.color.white))
                    .setText("Please select a category!")
                    .warning()
                    .show();
        }else if(sourceSpinner.getText().toString().equalsIgnoreCase("Select Source (Where the URL is found)")){
            Snacky.builder()
                    .setView(getWindow().getDecorView().getRootView())
                    .setTextColor(getResources().getColor(R.color.white))
                    .setText("Please select a source!")
                    .warning()
                    .show();

        }else{
            caseNumber = generateReportCaseNumber();
            new RecognizeTextTask().execute();
            disableInputFields();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SubmitReportTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Reports.this);
            progressDialog.setMessage("Processing report...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            submitReport();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            new AlertDialog.Builder(Reports.this)
                    .setTitle("Success!")
                    .setIcon(R.drawable.app_icon)
                    .setMessage("Case Number: " + caseNumber + "\n\n" + "This malicious URL is successfully reported.Please give us 1-10 days to investigate this malicious URL. " +
                            "Afterwards,the result will be sent to you.\n"+ "Please give us reasonable time to investigate this URL you found.")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //clear all fields
                            reset();
                            enableInputFields();
                        }
                    }).show();

        }
    }

    private void submitReport(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parms=new HashMap<String, String>();
                String imgdata=imgToString(imageBitmap);
                String category = categorySpinner.getText().toString();
                String url_source = sourceSpinner.getText().toString();
                parms.put("imageurl",imgdata);
                parms.put("category",category);
                parms.put("url_source",url_source);
                parms.put("caseNum",caseNumber);
                parms.put("email",sharedPreferences.getString("user_email",""));
                parms.put("name",sharedPreferences.getString("user_display_name",""));
                parms.put("lat",tempLat);
                parms.put("lng",tempLng);
                parms.put("textUrl",myURl);
                return parms;

            }
        };
        RequestQueue rq= Volley.newRequestQueue(Reports.this);
        rq.add(stringRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        //remove location callback:
        locationManager.removeUpdates(this);

        tempLat = Double.toString(location.getLatitude());
        tempLng = Double.toString(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(Reports.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                tempLat = Double.toString(location.getLatitude());
                tempLng = Double.toString(location.getLongitude());

            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

    //method for generating report case
    public String generateReportCaseNumber(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(random.nextInt(9)+1);

        for (int i = 0; i<11; i++){
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }

    private void initCategorySpinnerItems(){
        //initialize spinner items
        categorySpinner.setItems("Select Category", "Phishing","Malware","Scam","Spam", "Defacement", "I don't know");
    }

    private void initSourceSpinnerItems(){
        //initialize spinner items
        sourceSpinner.setItems("Select Source (Where the URL is found)","Facebook","Twitter","Instagram","Messenger","WhatsApp","Reddit","Other website");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_imagescan:
                finish();
                startActivity(new Intent(Reports.this,MainActivity.class));
                break;
            case R.id.nav_texturlscan:
                finish();
                startActivity(new Intent(Reports.this,ScanTextUrl.class));
                break;
            case R.id.nav_reports:
                //current activity
                break;
            case R.id.nav_learn:
                startActivity(new Intent(Reports.this,Learn.class));
                break;
            case R.id.nav_signout:
                googleConfig.signOut(Reports.this);
                sharedPreferences.edit().clear().commit();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }


    //text recognition process
    private void detectText() throws IOException {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromFilePath(Reports.this,imageUri);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                // ...
                                processText(firebaseVisionText);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }

    private void processText(FirebaseVisionText text){
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();

        if(blocks.size() ==0){
            Snacky.builder()
                    .setText("No Text")
                    .setIcon(R.drawable.ic_error_outline_black_24dp)
                    .info()
                    .show();

            return;
        }

        recognizedText = new StringBuilder();

        for (FirebaseVisionText.TextBlock block: text.getTextBlocks()){
            recognizedText.append(block.getText()).append("\n");
        }

        longTxt = recognizedText.toString();

        new RecogURLTask().execute();


    }

    @SuppressLint("StaticFieldLeak")
    class RecognizeTextTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Reports.this);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Thread.sleep(5000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            try {
                detectText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void extractUrl (String longTxt){
        UrlDetector urlDetector = new UrlDetector(longTxt, UrlDetectorOptions.Default);
        List<Url> found = urlDetector.detect();

        if(found.size() == 0){
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Unable to process the report")
                    .setMessage("No URLs found on the image. Please try again")
                    .setCancelable(false)
                    .setIcon(R.drawable.app_icon)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            reset();
                            enableInputFields();

                        }
                    }).show();
        }else{
            for (Url url: found){
                myURl = url.getFullUrl();
            }

            new SubmitReportTask().execute();


        }

    }

    @SuppressLint("StaticFieldLeak")
    class RecogURLTask extends  AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //call the extract url method
            extractUrl(longTxt);

        }
    }

    private void disableInputFields(){
        categorySpinner.setEnabled(false);
        sourceSpinner.setEnabled(false);
        submitBtn.setEnabled(false);
    }

    private void enableInputFields(){
        categorySpinner.setEnabled(true);
        sourceSpinner.setEnabled(true);
        submitBtn.setEnabled(true);
    }

    private void reset(){
        urlImage.setImageDrawable(getResources().getDrawable(R.drawable.bkg_add_img));
        initCategorySpinnerItems();
        initSourceSpinnerItems();
        capturedDateTime.setText("---");
        hasImage = false;

    }

}