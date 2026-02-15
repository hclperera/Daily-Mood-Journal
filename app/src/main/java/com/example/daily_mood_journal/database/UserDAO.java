package com.example.daily_mood_journal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.daily_mood_journal.utils.PasswordUtils;

public class UserDAO {

    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Register user
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, PasswordUtils.hashPassword(password));

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Validate login
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COLUMN_USER_PASSWORD +
                " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_USER_EMAIL + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            cursor.close();
            db.close();
            return PasswordUtils.verifyPassword(password, storedHash);
        }
        return false;
    }
}