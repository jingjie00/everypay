package com.makeitfeature.everypay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.makeitfeature.everypay.camera.CameraFragment;
import com.makeitfeature.everypay.utils.Preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class CameraRecognitionActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;


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




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_camera_recognition);
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .commit();

        }

    }


    private Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            // Insert custom code here
            try {
                    ArrayList<Bitmap> bm = CameraFragment.current_input_faces;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                long interval = Preferences.getLongPreferenceValue(getApplicationContext(), Preferences.SETTING_OUTPUTINTERVAL);
                handler.postDelayed(runnable, interval);
            }

        }
    };




}

