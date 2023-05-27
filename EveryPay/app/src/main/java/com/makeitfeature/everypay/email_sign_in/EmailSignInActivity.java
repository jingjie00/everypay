package com.makeitfeature.everypay.email_sign_in;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.makeitfeature.everypay.R;

import java.util.Locale;

public class EmailSignInActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;

    SensorManager sensorManager;
    private FirebaseAuth mAuth;


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_sign_in_activity);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Button nextBtn = findViewById(R.id.next_button);
        EditText editText = findViewById(R.id.email_address_input);
        TextView error = findViewById(R.id.email_address_error);

        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        editText.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void afterTextChanged(Editable s) {
                String email = editText.getText().toString();

                if (email.matches(emailPattern) && email.length() > 0) {
                    error.setText(getString(R.string.email_valid_prompt));
                    error.setTextColor(getBaseContext().getColor(R.color.black));
                } else {
                    error.setText(getString(R.string.email_invalid_prompt));
                    error.setTextColor(getBaseContext().getColor(R.color.purple_200));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        mAuth = FirebaseAuth.getInstance();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString();

                if(!email.matches(emailPattern) && email.length() > 0){
                    Toast.makeText(getApplicationContext(), getString(R.string.email_empty_prompt), Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                if (task.isSuccessful()) {
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if (isNewUser) {
                                        Intent intent = new Intent(EmailSignInActivity.this, EmailSignUpActivity.class);
                                        intent.putExtra("email_address", email);
                                        startActivity(intent);
                                    } else{
                                            Intent intent = new Intent(EmailSignInActivity.this, EmailSignInValidationActivity.class);
                                            intent.putExtra("email_address", email);
                                            startActivity(intent);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
