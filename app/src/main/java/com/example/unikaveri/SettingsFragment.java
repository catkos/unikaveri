package com.example.unikaveri;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.TextView;

import androidx.annotation.Nullable;
/**
 * Call for preference resources
 */

//I don't actually remember if I need this
//but I am not gonna delete it and find out
//so, enjoy a possibly useless class

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}