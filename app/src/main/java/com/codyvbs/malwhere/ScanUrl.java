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
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

public class ScanUrl extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final String TAG = "ScanUrlActivity";

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    EditText editTextUrl;
    TextView tv1,tv2;

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

        //set the value to edditTextUrl
        editTextUrl.setText(new Adapter().getDetected_URL());

        //set custom font for textviews
        TextView [] textViewArray = {tv1,tv2};
        EditText [] editTextsArray = {editTextUrl};
        setTextViewFontFamily(textViewArray,editTextsArray);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Configure Google Client
        configureGoogleClient();

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
    private void scanUrl(String url){
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(getResources().getString(R.string.virus_total_key));
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

        } catch (APIKeyNotFoundException e) {
            e.printStackTrace();
        }
    }

}