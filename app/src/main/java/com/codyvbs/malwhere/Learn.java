package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Learn extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "LearnActivity";

    GoogleConfig googleConfig = new GoogleConfig();

    SharedPreferences sharedPreferences;

    Button feedbackBtn;

    TextView dataPrivacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

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

        feedbackBtn = findViewById(R.id.feedbackBtn);
        dataPrivacy = findViewById(R.id.dataPrivacy);

        dataPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Learn.this,DataPrivacy.class));
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFeedback();
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
                startActivity(new Intent(Learn.this,MainActivity.class));
                break;
            case R.id.nav_texturlscan:
                finish();
                startActivity(new Intent(Learn.this,ScanTextUrl.class));
                break;
            case R.id.nav_reports:
                //check if the current user is a guest user
                try{
                    if(sharedPreferences.getString("guest_user","").isEmpty()){
                        finish();
                        startActivity(new Intent(Learn.this,Reports.class));
                    }else{
                        finish();
                        startActivity(new Intent(Learn.this,ReportGuest.class));
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Learn.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_learn:
                //current activity
                break;
            case R.id.nav_signout:
                googleConfig.signOut(Learn.this);
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

    private void createFeedback(){
        Intent feedbackEmail = new Intent(Intent.ACTION_SEND);

        feedbackEmail.setType("text/email");
        feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"malwhereteam@gmail.com"});
        feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        startActivity(Intent.createChooser(feedbackEmail, "Send Feedback:"));
    }

}