package com.ngbcode.leanonme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "Register";
    public static final String CLASS_NAME = "com.ngbcode.leanonme.RegisterActivity";

    // View variables
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    private EditText fnameView;
    private EditText lnameView;
    private EditText phoneView;

    // Registration variables
    private String email;
    private String password;
    private boolean registrationSuccessful;

    // Firebase variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fnameView = (EditText) findViewById(R.id.register_fname);
        lnameView = (EditText) findViewById(R.id.register_lname);
        phoneView = (EditText) findViewById(R.id.register_phone_number);
        emailView = (EditText) findViewById(R.id.register_email);
        passwordView = (EditText) findViewById(R.id.register_password);
        passwordConfirmView = (EditText) findViewById(R.id.register_password_confirm);

    }

    public void registerUser(View view) {
        // TODO: store new user values

        email = emailView.getText().toString();
        password = passwordConfirmView.getText().toString();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                registrationSuccessful = true;
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                    registrationSuccessful = false;
                }

            }
        });
        if(registrationSuccessful) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String fname = fnameView.getText().toString();
        if (TextUtils.isEmpty(fname)) {
            fnameView.setError("First name cannot be empty.");
            valid = false;
        } else {
            fnameView.setError(null);
        }

        String lname = lnameView.getText().toString();
        if (TextUtils.isEmpty(fname)) {
            lnameView.setError("Last name cannot be empty.");
            valid = false;
        } else {
            lnameView.setError(null);
        }

        String email = emailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailView.setError("Required.");
            valid = false;
        } else if(!email.contains("@")) {
            emailView.setError("Invalid email address.");
            valid = false;
        } else {
            emailView.setError(null);
        }

        String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordView.setError("Required.");
            valid = false;
        } else if(password.length() < 6) {
            passwordView.setError("6 characters required.");
            valid = false;
        } else {
            passwordView.setError(null);
        }

        String passwordConfirm = passwordConfirmView.getText().toString();
        if (!passwordConfirm.equals(password)) {
            passwordConfirmView.setError("Must match password above.");
            valid = false;
        } else {
            passwordConfirmView.setError(null);
        }

        return valid;
    }
}
