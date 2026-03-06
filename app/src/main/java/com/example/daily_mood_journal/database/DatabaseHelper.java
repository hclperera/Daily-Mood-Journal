package com.example.daily_mood_journal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moodjournal.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Moods table
    public static final String TABLE_MOODS = "moods";
    public static final String COLUMN_MOOD_ID = "id";
    public static final String COLUMN_MOOD_USER_ID = "user_id";
    public static final String COLUMN_MOOD_TYPE = "mood";
    public static final String COLUMN_MOOD_NOTE = "note";
    public static final String COLUMN_MOOD_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        String CREATE_MOODS_TABLE = "CREATE TABLE " + TABLE_MOODS + "("
                + COLUMN_MOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MOOD_USER_ID + " INTEGER,"
                + COLUMN_MOOD_TYPE + " TEXT,"
                + COLUMN_MOOD_NOTE + " TEXT,"
                + COLUMN_MOOD_DATE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_MOOD_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_MOODS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}