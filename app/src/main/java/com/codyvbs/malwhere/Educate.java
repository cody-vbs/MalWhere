package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Educate extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final String TAG = "EducateActivity";

    GoogleConfig googleConfig = new GoogleConfig();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set the current item
        if(new Adapter().getGetClicked().equalsIgnoreCase("ScanUrl")){
            navigationView.getMenu().getItem(0).setChecked(true);
        }else{
            navigationView.getMenu().getItem(1).setChecked(true);
        }

        googleConfig.configureGoogleClient(this);

        //sharedpreference
        sharedPreferences = getSharedPreferences(new Adapter().MyGuestPresf,MODE_PRIVATE);

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
                startActivity(new Intent(Educate.this,MainActivity.class));
                break;
            case R.id.nav_texturlscan:
                finish();
                startActivity(new Intent(Educate.this,ScanTextUrl.class));
                break;
            case R.id.nav_reports:
                //check if the current user is a guest user
                try{
                    if(sharedPreferences.getString("guest_user","").isEmpty()){
                        finish();
                        startActivity(new Intent(Educate.this,Reports.class));
                    }else{
                        finish();
                        startActivity(new Intent(Educate.this,ReportGuest.class));
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Educate.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_learn:
                //current activity
                break;
            case R.id.nav_signout:
                googleConfig.signOut(Educate.this);
                sharedPreferences.edit().clear().commit();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(new Adapter().getGetClicked().equalsIgnoreCase("ScanTextUrl")){
            finish();
            startActivity(new Intent(Educate.this,ScanTextUrl.class));
        }else{
            finish();
            startActivity(new Intent(Educate.this,ScanUrl.class));
        }
    }
}