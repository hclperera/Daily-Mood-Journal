package com.example.daily_mood_journal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daily_mood_journal.R;
import com.example.daily_mood_journal.database.UserDAO;
import com.example.daily_mood_journal.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginBtn;
    private TextView registerRedirect;
    private UserDAO userDAO;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEdit = findViewById(R.id.etEmail);
        passwordEdit = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        registerRedirect = findViewById(R.id.tvRegisterRedirect);

        // Initialize DAO and session manager
        userDAO = new UserDAO(this);
        session = new SessionManager(this);

        // Check if user is already logged in
        if (session.isLoggedIn()) {
            navigateToDashboard();
        }

        // Login button click
        loginBtn.setOnClickListener(v -> handleLogin());

        // Redirect to RegisterActivity
        registerRedirect.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    // Handle login logic
    private void handleLogin() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Email is required");
            emailEdit.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Password is required");
            passwordEdit.requestFocus();
            return;
        }

        if (userDAO.loginUser(email, password)) {
            // Save session
            session.saveUserEmail(email);
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    // Navigate to Dashboard
    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}