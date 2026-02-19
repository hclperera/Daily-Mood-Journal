package com.example.daily_mood_journal.database;

import android.content.Context;
public class MoodDAO {

    private DatabaseHelper dbHelper;

    public MoodDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
}