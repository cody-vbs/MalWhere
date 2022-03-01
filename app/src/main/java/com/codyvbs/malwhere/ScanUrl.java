package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.kanishka.virustotal.exception.InvalidArgumentsException;
import com.kanishka.virustotal.exception.QuotaExceededException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ScanUrl extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "ScanUrlActivity";

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    EditText editTextUrl;
    TextView tv1,tv2,tvScanResult;
    Button scanBtn;

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
                scanUrl("https://evilzone.org/");
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
    public void scanUrl(String myUrl) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(getResources().getString(R.string.virus_total_key));
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            String urls[] = {myUrl};
            ScanInfo[] scanInfoArr = virusTotalRef.scanUrls(urls);

            for (ScanInfo scanInformation : scanInfoArr) {
                System.out.println("___SCAN INFORMATION___");
//                System.out.println("MD5 :\t" + scanInformation.getMd5());
//                System.out.println("Perma Link :\t" + scanInformation.getPermalink());
//                System.out.println("Resource :\t" + scanInformation.getResource());
//                System.out.println("Scan Date :\t" + scanInformation.getScan_date());
//                System.out.println("Scan Id :\t" + scanInformation.getScan_id());
//                System.out.println("SHA1 :\t" + scanInformation.getSha1());
//                System.out.println("SHA256 :\t" + scanInformation.getSha256());
//                System.out.println("Verbose Msg :\t" + scanInformation.getVerbose_msg());
//                System.out.println("Response Code :\t" + scanInformation.getResponse_code());
//                System.out.println("done.");
            }

            getUrlReport(myUrl);

        } catch (APIKeyNotFoundException ex) {
            System.err.println("API Key not found! " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported Encoding Format!" + ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            System.err.println("Invalid API Key " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Something Bad Happened! " + ex.getMessage());
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
//                System.out.println("MD5 :\t" + report.getMd5());
//                System.out.println("Perma link :\t" + report.getPermalink());
//                System.out.println("Resourve :\t" + report.getResource());
//                System.out.println("Scan Date :\t" + report.getScan_date());
//                System.out.println("Scan Id :\t" + report.getScan_id());
//                System.out.println("SHA1 :\t" + report.getSha1());
//                System.out.println("SHA256 :\t" + report.getSha256());
//                System.out.println("Verbose Msg :\t" + report.getVerbose_msg());
//                System.out.println("Response Code :\t" + report.getResponse_code());
//                System.out.println("Positives :\t" + report.getPositives());
//                System.out.println("Total :\t" + report.getTotal());

                StringBuilder sbResult = new StringBuilder();
                Map<String, VirusScanInfo> scans = report.getScans();

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                for (String key : scans.keySet()) {
                    VirusScanInfo virusInfo = scans.get(key);
//                    System.out.println("Scanner : " + key);
//                    System.out.println("\t\t Resut : " + virusInfo.getResult());
//                    System.out.println("\t\t Update : " + virusInfo.getUpdate());
//                    System.out.println("\t\t Version :" + virusInfo.getVersion());

//                    if(virusInfo.getResult().equalsIgnoreCase("clean site")){
//                        SpannableString cleanStr = new SpannableString(virusInfo.getResult());
//                        cleanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, cleanStr.length(), 0);
//                        spannableStringBuilder.append(key+ " .................... " + cleanStr);
//                    }else{
//                        SpannableString unratedStr = new SpannableString(virusInfo.getResult());
//                        unratedStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, unratedStr.length(), 0);
//                        spannableStringBuilder.append(key+ " .................... " + unratedStr);
//                    }


                    sbResult.append(key + ".......... " + virusInfo.getResult()).append("\n");

                }

//                tvScanResult.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                  tvScanResult.setText(sbResult.toString());

            }



        } catch (APIKeyNotFoundException ex) {
            System.err.println("API Key not found! " + ex.getMessage());
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported Encoding Format!" + ex.getMessage());
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();

        } catch (UnauthorizedAccessException ex) {
            System.err.println("Invalid API Key " + ex.getMessage());
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            System.err.println("Something Bad Happened! " + ex.getMessage());
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

}