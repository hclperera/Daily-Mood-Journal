package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.R;
import com.example.daily_mood_journal.activities.DashboardActivity;
import com.example.daily_mood_journal.database.MoodDAO;
import com.example.daily_mood_journal.database.UserDAO;
import com.example.daily_mood_journal.models.MoodEntry;
import com.example.daily_mood_journal.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditMoodActivity extends AppCompatActivity {

    Spinner moodSpinner;
    EditText noteEdit;
    Button btnUpdate, btnDelete, btnBackHome;
    MoodDAO moodDAO;
    SessionManager session;
    UserDAO userDAO;
    int moodId;
    MoodEntry moodEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);

        // Initialize views
        moodSpinner = findViewById(R.id.spinnerMood);
        noteEdit = findViewById(R.id.etNote);
        btnUpdate = findViewById(R.id.btnUpdateMood);
        btnDelete = findViewById(R.id.btnDeleteMood);
        btnBackHome = findViewById(R.id.btnBackHome);

        moodDAO = new MoodDAO(this);
        session = new SessionManager(this);
        userDAO = new UserDAO(this);

        // Get moodId from intent (default -1)
        moodId = getIntent().getIntExtra("moodId", -1);

        // Load mood data based on moodId
        loadMoodData();

        // ----------------------
        // Update Mood
        // ----------------------
        btnUpdate.setOnClickListener(v -> {
            if (moodEntry == null) return;

            String mood = moodSpinner.getSelectedItem().toString();
            String note = noteEdit.getText().toString().trim();

            moodEntry.setMoodType(mood);
            moodEntry.setNote(note);
            moodEntry.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));

            if (moodDAO.updateMood(moodEntry)) {
                Toast.makeText(this, "Mood updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        // ----------------------
        // Delete Mood
        // ----------------------
        btnDelete.setOnClickListener(v -> {
            if (moodEntry != null && moodDAO.deleteMood(moodEntry.getId())) {
                Toast.makeText(this, "Mood deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });

        // ----------------------
        // Back to Home
        // ----------------------
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(EditMoodActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadMoodData() {
        String email = session.getUserEmail();
        int userId = userDAO.getUserId(email);

        List<MoodEntry> moods = moodDAO.getMoods(userId);

        if (moods.isEmpty()) {
            Toast.makeText(this, "No moods found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // If no moodId passed, pick the *latest mood*
        if (moodId == -1) {
            moodEntry = moods.get(0); // moods list is DESC sorted by date
        } else {
            // Find the mood by id
            for (MoodEntry m : moods) {
                if (m.getId() == moodId) {
                    moodEntry = m;
                    break;
                }
            }
            if (moodEntry == null) {
                Toast.makeText(this, "Mood not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // Populate UI
        noteEdit.setText(moodEntry.getNote());

        for (int i = 0; i < moodSpinner.getCount(); i++) {
            if (moodSpinner.getItemAtPosition(i).toString().equals(moodEntry.getMoodType())) {
                moodSpinner.setSelection(i);
                break;
            }
        }
    }
}