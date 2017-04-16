package com.ngbcode.leanonme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngbcode.leanonme.pages.EventsActivity;
import com.ngbcode.leanonme.pages.ResearchActivity;

public class MainActivity extends AppCompatActivity {

    public static final String CLASS_NAME = "com.ngbcode.leanonme.MainActivity";
    public static final String TAG = "Main";

    Button researchButton;
    Button eventsButton;
    Button chatButton;
    Button journalButton;
    Intent intent;

    // Firebase variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();

        // Create the buttons for this activity
        researchButton = (Button) findViewById(R.id.researchPageButton);
        eventsButton = (Button) findViewById(R.id.eventsPageButton);
        chatButton = (Button) findViewById(R.id.chatPageButton);
        journalButton = (Button) findViewById(R.id.journalPageButton);

        // Manage firebase variables
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void goToResearch(View view) {
        intent = new Intent(this, ResearchActivity.class);
        startActivity(intent);
    }
    public void goToEvents(View view) {
        intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
        /*Snackbar sb = Snackbar.make(findViewById(R.id.eventsPageButton),
                "Currently being updated", Snackbar.LENGTH_LONG);
        View snackbarView = sb.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sb.show();*/
    }
    public void goToChat(View view) {
        // TODO: go to ChatActivity with a new intent
        Snackbar sb = Snackbar.make(findViewById(R.id.chatPageButton),
                "Currently being updated", Snackbar.LENGTH_LONG);
        View snackbarView = sb.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sb.show();
    }
    public void goToJournal(View view) {
        // TODO: go to JournalActivity with a new intent
        Snackbar sb = Snackbar.make(findViewById(R.id.journalPageButton),
                "Currently being updated", Snackbar.LENGTH_LONG);
        View snackbarView = sb.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sb.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        // TODO: update about setting
        // TODO: fixed the closing of the activity on logout
        if(id == R.id.logout_setting) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
