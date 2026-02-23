package com.example.daily_mood_journal.models;

public class MoodEntry {
    private int id;
    private int userId;
    private String moodType;
    private String note;
    private String date;

    // --- SETTERS ---

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }
    // --- GETTERS ---

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getMoodType() {
        return moodType;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }
}
