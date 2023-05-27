package com.makeitfeature.everypay.email_sign_in;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.makeitfeature.everypay.R;
import com.makeitfeature.everypay.RestoreInitialiseActivity;

import java.util.Locale;

public class EmailSignUpActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;


    SensorManager sensorManager;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private EditText password;



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
        setContentView(R.layout.email_sign_up_activity);

        mAuth = FirebaseAuth.getInstance();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        dialog = new Dialog(EmailSignUpActivity.this);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        String email = getIntent().getStringExtra("email_address");
        TextView welcome = findViewById(R.id.welcome);
        welcome.setText(getString(R.string.email_welcome_prompt1) +" "+ email+ getString(R.string.email_welcome_prompt2));


        Button signUpBtn = findViewById(R.id.sign_up_button);
        EditText username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        TextView uError = findViewById(R.id.username_error);
        TextView pError = findViewById(R.id.password_error);

        username.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void afterTextChanged(Editable s) {
                String name = username.getText().toString();


                if (name.length() > 0 && name.length() <= 16) {
                    uError.setText(getString(R.string.email_username_valid));
                    uError.setTextColor(getBaseContext().getColor(R.color.black));
                } else {
                    uError.setText(R.string.email_username_invalid);
                    uError.setTextColor(getBaseContext().getColor(R.color.red));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void afterTextChanged(Editable s) {
                String pass = password.getText().toString();


                if (pass.length() > 6 && pass.length() <= 16) {
                    pError.setText(getString(R.string.email_password_valid));
                    pError.setTextColor(getBaseContext().getColor(R.color.black));
                } else {
                    pError.setText(getString(R.string.email_password_invalid));
                    pError.setTextColor(getBaseContext().getColor(R.color.red));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if (!fieldValidation(name, pass))
                    return;
                //submit
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(EmailSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(EmailSignUpActivity.this, getString(R.string.email_auth_failed) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), getString(R.string.email_hi) +" "+ name + getString(R.string.email_account_created_and_login), Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(EmailSignUpActivity.this, RestoreInitialiseActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(EmailSignUpActivity.this,getString(R.string.email_auth_failed) + task.getException(),
                                                                Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }
                                            });


                                }
                            }
                        });

            }
        });
    }

    private boolean fieldValidation(String name, String pass) {
        boolean flag = true;
        if (!(name.length() > 0 && name.length() < 20)) {
            Toast.makeText(getApplicationContext(), getString(R.string.email_username_invalid_p), Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if (!(pass.length() >= 6 && pass.length() <= 16)) {
            Toast.makeText(getApplicationContext(), getString(R.string.email_password_invalid_p), Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    public void ShowHidePass(View view) {

        if(view.getId()==R.id.show_pass_btn){
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_pass);
                view.setContentDescription(getString(R.string.email_password_hide));
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.show_pass);
                view.setContentDescription(getString(R.string.email_password_show));
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}
