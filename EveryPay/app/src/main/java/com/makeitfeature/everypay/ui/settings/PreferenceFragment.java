package com.makeitfeature.everypay.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.makeitfeature.everypay.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}