package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.dto.VirusScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ScanUrl extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "ScanUrlActivity";

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    EditText editTextUrl;
    TextView tv1,tv2,tvScanResult;
    Button scanBtn,retryBtn;

    ProgressDialog progressDialog;

    int cleanSiteCount = 0, unratedSiteCount = 0,maliciousCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_url);

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

        //this will make the textview scrollble
        tvScanResult.setMovementMethod(new ScrollingMovementMethod());


        //set the value to edditTextUrl
        editTextUrl.setText(new Adapter().getDetected_URL());

        //set custom font for textviews
        TextView [] textViewArray = {tv1,tv2,tvScanResult};
        EditText [] editTextsArray = {editTextUrl};
        setTextViewFontFamily(textViewArray,editTextsArray);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Configure Google Client
        configureGoogleClient();

        //strict mode policy

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ScanURLTask().execute();
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScanUrl.this,MainActivity.class));
                finish();
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
                        startActivity(new Intent(ScanUrl.this,Login.class));
                        finish();
                    }
                });
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

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ScanUrl.this);
            progressDialog.setMessage("Analyzing URL");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //call the extract url scan report method
            scanUrl(editTextUrl.getText().toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            getUrlReport(editTextUrl.getText().toString());



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

                StringBuilder sbResult = new StringBuilder();
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

                  tvScanResult.setText(sbResult.toString());


                if(maliciousCount > 0){

                    MaliciousDialog maliciousDialog = new MaliciousDialog();
                    maliciousDialog.showDialog(ScanUrl.this,"Malicious URL",
                            Integer.toString(maliciousCount) + "/" + Integer.toString(scans.size()) + " vendors flagged this URL as malicious");

                }else{

                    BenignDialog benignDialog = new BenignDialog();
                    benignDialog.showDialog(ScanUrl.this,"Benign URL");

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
        public void showDialog(Activity activity,String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.safe_url_dialog);

            TextView textViewMessage = (TextView) dialog.findViewById(R.id.text_dialog);
            textViewMessage.setText(msg);

            Button dialogOkButton  = (Button) dialog.findViewById(R.id.btn_dialog_ok);
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
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

}