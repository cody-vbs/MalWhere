package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.dto.VirusScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class ScanTextUrl extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "ScanTextUrlActivity";

    private static final String URL = new Adapter().SCAN_LOGS_ONLINE;


    EditText editTextUrl;
    TextView tv1,tv2,tvScanResult;
    Button scanBtn,retryBtn;

    ProgressDialog progressDialog,progressDialog2;

    int cleanSiteCount = 0, unratedSiteCount = 0,maliciousCount = 0;

    String longURL;

    StringBuilder sbResult;

    GoogleConfig googleConfig = new GoogleConfig();

    SharedPreferences sharedPreferences,guestSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_text_url);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editTextUrl = findViewById(R.id.edittext_url);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        scanBtn  = findViewById(R.id.scan_btn);
        retryBtn = findViewById(R.id.btn_retry);
        tvScanResult = findViewById(R.id.tvscanResult);

        //set the current item
        navigationView.getMenu().getItem(1).setChecked(true);

        //sharedpreference
        sharedPreferences = getSharedPreferences(new Adapter().MainUserPresf,MODE_PRIVATE);
        guestSharedPreference = getSharedPreferences(new Adapter().MyGuestPresf,MODE_PRIVATE);

        //this will make the textview scrollble
        tvScanResult.setMovementMethod(new ScrollingMovementMethod());

        //set custom font for textviews
        TextView [] textViewArray = {tvScanResult};
        EditText [] editTextsArray = {editTextUrl};
        setTextViewFontFamily(textViewArray,editTextsArray);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Configure Google Client
        googleConfig.configureGoogleClient(this);

        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

        //strict mode policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disable the textfield
                editTextUrl.setEnabled(false);
                scanBtn.setEnabled(false);

                if(editTextUrl.getText().toString().isEmpty()){
                    Snacky.builder()
                            .setView(view)
                            .setTextColor(getResources().getColor(R.color.white))
                            .setText("No URL Found")
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .warning()
                            .show();
                }else{
                    String urlTxt = editTextUrl.getText().toString();
                    if(isURLShorten(new Adapter().urlShortenerDomain,urlTxt) == true){
                        extractLongUrl(editTextUrl.getText().toString());
                        new UnshortenURLTask().execute();
                    }else{
                        new ScanTextUrl.ScanURLTask().execute();

                    }

                }

            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable the textfield
                editTextUrl.setEnabled(true);
                scanBtn.setEnabled(true);

                reset();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_imagescan:
                startActivity(new Intent(ScanTextUrl.this,MainActivity.class));
                break;
            case R.id.nav_texturlscan:
                // current activity
                break;
            case R.id.nav_reports:
                //check if the current user is a guest user
                try{
                    if(sharedPreferences.getString("guest_user","").isEmpty()){
                        finish();
                        startActivity(new Intent(ScanTextUrl.this,Reports.class));
                    }else{
                        finish();
                        startActivity(new Intent(ScanTextUrl.this,ReportGuest.class));
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ScanTextUrl.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_learn:
                finish();
                startActivity(new Intent(ScanTextUrl.this,Learn.class));
                break;
            case R.id.nav_signout:
                googleConfig.signOut(this);
                sharedPreferences.edit().clear().commit();
                break;

        }
        return true;
    }
    private void setTextViewFontFamily(TextView [] tvs,EditText [] editxts){
        Typeface customFont = ResourcesCompat.getFont(this,R.font.robotobold);

        for(int x = 0; x<tvs.length ;x++){
            tvs[x].setTypeface(customFont);
        }

        for (int y =0 ; y<editxts.length;y++){
            editxts[y].setTypeface(customFont);
        }

    }

    //method for scanning URL using Virus Total API

    class ScanURLTask extends AsyncTask<Void,Void,Void> {
        String myURl = editTextUrl.getText().toString();

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ScanTextUrl.this);
            progressDialog.setMessage("Analyzing URL");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //call the extract url scan report method

            if(
                isURLShorten(new Adapter().urlShortenerDomain,myURl) == true){
                scanUrl(new Adapter().getFinalLongURL());
            }else{
                scanUrl(myURl);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if(isURLShorten(new Adapter().urlShortenerDomain,myURl) == true){
                getUrlReport(new Adapter().getFinalLongURL());
            }else{
                getUrlReport(myURl);
            }



        }
    }



    public void scanUrl(String myUrl) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(getResources().getString(R.string.virus_total_key));
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            String urls[] = {myUrl};
            ScanInfo[] scanInfoArr = virusTotalRef.scanUrls(urls);

            for (ScanInfo scanInformation : scanInfoArr) {
                Log.d(TAG,"___SCAN INFORMATION___");

            }


        } catch (APIKeyNotFoundException ex) {
            Toast.makeText(this,"API Key not found! " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException ex) {
            Toast.makeText(this,"Unsupported Encoding Format!" + ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (UnauthorizedAccessException ex) {
            Toast.makeText(this,"Invalid API Key " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this,"Something Bad Happened! " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void getUrlReport(String myUrl){

        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(getResources().getString(R.string.virus_total_key));
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            String urls[] = {myUrl};
            FileScanReport[] reports = virusTotalRef.getUrlScanReport(urls, false);

            String temp = "";

            for (FileScanReport report : reports) {
                if(report.getResponseCode()==0){
                    continue;
                }

                sbResult = new StringBuilder();

                Map<String, VirusScanInfo> scans = report.getScans();

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                for (String key : scans.keySet()) {
                    VirusScanInfo virusInfo = scans.get(key);

                    if(virusInfo.getResult().contains("clean")){
                        cleanSiteCount++;
                    }else if(virusInfo.getResult().contains("unrated")){
                        unratedSiteCount++;
                    }else{
                        maliciousCount++;
                    }

                    sbResult.append(key + ".......... " + virusInfo.getResult()).append("\n");

                }


                if(maliciousCount > 0){

                    MaliciousDialog maliciousDialog = new MaliciousDialog();
                    maliciousDialog.showDialog(ScanTextUrl.this,"Malicious URL",
                            Integer.toString(maliciousCount) + "/" + Integer.toString(scans.size()) + " vendors flagged this URL as malicious");

                    //save log to server
                    String user = "";
                    String scanResult = "Malicious";
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                    if(sharedPreferences.getString("guest_user","").isEmpty()){
                        user = sharedPreferences.getString("user_display_name","");
                    }else{
                        user = sharedPreferences.getString("guest_user","");
                    }

                    tvScanResult.setText(sbResult.toString());


                    saveLog(user,scanResult,timeStamp);


                }else{
                    //if url is not blacklisted call predict model
                    new PredictUrlModelTask().execute();
                }

            }

        } catch (APIKeyNotFoundException ex) {
            Toast.makeText(this,"API Key not found! " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException ex) {
            Toast.makeText(this,"Unsupported Encoding Format!" + ex.getMessage(),Toast.LENGTH_SHORT).show();

        } catch (UnauthorizedAccessException ex) {
            Toast.makeText(this,"Invalid API Key " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this,"Something Bad Happened! " + ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //benign and malicious dialog result
    public class BenignDialog{
        public void showDialog(Activity activity, String msg, String msg2){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.safe_url_dialog);

            TextView textViewMessage = (TextView) dialog.findViewById(R.id.text_dialog1);
            textViewMessage.setText(msg);

            TextView textViewMessage2 = (TextView)dialog.findViewById(R.id.text_dialog2);
            textViewMessage2.setText(msg2);

            Button dialogOkButton  = (Button) dialog.findViewById(R.id.btn_dialog_ok);
            Button dialogLearn = (Button) dialog.findViewById(R.id.btn_dialog_learn);

            dialogLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ScanTextUrl.this,Educate.class));
                    new Adapter().setGetClicked("ScanTextUrl");
                }
            });
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    public class MaliciousDialog{
        public void showDialog(Activity activity,String msg,String count){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.malicious_url_dialog);

            TextView textViewMessage = (TextView) dialog.findViewById(R.id.text_dialog);
            textViewMessage.setText(msg);

            TextView textViewCount = (TextView) dialog.findViewById(R.id.text_dialog2);
            textViewCount.setText(count);

            Button dialogOkButton  = (Button) dialog.findViewById(R.id.btn_dialog_ok);
            Button dialogLearn = (Button) dialog.findViewById(R.id.btn_dialog_learn);

            dialogLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ScanTextUrl.this,Educate.class));
                    new Adapter().setGetClicked("ScanTextUrl");
                }
            });
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    public void checkConnection(){
        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder builder = new NoInternetDialogPendulum.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesPendulum properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });

        properties.setCancelable(false);
        properties.setNoInternetConnectionTitle("No Internet");
        properties.setNoInternetConnectionMessage("Check your Internet connection and try again");
        properties.setShowInternetOnButtons(true);
        properties.setPleaseTurnOnText("Please turn on");
        properties.setWifiOnButtonText("Wifi");
        properties.setMobileDataOnButtonText("Mobile data");

        properties.setOnAirplaneModeTitle("No Internet");
        properties.setOnAirplaneModeMessage("You have turned on the airplane mode.");
        properties.setPleaseTurnOffText("Please turn off");
        properties.setAirplaneModeOffButtonText("Airplane mode");
        properties.setShowAirplaneModeOffButtons(true);

        builder.build();
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

    private void reset(){
        editTextUrl.setText("");
        tvScanResult.setText("...");
    }

    private void saveLog(String user, String scanResult, String timestamp){
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
                parms.put("user",user);
                parms.put("scanResult",scanResult);
                parms.put("timestamp",timestamp);

                return parms;

            }
        };
        RequestQueue rq= Volley.newRequestQueue(ScanTextUrl.this);
        rq.add(stringRequest);
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
                        //assign the long url to setter and getter
                        try {
                            new Adapter().setFinalLongURL(response.getString("longUrl"));
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


    @SuppressLint("StaticFieldLeak")
    class PredictUrlModelTask extends  AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if(isURLShorten(new Adapter().urlShortenerDomain,editTextUrl.getText().toString()) == true){
                predictiveModedl(new Adapter().getFinalLongURL());
            }else{
                predictiveModedl(editTextUrl.getText().toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(progressDialog2 != null && progressDialog2.isShowing()){
                progressDialog2.dismiss();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    class UnshortenURLTask extends  AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog2 = new ProgressDialog(ScanTextUrl.this);
            progressDialog2.setMessage("Processing...");
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

            extractLongUrl(editTextUrl.getText().toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(progressDialog2 != null && progressDialog2.isShowing()){
                progressDialog2.dismiss();
            }
            shortURLWarnDialog(new Adapter().getFinalLongURL());
            //new ScanURLTask().execute();

        }
    }

    private void shortURLWarnDialog(String longURL) {
        String msg = "The URL you entered is a short URL. We extracted the long URL to get the best results. Click continue to start scanning.";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(msg);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK),0,spannableString.length(),0);
        spannableStringBuilder.append(spannableString);


        final EditText recogText = new EditText(this);
        recogText.setText(spannableStringBuilder, EditText.BufferType.SPANNABLE);
        recogText.setEnabled(false);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Short URL Detected")
                .setView(recogText)
                .setIcon(R.drawable.app_icon)
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            new ScanURLTask().execute();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    reset();
            }
        }).show();

    }

    //trained model for lexical feature approach

    private void predictiveModedl(String mUrl){
        RequestQueue queue = Volley.newRequestQueue(this);
        String myReq = getString(R.string.predictive_model) + mUrl;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, myReq, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        //assign the long url to setter and getter
                        try {

                            //Toast.makeText(ScanTextUrl.this,response.getString("prediction"),Toast.LENGTH_SHORT).show();
                           if(response.getString("prediction").equalsIgnoreCase("[0]")){
                               BenignDialog benignDialog = new BenignDialog();
                               benignDialog.showDialog(ScanTextUrl.this,"Benign URL", "No vendors flagged this URL as malicious");

                               //save log to server
                               String user = "";
                               String scanResult = "Benign";
                               String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());


                               if(sharedPreferences.getString("guest_user","").isEmpty()){
                                   user = sharedPreferences.getString("user_display_name","");
                               }else{
                                   user = sharedPreferences.getString("guest_user","");
                               }

                               sbResult.append("\n").append("MalWhere Predictive Model: BENIGN");

                               tvScanResult.setText(sbResult.toString());

                               saveLog(user,scanResult,timeStamp);
                           }else{

                               MaliciousDialog maliciousDialog = new MaliciousDialog();
                               maliciousDialog.showDialog(ScanTextUrl.this,"Malicious URL",
                                       "This URL is flagged as Malicious");

                               //save log to server
                               String user = "";
                               String scanResult = "Malicious";
                               String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                               if(sharedPreferences.getString("guest_user","").isEmpty()){
                                   user = sharedPreferences.getString("user_display_name","");
                               }else{
                                   user = sharedPreferences.getString("guest_user","");
                               }

                               sbResult.append("\n").append("MalWhere Predictive Model: MALICIOUS");

                               tvScanResult.setText(sbResult.toString());


                               saveLog(user,scanResult,timeStamp);

                           }

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


    }