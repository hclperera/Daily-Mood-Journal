package com.example.daily_mood_journal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.daily_mood_journal.models.MoodEntry;

import java.util.ArrayList;
import java.util.List;
public class MoodDAO {

    private DatabaseHelper dbHelper;

    public MoodDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Add mood
    public boolean addMood(MoodEntry mood) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MOOD_USER_ID, mood.getUserId());
        values.put(DatabaseHelper.COLUMN_MOOD_TYPE, mood.getMoodType());
        values.put(DatabaseHelper.COLUMN_MOOD_NOTE, mood.getNote());
        values.put(DatabaseHelper.COLUMN_MOOD_DATE, mood.getDate());

        long result = db.insert(DatabaseHelper.TABLE_MOODS, null, values);
        db.close();
        return result != -1;
    }
    // Get moods by user (most recent first)
    public List<MoodEntry> getMoods(int userId) {
        List<MoodEntry> moods = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_MOODS +
                " WHERE " + DatabaseHelper.COLUMN_MOOD_USER_ID + "=? " +
                " ORDER BY " + DatabaseHelper.COLUMN_MOOD_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MoodEntry mood = new MoodEntry();
                    mood.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD_ID)));
                    mood.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD_USER_ID)));
                    mood.setMoodType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD_TYPE)));
                    mood.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD_NOTE)));
                    mood.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOOD_DATE)));
                    moods.add(mood);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return moods;
    }

    // Update mood
    public boolean updateMood(MoodEntry mood) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MOOD_TYPE, mood.getMoodType());
        values.put(DatabaseHelper.COLUMN_MOOD_NOTE, mood.getNote());
        values.put(DatabaseHelper.COLUMN_MOOD_DATE, mood.getDate());

        int result = db.update(
                DatabaseHelper.TABLE_MOODS,
                values,
                DatabaseHelper.COLUMN_MOOD_ID + "=?",
                new String[]{String.valueOf(mood.getId())}
        );
        db.close();
        return result > 0;
    }

    // Delete mood
    public boolean deleteMood(int moodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(
                DatabaseHelper.TABLE_MOODS,
                DatabaseHelper.COLUMN_MOOD_ID + "=?",
                new String[]{String.valueOf(moodId)}
        );
        db.close();
        return result > 0;
    }

}