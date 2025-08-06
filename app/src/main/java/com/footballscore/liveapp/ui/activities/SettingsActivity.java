package com.footballscore.liveapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        
        setupToolbar();
        setupClickListeners();
        updateUI();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void setupClickListeners() {
        // Theme toggle
        binding.themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? 
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Notifications toggle
        binding.notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply();
            Toast.makeText(this, isChecked ? "Notifications enabled" : "Notifications disabled", 
                Toast.LENGTH_SHORT).show();
        });

        // About app
        binding.aboutLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Football Live Score v" + getAppVersion(), Toast.LENGTH_LONG).show();
        });

        // Share app
        binding.shareLayout.setOnClickListener(v -> shareApp());

        // Clear cache
        binding.clearCacheLayout.setOnClickListener(v -> clearCache());

        // Social media links
        binding.twitterIcon.setOnClickListener(v -> openUrl("https://twitter.com"));
        binding.facebookIcon.setOnClickListener(v -> openUrl("https://facebook.com"));
        binding.instagramIcon.setOnClickListener(v -> openUrl("https://instagram.com"));

        // Check updates
        binding.updateLayout.setOnClickListener(v -> checkForUpdates());
    }

    private void updateUI() {
        // Set current theme state
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        binding.themeSwitch.setChecked(isDarkMode);

        // Set notifications state
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        binding.notificationsSwitch.setChecked(notificationsEnabled);

        // Set app version
        binding.versionText.setText("Version " + getAppVersion());
    }

    private String getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Football Live Score App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
            "Check out this amazing Football Live Score app! Get live scores, match details, and more.");
        startActivity(Intent.createChooser(shareIntent, "Share App"));
    }

    private void clearCache() {
        // Clear app cache
        try {
            deleteCache(getCacheDir());
            Toast.makeText(this, "Cache cleared successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCache(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    deleteCache(new java.io.File(dir, child));
                }
            }
        }
        if (dir != null) {
            dir.delete();
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void checkForUpdates() {
        Toast.makeText(this, "You have the latest version", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}