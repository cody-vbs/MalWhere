package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

import de.mateware.snacky.Snacky;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private DrawerLayout drawer;
    private static final String TAG = "MainActivity";


    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    FloatingActionButton fabCheck,fabAdd;
    ImageView preview;

    boolean hasImage =false;

    Uri imageUri;

    ProgressDialog progressDialog,progressDialog2;


    StringBuilder recognizedText;

    String longTxt;


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


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Configure Google Client
        configureGoogleClient();

        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasImage){

                    Snacky.builder()
                        .setView(view)
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
            case R.id.nav_signout:
                signOut();

        }
        return true;
    }

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {

        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to end your current session?")
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseSignout();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();

    }

    private void firebaseSignout(){
        // Firebase sign out
        firebaseAuth.signOut();
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Signed out of google");
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish();
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

    class RecognizeTextTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Recognizing text");
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

    class RecogURLTask extends  AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog2 = new ProgressDialog(MainActivity.this);
            progressDialog2.setMessage("Detecting URL");
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

    private void recogTextDialog(){
        final EditText recogText  = new EditText(this);
        recogText.setText(recognizedText.toString());

        new AlertDialog.Builder(this)
                .setTitle("Recognized Characters")
                .setView(recogText)
                .setIcon(android.R.drawable.sym_def_app_icon)
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

    private void recogURLDialog(String detectedURL){
        final EditText recogURL  = new EditText(this);
        recogURL.setText(detectedURL);

        new AlertDialog.Builder(this)
                .setTitle("Detected URL")
                .setView(recogURL)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setCancelable(false)
                .setPositiveButton("Analyze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mURLs = recogURL.getText().toString();
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

        StringBuilder myUrl = new StringBuilder();

        for (Url url: found){

            if(url.getFullUrl().length() > 1){
                myUrl.append(url).append("\n");
            }else{
                myUrl.append(url);
            }


        }

        //call the dialog
        recogURLDialog(myUrl.toString());
    }





}