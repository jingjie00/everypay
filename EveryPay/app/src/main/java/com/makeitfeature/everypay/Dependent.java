package com.makeitfeature.everypay;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Dependent extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent);
        String data = "iVBORw0KGgoAAAANSUhEUgAAAFwAAABcCAYAAADj79JYAAAAAklEQVR4AewaftIAAAJXSURBVO3BQY4bQQwEwSxi/v/ltI6ED71oSEvLACPiC2tMsUYVa1SxRj38JQmTVLokdCpdEm6odEmYpNIVa1SxRhVr1MMPVD4pCScq71C5ofJJSTgp1qhijSrWqIdLSbihciMJJypdEj4pCTdUbhRrVLFGFWvUw39OpUtCp/JNijWqWKOKNerhy6mcJKFT+WbFGlWsUcUa9XBJ5TepdEm4kYRO5YbKbyrWqGKNKtaohx8kYVISOpUuCZ1Kl4QbSZhUrFHFGlWsUfGF/1gSTlS+SbFGFWtUsUY9/CUJ71A5SUKn0iXhk5LQqZwk4YZKl4ROpSvWqGKNKtao+EKThBOVLgk3VLok/EsqJ0l4h0pXrFHFGlWsUQ8/UHmHSpeETuUkCZ1Kl4RO5ZNUPqlYo4o1qlijHv6i0iXhRKVLwonKDZVPSsINlS4JnUqXhE6lK9aoYo0q1qj4wgcl4YbKjSR0Kl0SOpWTJHQqN5LQqZwUa1SxRhVrVHzhiyShU+mScKJykoRO5SQJncpJEjqVrlijijWqWKPiC00SJqncSMINlZMkdCq/qVijijWqWKMefqDySUn4JJVPSkKn0iXhROWkWKOKNapYox4uJeGGyjuScCMJn5SETuUkCZ1KV6xRxRpVrFEPXyYJnUqXhC4JncpJEt6RhBOVk2KNKtaoYo16+HJJOFHpktCp3FD5TcUaVaxRxRr1cEllkkqXhBOVLgmdSpeEG0m4odIVa1SxRhVr1MMPkvAvJeE3qXRJ6FQ6lS4JncpJsUYVa1SxRsUX1phijSrWqGKN+gPcmwm2zII9sAAAAABJRU5ErkJggg==";
        // Display the first 500 characters of the response string.
        byte[] bytes= Base64.decode(data, Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView


        ImageView qrView= findViewById(R.id.dependentQr);
        qrView.setVisibility(View.VISIBLE);
        qrView.setImageBitmap(bitmap);

        ImageView dependentQr = findViewById(R.id.dependentQr);
        dependentQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView gName = findViewById(R.id.recNum);
                gName.setVisibility(View.VISIBLE);
            }
        });

    }

    public void Toaster(String msg){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void goPayment(View view){
        Intent i = new Intent(Dependent.this, PaymentActivity.class);
        startActivity(i);
        finish();
    }


}