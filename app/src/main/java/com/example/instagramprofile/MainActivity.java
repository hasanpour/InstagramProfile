package com.example.instagramprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = sharedPreferences.getString("prf_access_token", null);

        /* Show Profile */
        if (accessToken != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    /* Request Access Token if it didn't exist */
    public void openLogInActivity(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
