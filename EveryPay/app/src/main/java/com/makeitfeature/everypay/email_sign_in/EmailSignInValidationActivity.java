package com.makeitfeature.everypay.email_sign_in;

import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.makeitfeature.everypay.R;
import com.makeitfeature.everypay.RestoreInitialiseActivity;

public class EmailSignInValidationActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;

    SensorManager sensorManager;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private EditText password;
    private TextView pError;



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
        setContentView(R.layout.email_sign_in_validation_activity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(EmailSignInValidationActivity.this);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        String email = getIntent().getStringExtra("email_address");
        TextView welcome = findViewById(R.id.welcome);

        welcome.setText(getString(R.string.email_sign_in_prompt_h) +" " + email +" " + getString(R.string.email_sign_in_prompt_t));

        Button signInButton = findViewById(R.id.sign_in_button);
        Button forgetPasswordButton = findViewById(R.id.forget_password_button);
        password = findViewById(R.id.password_input);
        pError = findViewById(R.id.password_error);

        password.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void afterTextChanged(Editable s) {
                String pass = password.getText().toString();


                if (pass.length() > 0 && pass.length() <= 16) {
                    pError.setText(getString(R.string.email_sign_in_pass_valid));
                    pError.setTextColor(getBaseContext().getColor(R.color.black));
                } else if (pass.length() == 0) {
                    pError.setText(getString(R.string.email_sign_in_pass_invalid_1));
                    pError.setTextColor(getBaseContext().getColor(R.color.red));
                } else {
                    pError.setText(R.string.email_sign_in_pass_invalid_2);
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


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                if (!(pass.length() > 0 && pass.length() <= 16)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.email_password_wrong_prompt1), Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();
                mAuth.signInWithEmailAndPassword(email, password.getText().toString())

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();

                                if (!task.isSuccessful()) {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(EmailSignInValidationActivity.this, getString(R.string.email_password_wrong_prompt2),
                                                Toast.LENGTH_SHORT).show();
                                        password.setText("");
                                        pError.setText(getString(R.string.email_password_wrong_prompt2));
                                    } else {
                                        Toast.makeText(EmailSignInValidationActivity.this, getString(R.string.email_auth_failed) + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.email_hi) +" "+ mAuth.getCurrentUser().getDisplayName() + getString(R.string.email_sign_in_success), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EmailSignInValidationActivity.this, RestoreInitialiseActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("restore_backup", true);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Dialog resetdialog = new AlertDialog.Builder(EmailSignInValidationActivity.this)
                                            .setTitle(R.string.email_reset_title)
                                            .setMessage(getString(R.string.reset_content1) + email + getString(R.string.reset_content2))
                                            .setIcon(R.drawable.email_sent)
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.reset_content3), Toast.LENGTH_SHORT).show();
                                                }
                                            }).show();
                                    resetdialog.getWindow().setBackgroundDrawableResource(R.color.lightyellow);

                                }
                            }
                        });
            }
        });
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {

                ((ImageView) (view)).setImageResource(R.drawable.hide_pass);
                view.setContentDescription(getString(R.string.email_password_hide));
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.show_pass);
                view.setContentDescription(getString(R.string.email_password_show));
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
