package com.makeitfeature.everypay;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionValidate();
        setContentView(R.layout.activity_main);
    }

    public void Toaster(String msg){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    private void permissionValidate(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.HIGH_SAMPLING_RATE_SENSORS) // ask single or multiple permission once
                .subscribe(permission  -> {
                    /*if (!permission) {
                        // At least one permission is denied
                        Toast.makeText(getApplicationContext(), getString(R.string.main_loading_1h1), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), getString(R.string.main_loading_1h2), Toast.LENGTH_SHORT).show();
                    }*/
                        login();


                });

    }

    private void login(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.logo)
                .setAvailableProviders(providers)
                .build();
        //signInLauncher.launch(signInIntent);

    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );



    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }
    }

    public void loginwithemail(View view){
        Intent i = new Intent(MainActivity.this, PaymentActivity.class);
        i.putExtra("user","dependent");
        i.putExtra("id","1273432172");
        startActivity(i);
        finish();
    }

    public void loginwithgoogle(View view){
        Intent i = new Intent(MainActivity.this, PaymentActivity.class);
        i.putExtra("user","guardian");
        i.putExtra("id","222233344");
        startActivity(i);
        finish();
    }


}