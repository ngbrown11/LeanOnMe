package com.ngbcode.leanonme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "Register";
    public static final String CLASS_NAME = "com.ngbcode.leanonme.RegisterActivity";

    // View variables
    private TextInputEditText emailView;
    private TextInputEditText passwordView;
    private TextInputEditText passwordConfirmView;
    private TextInputEditText fnameView;
    private TextInputEditText lnameView;
    private TextInputEditText phoneView;
    private ProgressBar progressBar;
    private View registerFormView;

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

        fnameView = (TextInputEditText) findViewById(R.id.register_fname);
        lnameView = (TextInputEditText) findViewById(R.id.register_lname);
        phoneView = (TextInputEditText) findViewById(R.id.register_phone_number);
        emailView = (TextInputEditText) findViewById(R.id.register_email);
        passwordView = (TextInputEditText) findViewById(R.id.register_password);
        passwordConfirmView = (TextInputEditText) findViewById(R.id.register_password_confirm);
        progressBar = (ProgressBar) findViewById(R.id.register_progress);

        // Manage firebase user
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void registerUser(View view) {
        // TODO: store new user values

        email = emailView.getText().toString();
        password = passwordConfirmView.getText().toString();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgress(true);

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
                    showProgress(registrationSuccessful);
                    return;
                }
                else {
                    showProgress(false);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(authStateListener != null)
            auth.removeAuthStateListener(authStateListener);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            registerFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
