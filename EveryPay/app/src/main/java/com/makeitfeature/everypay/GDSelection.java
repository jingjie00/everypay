package com.makeitfeature.everypay;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class GDSelection extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdselection);
    }

    public void Toaster(String msg){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void doDependent(View view){
        Intent i = new Intent(GDSelection.this, Dependent.class);
        startActivity(i);
        finish();
    }

    public void doGuardian(View view){
        Intent i = new Intent(GDSelection.this, Guardian.class);
        startActivity(i);
        finish();
    }


}