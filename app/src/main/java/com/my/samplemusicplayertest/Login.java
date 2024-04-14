package com.my.samplemusicplayertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerButton;

    private FirebaseAuth mAuth;
    private LoginCallback loginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.loginbutton);
        registerButton = findViewById(R.id.SignupLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Register layout
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userEmail = user.getEmail(); // Get the email of the user
                            Toast.makeText(Login.this, "Welcome, " + userEmail, Toast.LENGTH_SHORT).show();
                            if (loginCallback != null) {
                                loginCallback.onLoginSuccess(userEmail);
                            }
                            navigateToHome();
                        }
                    } else {
                        // Login failed
                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the login activity to prevent going back to it with the back button
    }

    public interface LoginCallback {
        void onLoginSuccess(String username);
    }

    public void setLoginCallback(LoginCallback callback) {
        this.loginCallback = callback;
    }
}