package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.R;
import com.example.daily_mood_journal.database.MoodDAO;
import com.example.daily_mood_journal.database.UserDAO;
import com.example.daily_mood_journal.models.MoodEntry;
import com.example.daily_mood_journal.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MoodHistoryActivity extends AppCompatActivity {

    ListView listView;
    Button btnBackHome;
    MoodDAO moodDAO;
    UserDAO userDAO;
    SessionManager session;
    List<MoodEntry> moods;

    private static final int EDIT_MOOD_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        listView = findViewById(R.id.listViewMoods);
        btnBackHome = findViewById(R.id.btnBackHome);

        moodDAO = new MoodDAO(this);
        userDAO = new UserDAO(this);
        session = new SessionManager(this);

        loadMoods();

        // Click to edit mood
        listView.setOnItemClickListener((parent, view, position, id) -> {
            MoodEntry selectedMood = moods.get(position);
            Intent intent = new Intent(MoodHistoryActivity.this, EditMoodActivity.class);
            intent.putExtra("moodId", selectedMood.getId());
            startActivityForResult(intent, EDIT_MOOD_REQUEST); // to refresh after edit/delete
        });

        // Back to Home
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(MoodHistoryActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadMoods() {
        String email = session.getUserEmail();
        int userId = userDAO.getUserId(email);
        moods = moodDAO.getMoods(userId);

        List<String> moodStrings = new ArrayList<>();
        for (MoodEntry m : moods) {
            moodStrings.add(m.getDate() + " - " + m.getMoodType() + " : " + m.getNote());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, moodStrings);
        listView.setAdapter(adapter);

        if (moods.isEmpty()) {
            Toast.makeText(this, "No moods recorded yet", Toast.LENGTH_SHORT).show();
        }
    }

    // Refresh list after edit/delete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_MOOD_REQUEST) {
            loadMoods(); // reload updated moods
        }
    }
}