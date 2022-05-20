package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DataPrivacy extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "LearnActivity";

    GoogleConfig googleConfig = new GoogleConfig();

    SharedPreferences sharedPreferences;

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_privacy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set the current item
        navigationView.getMenu().getItem(3).setChecked(true);

        googleConfig.configureGoogleClient(this);

        //sharedpreference
        sharedPreferences = getSharedPreferences(new Adapter().MyGuestPresf,MODE_PRIVATE);

        webView = findViewById(R.id.webView);

        webView.loadUrl("https://www.privacypolicygenerator.info/live.php?token=KOlGiZjlKKPqOg3SdItxWQEQrDI3Qa1w");

        webView.setWebChromeClient(new WebChromeClient() {
            private ProgressDialog mProgress;

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (mProgress == null) {
                    mProgress = new ProgressDialog(DataPrivacy.this);
                    mProgress.show();
                }
                mProgress.setMessage("Loading " + String.valueOf(progress) + "%");
                if (progress == 100) {
                    mProgress.dismiss();
                    mProgress = null;
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_imagescan:
                finish();
                startActivity(new Intent(DataPrivacy.this,MainActivity.class));
                break;
            case R.id.nav_texturlscan:
                finish();
                startActivity(new Intent(DataPrivacy.this,ScanTextUrl.class));
                break;
            case R.id.nav_reports:
                //check if the current user is a guest user
                try{
                    if(sharedPreferences.getString("guest_user","").isEmpty()){
                        finish();
                        startActivity(new Intent(DataPrivacy.this,Reports.class));
                    }else{
                        finish();
                        startActivity(new Intent(DataPrivacy.this,ReportGuest.class));
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(DataPrivacy.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_learn:
                //current activity
                break;
            case R.id.nav_signout:
                googleConfig.signOut(DataPrivacy.this);
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
}