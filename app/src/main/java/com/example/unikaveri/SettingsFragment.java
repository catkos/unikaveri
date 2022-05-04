package com.example.unikaveri;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.TextView;

import androidx.annotation.Nullable;
/**
 * Settings fragment for calling preference resources
 */

//Might not look like much but deleting this
//causes issues on a galaxy brain scale

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}