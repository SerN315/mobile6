package com.my.samplemusicplayertest;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // "Saved Music"
            Preference savedMusicPreference = findPreference("saved_music");
            if (savedMusicPreference != null) {
                savedMusicPreference.setOnPreferenceClickListener(preference -> {
                    // Mở Activity hiển thị danh sách nhạc đã lưu

                    return true;
                });
            }

            //Chọn chat lượng âm thanh
            ListPreference soundQualityPreference = findPreference("sound_quality");
            if (soundQualityPreference != null) {
                soundQualityPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    String newQuality = (String) newValue;
                    applySoundQuality(newQuality);
                    return true;
                });
            }

            ListPreference fontSizePreference = findPreference("font_size");
            if (fontSizePreference != null) {
                fontSizePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    String fontSize = (String) newValue;
                    applyFontSize(fontSize);
                    return true;
                });
            }

            //Bật/tắt thông báo
            SwitchPreferenceCompat notificationPreference = findPreference("notifications");
            if (notificationPreference != null) {
                notificationPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean notificationsEnabled = (Boolean) newValue;
                    if (notificationsEnabled) {

                        enableNotifications();
                    } else {

                        disableNotifications();
                    }
                    return true;
                });
            }



            //Thông tin ứng dụng
            Preference appInfoPreference = findPreference("app_info");
            if (appInfoPreference != null) {
                appInfoPreference.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(getActivity(), AppInfoActivity.class);
                    startActivity(intent);
                    return true;
                });
            }



            // Xử lý preference cho theme
            SwitchPreferenceCompat darkModePreference = findPreference("dark_mode");
            if (darkModePreference != null) {
                darkModePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean darkModeEnabled = (Boolean) newValue;
                    AppCompatDelegate.setDefaultNightMode(darkModeEnabled ?
                            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                    return true;
                });
            }


        }

        private void applyFontSize(String fontSize) {
            Configuration configuration = getResources().getConfiguration();
            adjustFontScale(configuration, fontSize);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            getActivity().recreate();
        }

        private void adjustFontScale(Configuration configuration, String fontSize) {
            switch (fontSize) {
                case "small":
                    configuration.fontScale = 0.85f;
                    break;
                case "normal":
                    configuration.fontScale = 1.0f;
                    break;
                case "large":
                    configuration.fontScale = 1.15f;
                    break;
                default:
                    configuration.fontScale = 1.0f;  // Đặt mặc định là normal nếu không có giá trị khớp
                    break;
            }
        }

        private void applySoundQuality(String quality) {
            // Đường dẫn hoặc URI của bản nhạc
            String path128kbps = "path_to_128kbps_song";
            String path320kbps = "path_to_320kbps_song";

            String selectedPath = quality.equals("128") ? path128kbps : path320kbps;

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.reset(); // Đảm bảo MediaPlayer không chơi nhạc
                mediaPlayer.setDataSource(selectedPath); // Đặt nguồn dữ liệu mới
                mediaPlayer.prepare(); // Chuẩn bị MediaPlayer
                mediaPlayer.start(); // Bắt đầu phát
            } catch (IOException e) {
                Log.e("MediaPlayer", "Error setting data source", e);
            }
        }

        private void disableNotifications() {
            NotificationManagerCompat.from(getContext()).cancelAll();
        }

        private void enableNotifications() {
            NotificationManagerCompat.from(getContext()).cancelAll();
        }
    }
}
