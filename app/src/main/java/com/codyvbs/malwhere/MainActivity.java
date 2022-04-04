package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.mateware.snacky.Snacky;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";

    FloatingActionButton fabCheck,fabAdd;
    ImageView preview;

    boolean hasImage =false;

    Uri imageUri;

    ProgressDialog progressDialog,progressDialog2,progressDialog3;


    StringBuilder recognizedText;

    String longTxt,shortURL;

    GoogleConfig googleConfig = new GoogleConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabCheck = findViewById(R.id.fab_check);
        fabAdd = findViewById(R.id.fab_add);
        preview = findViewById(R.id.img_preview);

        //set the current item
        navigationView.getMenu().getItem(0).setChecked(true);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Configure Google Client
        googleConfig.configureGoogleClient(MainActivity.this);

        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasImage){

                    Snacky.builder()
                        .setView(view)
                            .setTextColor(getResources().getColor(R.color.white))
                        .setText("Add an image that contains a URL")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .warning()
                        .show();
                }else{
                    new RecognizeTextTask().execute();
                }


            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionOpenCam();

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_imagescan:
                //current activity
                break;
            case R.id.nav_texturlscan:
                finish();
                startActivity(new Intent(MainActivity.this,ScanTextUrl.class));
                break;
            case R.id.nav_reports:
                finish();
                startActivity(new Intent(MainActivity.this,Reports.class));
                break;
            case R.id.nav_learn:
                finish();
                startActivity(new Intent(MainActivity.this,Learn.class));
                break;
            case R.id.nav_signout:
                googleConfig.signOut(MainActivity.this);
                break;

        }
        return true;
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
                imageUri= result.getUri();
                preview.setImageURI(imageUri);

                //set boolean value to true
                hasImage = true;
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
    //*********************************************************************************************************************

    //text recognition process
    private void detectText() throws IOException {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromFilePath(MainActivity.this,imageUri);
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

        recogTextDialog();

    }

    @SuppressLint("StaticFieldLeak")
    class RecognizeTextTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Recognizing text");
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

    @SuppressLint("StaticFieldLeak")
    class RecogURLTask extends  AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog2 = new ProgressDialog(MainActivity.this);
            progressDialog2.setMessage("Detecting URL");
            progressDialog2.setCancelable(false);
            progressDialog2.show();
        }

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
            if(progressDialog2 != null && progressDialog2.isShowing()){
                progressDialog2.dismiss();
            }

            //call the extract url method
            extractUrl(longTxt);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class UnshortenURLTask extends  AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog3 = new ProgressDialog(MainActivity.this);
            progressDialog3.setMessage("Processing...");
            progressDialog3.setCancelable(false);
            progressDialog3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            extractLongUrl(shortURL);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(progressDialog3 != null && progressDialog3.isShowing()){
                progressDialog3.dismiss();
            }


        }
    }

    private void recogTextDialog(){
        final EditText recogText  = new EditText(this);
        recogText.setText(recognizedText.toString());

        new AlertDialog.Builder(this)
                .setTitle("Recognized Characters")
                .setView(recogText)
                .setIcon(R.drawable.app_icon)
                .setCancelable(false)
                .setPositiveButton("Extract URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        longTxt = recogText.getText().toString();

                        //execute URL Detection
                        new RecogURLTask().execute();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();

    }

    private void shortenURLWarnDialog(String URL){

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString str = new SpannableString(URL);
        str.setSpan(new ForegroundColorSpan(Color.BLUE),0,str.length(),0);
        spannableStringBuilder.append("It looks like " );
        spannableStringBuilder.append(str);
        spannableStringBuilder.append(" is a shortened URL. We will automatically extract the long URL click ");
        spannableStringBuilder.append("\" Unshorten URL \"");


        final EditText recogURL  = new EditText(this);
        recogURL.setText(spannableStringBuilder,
                EditText.BufferType.SPANNABLE);

        new AlertDialog.Builder(this)
                .setTitle("Shortened URL")
                .setView(recogURL)
                .setIcon(R.drawable.app_icon)
                .setCancelable(false)
                .setPositiveButton("Unshorten URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            shortURL = URL;
                            new UnshortenURLTask().execute();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void extractUrl (String longTxt){
        UrlDetector urlDetector = new UrlDetector(longTxt, UrlDetectorOptions.Default);
        List<Url> found = urlDetector.detect();

        String myURl = "";
        if(found.size() == 0){
           new AlertDialog.Builder(this)
                   .setTitle("MalWhere")
                   .setMessage("No URLs found. Please try again")
                   .setCancelable(false)
                   .setIcon(R.drawable.app_icon)
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   }).show();
        }else{
            for (Url url: found){
                myURl = url.getFullUrl();
            }


            if(isURLShorten(new Adapter().urlShortenerDomain,myURl) == true){


                //display short url detected warn dialog
                shortenURLWarnDialog(myURl);

            }else{
                //set the url value to adapter
                new Adapter().setDetected_URL(myURl);
                //open new activity
                startActivity(new Intent(MainActivity.this,ScanUrl.class));
            }



        }

    }

    //check if url is shorten url
    private boolean isURLShorten(String [] domainArr,String url){

        for(String domain: domainArr){
            if(url.startsWith("https://" + domain) || url.startsWith("http://" + domain)){
                return true;
            }
        }

        return false;
    }

    private void extractLongUrl(String urlShorten){

        RequestQueue queue = Volley.newRequestQueue(this);
        String myReq = "https://unshort.herokuapp.com/api/?url=" + urlShorten;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, myReq, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            //assign the long url to setter and getter
                            new Adapter().setDetected_URL(response.getString("longUrl"));
                            //open new activity scan class
                            startActivity(new Intent(MainActivity.this,ScanUrl.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
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



}