package com.makeitfeature.everypay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.makeitfeature.everypay.utils.Preferences;

import java.util.Locale;

public class RestoreInitialiseActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;

    SensorManager sensorManager;
    private TextView status;
    private FirebaseAuth mAuth;
    private Boolean restoreBackup;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        TextView moreInfo = findViewById(R.id.more_info);
        moreInfo.setMovementMethod(LinkMovementMethod.getInstance());

        status = findViewById(R.id.status);
        status.setText(getString(R.string.main_loading_hint) + 4 + getString(R.string.main_loading_4));

        restoreBackup = getIntent().getBooleanExtra("restore_backup",false);




    }



    private void restore(){
        status.setText(getString(R.string.main_loading_hint)  + 4 + getString(R.string.main_loading_5));
        mAuth = FirebaseAuth.getInstance();
    }



    private void proceed(){
        Intent intent = new Intent(RestoreInitialiseActivity.this,Home.class);
        startActivity(intent);
        finishAffinity();
    }



}



