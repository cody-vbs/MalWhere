package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.materialspinner.MaterialSpinner;
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

public class Reports extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private DrawerLayout drawer;
    private static final String TAG = "ReportsActivity";

    private static final String URL = "http://192.168.1.4/MalWhere/submit_report.php";

    GoogleConfig googleConfig = new GoogleConfig();

    ImageView urlImage;
    Button submitBtn;
    MaterialSpinner categorySpinner;
    EditText description;
    FloatingActionButton fabAddImage;
    TextView capturedDateTime;

    Bitmap imageBitmap;
    String timeStamp,caseNumber;

    ProgressDialog progressDialog;

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
        description = findViewById(R.id.description);
        fabAddImage = findViewById(R.id.fab_add);
        capturedDateTime = findViewById(R.id.capturedDate);

        //set the current item
        navigationView.getMenu().getItem(1).setChecked(true);

        googleConfig.configureGoogleClient(this);

        initCategorySpinnerItems();

        TextView tvsArr [] = {capturedDateTime};
        EditText editTextArr []= {description};

        MaterialSpinner materialSpinnerArr[] = {categorySpinner};

        //set custom font UI
        new CustomUI().setTextViewFontFamily(this,tvsArr,editTextArr);
        new CustomUI().setSpinnerFont(this,materialSpinnerArr);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

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

    private void validate(){
        if(categorySpinner.getText().toString().equalsIgnoreCase("Select Category")){
            Snacky.builder()
                    .setView(getWindow().getDecorView().getRootView())
                    .setTextColor(getResources().getColor(R.color.white))
                    .setText("Please select a category!")
                    .warning()
                    .show();
        }else{
            caseNumber = generateReportCaseNumber();
            new SubmitReportTask().execute();
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
                    .setMessage("Case Number: " + caseNumber + "\n\n" + "Thank you for reporting a suspicious URL you found online.\n"+
                    "Please give us reasonable time to investigate this URL you found.")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //clear all fields
                            urlImage.setImageDrawable(getResources().getDrawable(R.drawable.bkg_add_img));
                            initCategorySpinnerItems();
                            description.setText("Description (Optional)");
                            capturedDateTime.setText("---");
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
                String desc = description.getText().toString();
                parms.put("imageurl",imgdata);
                parms.put("category",category);
                parms.put("description",desc);
                parms.put("caseNum",caseNumber);
                return parms;

            }
        };
        RequestQueue rq= Volley.newRequestQueue(Reports.this);
        rq.add(stringRequest);
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
        categorySpinner.setItems("Select Category", "Phishing","Malware","N/A");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_imagescan:
                finish();
                startActivity(new Intent(Reports.this,MainActivity.class));
                break;
            case R.id.nav_reports:
                //current activity
                break;
            case R.id.nav_learn:
                startActivity(new Intent(Reports.this,Learn.class));
                break;
            case R.id.nav_signout:
                googleConfig.signOut(Reports.this);
                break;

        }
        return true;
    }
}