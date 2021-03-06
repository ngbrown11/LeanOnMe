package com.ngbcode.leanonme.pages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngbcode.leanonme.LoginActivity;
import com.ngbcode.leanonme.R;
import com.ngbcode.leanonme.WebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchActivity extends AppCompatActivity {

    public static final String CLASS_NAME = "com.ngbcode.leanonme.ResearchActivity";
    public static final String TAG = "Research";

    List<String> infoList = new ArrayList<String>();
    //List<Map<String, String>> infoList = new ArrayList<Map<String, String>>();
    Map<String, String> info = new HashMap<String, String>();
    String[] newInfoList;
    String[] linkNames;

    // Firebase variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        // List of web pages
        /*info.put("Breast Cancer", "https://www.cancer.gov/types/breast");
        info.put("Breast Cancer Treatments", "https://www.cancer.gov/types/breast/patient/breast-treatment-pdq");
        info.put("Google", "https://www.google.com");*/

        // TODO: add title to each list link
        final ListView listView = (ListView) findViewById(android.R.id.list);
        infoList.add("Breast Cancer (Gov site)");
        infoList.add("Breast Cancer Types (Cancer center)");
        infoList.add("Treatment Information (Gov site)");
        infoList.add("Get Involved (Community)");
        infoList.add("Breast Cancer Coach");
        infoList.add("Breast Cancer Survivors");
        infoList.add("Google");
        infoList.toArray();
        // infoList.add(info);
        linkNames = new String[7];
        linkNames[0] = "https://www.cancer.gov/types/breast";
        linkNames[1] = "http://www.cancercenter.com/breast-cancer/types/";
        linkNames[2] = "https://www.cancer.gov/types/breast/patient/breast-treatment-pdq";
        linkNames[3] = "http://www.breastcancer.org/community";
        linkNames[4] = "http://www.mybreastcancercoach.org/";
        linkNames[5] = "http://www.thebreastcancersurvivorsnetwork.org/home0.aspx";
        linkNames[6] = "https://www.google.com";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, infoList);

        listView.setAdapter(adapter);

        // Add click listener for webview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ResearchActivity.this, WebViewActivity.class);
                String url = (String) listView.getItemAtPosition(i).toString();
                url = linkNames[i];
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        // TODO: update about setting

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
