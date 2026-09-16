package com.example.flamepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnCreateAccount;
    private LinearLayout btnGoogle;
    private LinearLayout btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        View scrollView = findViewById(R.id.signUpScrollView);
        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
    }

    private void setupClickListeners() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Google Sign Up Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Facebook Sign Up Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSignUp() {
        String input = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 1. Basic Empty Check
        if (input.isEmpty()) {
            etEmail.setError("Email or Username required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }

        // 2. Format Validation
        if (input.contains("@")) {
            // Strict Gmail check for testers
            if (!(input.toLowerCase().endsWith("@gmail.com") && input.length() > 10)) {
                etEmail.setError("Please use a valid Gmail address");
                etEmail.requestFocus();
                return;
            }
        } else {
            // Username check
            if (!(input.length() >= 3 && input.length() <= 15 && input.matches("^[a-zA-Z0-9_]*$"))) {
                etEmail.setError("Username must be 3-15 characters (no spaces)");
                etEmail.requestFocus();
                return;
            }
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm Password required");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Simulated Success
        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(SignUpActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
