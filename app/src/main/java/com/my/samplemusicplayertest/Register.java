package com.my.samplemusicplayertest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Find views by ID
        usernameEditText = findViewById(R.id.RegisterUsername);
        emailEditText = findViewById(R.id.RegisterEmail);
        passwordEditText = findViewById(R.id.RegisterPassword);
        registerButton = findViewById(R.id.registered);

        // Set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the inputs (e.g., check if fields are empty)

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // Perform any additional actions after registration

                            // Navigate back to the Login activity
                            navigateToLogin();
                        } else {
                            // Registration failed
                            // Display an error message
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            showErrorAlert(errorMessage);
                        }
                    }
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity to prevent the user from going back to the registration screen
    }

    private void showErrorAlert(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Failed")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }
}