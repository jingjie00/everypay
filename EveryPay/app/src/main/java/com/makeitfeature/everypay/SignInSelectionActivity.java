package com.makeitfeature.everypay;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.makeitfeature.everypay.email_sign_in.EmailSignInActivity;
import com.makeitfeature.everypay.utils.Preferences;

import java.util.Locale;

public class SignInSelectionActivity extends AppCompatActivity {
    String ACTIVITY_DESCRIPTION;

    SensorManager sensorManager;
    private Dialog dialog;



    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_selection_activity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        TextView moreInfo = findViewById(R.id.more_info);
        moreInfo.setMovementMethod(LinkMovementMethod.getInstance());

        dialog = new Dialog(SignInSelectionActivity.this);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SignInSelectionActivity.this, gso);
        mGoogleSignInClient.signOut();

        Button googleSignIn = findViewById(R.id.google);
        Button emailSignIn = findViewById(R.id.email);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Preferences.writeBooleanToPreference(getApplicationContext(), Preferences.SIGNIN_ANOMYNOUS, false);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Preferences.writeBooleanToPreference(getApplicationContext(), Preferences.SIGNIN_ANOMYNOUS, false);
                Intent intent = new Intent(SignInSelectionActivity.this, EmailSignInActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult();
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.sign_in_failed) + e, Toast.LENGTH_SHORT).show();
            }
        }
        dialog.dismiss();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.hi) +" "+ mAuth.getCurrentUser().getDisplayName() + getString(R.string.account_google_created), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.hi) +" "+ mAuth.getCurrentUser().getDisplayName() + getString(R.string.account_google_welcome_back), Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(SignInSelectionActivity.this, RestoreInitialiseActivity.class);
                        intent.putExtra("restore_backup", true);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
