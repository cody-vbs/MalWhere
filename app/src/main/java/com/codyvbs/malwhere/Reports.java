package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class Reports extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "ReportsActivity";

    GoogleConfig googleConfig = new GoogleConfig();

    ImageView urlImage;
    Button submitBtn;
    MaterialSpinner categorySpinner;
    EditText description;

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

        //initialize spinner items
        categorySpinner.setItems("Select Category", "Phishing","Malware","N/A");

        //set the current item
        navigationView.getMenu().getItem(1).setChecked(true);

        googleConfig.configureGoogleClient(this);


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