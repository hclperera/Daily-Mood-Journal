package com.example.daily_mood_journal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MoodSession";
    private static final String KEY_EMAIL = "email";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save user email when logged in
    public void saveUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Get logged in user email
    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Logout user
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return getUserEmail() != null;
    }
}
