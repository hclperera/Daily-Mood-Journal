package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.R;
import com.example.daily_mood_journal.database.MoodDAO;
import com.example.daily_mood_journal.database.UserDAO;
import com.example.daily_mood_journal.models.MoodEntry;
import com.example.daily_mood_journal.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMoodActivity extends AppCompatActivity {

    Spinner moodSpinner;
    EditText noteEdit;
    Button btnAdd, btnBackHome;
    MoodDAO moodDAO;
    SessionManager session;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        moodSpinner = findViewById(R.id.spinnerMood);
        noteEdit = findViewById(R.id.etNote);
        btnAdd = findViewById(R.id.btnAddMood);
        btnBackHome = findViewById(R.id.btnBackHome);

        moodDAO = new MoodDAO(this);
        session = new SessionManager(this);
        userDAO = new UserDAO(this);

        // Add Mood
        btnAdd.setOnClickListener(v -> {
            String mood = moodSpinner.getSelectedItem().toString();
            String note = noteEdit.getText().toString().trim();
            String email = session.getUserEmail();
            int userId = userDAO.getUserId(email);

            MoodEntry moodEntry = new MoodEntry();
            moodEntry.setUserId(userId);
            moodEntry.setMoodType(mood);
            moodEntry.setNote(note);
            moodEntry.setDate(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            .format(new Date())
            );

            if (moodDAO.addMood(moodEntry)) {
                Toast.makeText(this, "Mood added successfully", Toast.LENGTH_SHORT).show();
                finish(); // returns to previous (Home)
            } else {
                Toast.makeText(this, "Failed to add mood", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to Home
        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(AddMoodActivity.this, SplashActivity.class));
            finish();
        });
    }
}
