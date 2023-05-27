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

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.generateQR)+"12345";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                try {
                    JSONObject responseJson=new JSONObject(response);
                    String data = responseJson.getString("data");
                    // Display the first 500 characters of the response string.
                    byte[] bytes= Base64.decode(data.replace("data:image/png;base64,",""), Base64.DEFAULT);
                    // Initialize bitmap
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    // set bitmap on imageView


                    ImageView qrView= findViewById(R.id.dependentQr);
                    qrView.setVisibility(View.VISIBLE);
                    qrView.setImageBitmap(bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GenerateQR","That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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