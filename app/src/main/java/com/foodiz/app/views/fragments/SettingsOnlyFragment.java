package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.views.dialogs.LanguageDialogFragment;

/**
 * A simple {@link PreferenceFragmentCompat} subclass.
 */
public class SettingsOnlyFragment extends PreferenceFragmentCompat {

    private Preference mLangPreference;

    public SettingsOnlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLangPreference =  (Preference) getPreferenceManager().findPreference("language");

        switch (MainActivity.preferencesConfig.readLangStatus()){
            case "he":
                mLangPreference.setSummary(getString(R.string.hebrew));
                break;

            case "en":
                mLangPreference.setSummary(getString(R.string.english));
                break;
        }

        mLangPreference.setOnPreferenceClickListener(preference -> {
            // open language dialog
            LanguageDialogFragment languageDialogFragment = new LanguageDialogFragment();
            languageDialogFragment.show(MainActivity.appFragmentManager, "languageDialogFragment");


            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (MainActivity.preferencesConfig.readLangStatus()){
            case "he":
                mLangPreference.setSummary(getString(R.string.hebrew));
                break;

            case "en":
                mLangPreference.setSummary(getString(R.string.english));
                break;
        }
    }
}