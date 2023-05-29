package com.makeitfeature.everypay;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class PaymentActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel Description";


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
        setContentView(R.layout.activity_payment);
        super.onCreate(savedInstanceState);

        TextView PaymentTitle = findViewById(R.id.PaymentTitle);
        PaymentTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView qrHint = findViewById(R.id.qrHint);
                qrHint.setVisibility(View.INVISIBLE);

                ImageView qrView= findViewById(R.id.qrView);
                qrView.setVisibility(View.INVISIBLE);
                ImageView scanSucceed= findViewById(R.id.scanSucceed);
                scanSucceed.setVisibility(View.VISIBLE);
            }
        });
        TextView qrHint = findViewById(R.id.qrHint);
        qrHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
        TextView myTextView = findViewById(R.id.qrHint);
        myTextView.setVisibility(View.INVISIBLE);
        showQR();
    }

    private void showQR() {

        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.generateQR)+"12345";
        // Request a string response from the provided URL.
        String data = "iVBORw0KGgoAAAANSUhEUgAAAFwAAABcCAYAAADj79JYAAAAAklEQVR4AewaftIAAAJXSURBVO3BQY4bQQwEwSxi/v/ltI6ED71oSEvLACPiC2tMsUYVa1SxRj38JQmTVLokdCpdEm6odEmYpNIVa1SxRhVr1MMPVD4pCScq71C5ofJJSTgp1qhijSrWqIdLSbihciMJJypdEj4pCTdUbhRrVLFGFWvUw39OpUtCp/JNijWqWKOKNerhy6mcJKFT+WbFGlWsUcUa9XBJ5TepdEm4kYRO5YbKbyrWqGKNKtaohx8kYVISOpUuCZ1Kl4QbSZhUrFHFGlWsUfGF/1gSTlS+SbFGFWtUsUY9/CUJ71A5SUKn0iXhk5LQqZwk4YZKl4ROpSvWqGKNKtao+EKThBOVLgk3VLok/EsqJ0l4h0pXrFHFGlWsUQ8/UHmHSpeETuUkCZ1Kl4RO5ZNUPqlYo4o1qlijHv6i0iXhRKVLwonKDZVPSsINlS4JnUqXhE6lK9aoYo0q1qj4wgcl4YbKjSR0Kl0SOpWTJHQqN5LQqZwUa1SxRhVrVHzhiyShU+mScKJykoRO5SQJncpJEjqVrlijijWqWKPiC00SJqncSMINlZMkdCq/qVijijWqWKMefqDySUn4JJVPSkKn0iXhROWkWKOKNapYox4uJeGGyjuScCMJn5SETuUkCZ1KV6xRxRpVrFEPXyYJnUqXhC4JncpJEt6RhBOVk2KNKtaoYo16+HJJOFHpktCp3FD5TcUaVaxRxRr1cEllkkqXhBOVLgmdSpeEG0m4odIVa1SxRhVr1MMPkvAvJeE3qXRJ6FQ6lS4JncpJsUYVa1SxRsUX1phijSrWqGKN+gPcmwm2zII9sAAAAABJRU5ErkJggg==";
        // Display the first 500 characters of the response string.
        byte[] bytes=Base64.decode(data.replace("data:image/png;base64,",""), Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView


        ImageView qrView= findViewById(R.id.qrView);
        qrView.setVisibility(View.VISIBLE);
        qrView.setImageBitmap(bitmap);
        TextView qrHint = findViewById(R.id.qrHint);
        qrHint.setVisibility(View.VISIBLE);

    }

    public void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Random randNum = new Random();
        String money = randNum.ints(0,100).findFirst().getAsInt()
                + "."+ randNum.ints(0,100).findFirst().getAsInt() ;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Your dependent paid to merchant.")
                .setContentText("RM "+money+"has been transferred")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
        createNotificationChannel();
        Toast.makeText(getApplicationContext(),"Your dependent paid RM "+money+" has been transfered.",Toast.LENGTH_LONG).show();
    }

    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESCRIPTION);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void goGDSelection(View view){
        Intent i = new Intent(PaymentActivity.this, GDSelection.class);
        startActivity(i);
        finish();
    }


}

