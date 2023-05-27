package com.makeitfeature.everypay;


import static android.Manifest.permission.HIGH_SAMPLING_RATE_SENSORS;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                    //signInValidate();
                });


    }

    public void pressme(View view){
        permissionValidate();

        Intent secondScreen = new Intent(this, Home.class);
        EditText editTextusername = findViewById(R.id.editTextTextuserN);
        EditText editTextaddress = findViewById(R.id.editTextTextPassword);

        String userName = editTextusername.getText().toString();
        String userPass = editTextaddress.getText().toString();

        if(userName.equals("admin") && userPass.equals("admin")){

            startActivity(secondScreen);
        }
        else {
            Toaster("Incorrect User or Password");
        }


    }

    public void forgotCred(View view){
        Intent forgotScreen = new Intent(this, forgot.class);
        startActivity(forgotScreen);
    }


}