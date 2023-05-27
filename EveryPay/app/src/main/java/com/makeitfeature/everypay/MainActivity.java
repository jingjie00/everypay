package com.makeitfeature.everypay;


import static android.Manifest.permission.HIGH_SAMPLING_RATE_SENSORS;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionValidate();
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
                        , HIGH_SAMPLING_RATE_SENSORS) // ask single or multiple permission once
                .subscribe(permission  -> {
                    if (!permission) {
                        // At least one permission is denied
                        Toast.makeText(getApplicationContext(), getString(R.string.main_loading_1h1), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), getString(R.string.main_loading_1h2), Toast.LENGTH_SHORT).show();
                    }

                });
        //signInValidate();
    }

    private void signInValidate() {
        status = findViewById(R.id.status);
        status.setText(getString(R.string.main_loading_hint) + 2 + getString(R.string.main_loading_2b));

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(@NonNull GetTokenResult getTokenResult) {
                    Toast.makeText(getApplicationContext(),getString(R.string.main_loading_2success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RestoreInitialiseActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    // FaceRecogniser.update(getApplicationContext());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.main_loading_2failure), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, SignInSelectionActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }


}