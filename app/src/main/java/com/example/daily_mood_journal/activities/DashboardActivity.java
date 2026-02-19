package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.R;
import com.example.daily_mood_journal.database.MoodDAO;
import com.example.daily_mood_journal.database.UserDAO;
import com.example.daily_mood_journal.utils.SessionManager;


public class DashboardActivity extends AppCompatActivity {

    Button btnAddMood, btnViewHistory, btnLogout;
    TextView welcomeText;
    SessionManager session;
    UserDAO userDAO;
    MoodDAO moodDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        btnAddMood = findViewById(R.id.btnAddMood);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        //btnEditMood = findViewById(R.id.btnEditMood); // Make sure this exists in XML
        btnLogout = findViewById(R.id.btnLogout);
        welcomeText = findViewById(R.id.tvWelcome);

        // Initialize DAOs and session
        session = new SessionManager(this);
        userDAO = new UserDAO(this);
        moodDAO = new MoodDAO(this);

        // Get user info
        String email = session.getUserEmail();
        if (email != null) {
            String name = userDAO.getUserNameByEmail(email);
            welcomeText.setText("Welcome, " + name);
        } else {
            welcomeText.setText("Welcome!");
        }

        // Add Mood button
        btnAddMood.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AddMoodActivity.class)));

        // View History button
        btnViewHistory.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, MoodHistoryActivity.class)));

        // Logout button
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
}