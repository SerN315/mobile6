package com.my.samplemusicplayertest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageChanger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_root_navigation_bar);

        LinearLayout languagesLayout = findViewById(R.id.languages);
        languagesLayout.setOnClickListener(v -> showLanguagePopup());
    }

    private void showLanguagePopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(popupView);
        AlertDialog alertDialog = dialogBuilder.create();

        CheckBox englishCheckbox = popupView.findViewById(R.id.checkbox_english);
        CheckBox spanishCheckbox = popupView.findViewById(R.id.checkbox_vietnamese);
        CheckBox frenchCheckbox = popupView.findViewById(R.id.checkbox_french);

        englishCheckbox.setOnClickListener(v -> {
            setLocale("en");
            alertDialog.dismiss();
        });

        spanishCheckbox.setOnClickListener(v -> {
            setLocale("es");
            alertDialog.dismiss();
        });

        frenchCheckbox.setOnClickListener(v -> {
            setLocale("fr");
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void setLocale(String languageCode) {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        recreate();
    }
}