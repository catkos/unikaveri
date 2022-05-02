package com.example.unikaveri;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference signaturePreference = findPreference("signature");

    }
}
