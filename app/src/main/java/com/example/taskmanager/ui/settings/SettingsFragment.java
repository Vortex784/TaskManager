package com.example.taskmanager.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;

import java.util.Locale;
public class SettingsFragment extends Fragment {

    private Switch switchDarkMode;
    private Switch switchNotifications;
    private Spinner spinnerLanguage;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        // Initialize UI elements
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        spinnerLanguage = view.findViewById(R.id.spinner_language);

        // Load saved preferences
        switchDarkMode.setChecked(sharedPreferences.getBoolean("dark_mode", false));
        switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", true));

        // Dark Mode Toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Notifications Toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notifications", isChecked);
            editor.apply();
        });

        // Language Selector
        String[] languages = {"English", "Polski", "Українська"};
        String[] languageCodes = {"en", "pl", "uk"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages);
        spinnerLanguage.setAdapter(adapter);

        // Set selected language
        String currentLang = sharedPreferences.getString("language", "en");
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(currentLang)) {
                spinnerLanguage.setSelection(i);
                break;
            }
        }

        // Change language on selection
        spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = languageCodes[position];
                if (!selectedLanguage.equals(sharedPreferences.getString("language", "en"))) {
                    changeLanguage(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void changeLanguage(String langCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", langCode);
        editor.apply();

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        requireActivity().recreate(); // Restart activity to apply changes
    }
}
