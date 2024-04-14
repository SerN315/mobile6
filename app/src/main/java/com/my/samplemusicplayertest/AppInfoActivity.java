package com.my.samplemusicplayertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}