package com.codyvbs.malwhere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kanishka.virustotalv2.VirusTotalConfig;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

import java.util.Random;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1001;

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    //components
    Button google_sign_in,login_guest;

    SharedPreferences.Editor editor,guestEditor;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        google_sign_in = findViewById(R.id.btn_google);
        login_guest = findViewById(R.id.btn_guest);

        editor = getSharedPreferences(new Adapter().MainUserPresf,MODE_PRIVATE).edit();
        guestEditor = getSharedPreferences(new Adapter().MyGuestPresf, MODE_PRIVATE).edit();

        login_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guestEditor.putString("guest_user",generateGuestUser());
                guestEditor.apply();

                finish();
                startActivity(new Intent(Login.this, MainActivity.class));

            }
        });

        google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToGoogle();
            }
        });

        // Configure Google Client
        configureGoogleClient();

        //check active connection
        new CheckConnectionClass().checkConnection(this,getLifecycle());

        VirusTotalConfig.getConfigInstance().getVirusTotalAPIKey();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
           //if the access token is active redirect user to the main page
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            guestEditor.clear().commit();
        }
    }
    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showToastMessage("Signed in successfully");
                dialog();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Sign in failed", e);
                showToastMessage("Sign in Failed " + e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());

                            //store email and name in sharedpreference
                            editor.putString("user_email",user.getEmail());
                            editor.putString("user_display_name",user.getDisplayName());
                            editor.apply();

                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }

                            showToastMessage("Welcome back " + user.getDisplayName());
                            launchMainActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showToastMessage("Authentication failed:" + task.getException());
                        }
                    }
                });
    }
    private void showToastMessage(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
    }

    private void launchMainActivity(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    //method for generating random guest username
    private String generateGuestUser(){
        char [] numArr = "0123456789".toCharArray();
        Random rand = new Random();
        StringBuilder sb  = new StringBuilder();

        for (int x = 0; x<5; x++){
            sb.append(numArr[rand.nextInt(numArr.length)]);
        }

        return "guest" + sb.toString();

    }

    private void dialog(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);
        dialog.show();
    }



}