package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        if (session.getUserEmail() != null) {
            // Already logged in
            startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
        } else {
            // Go to login
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}
