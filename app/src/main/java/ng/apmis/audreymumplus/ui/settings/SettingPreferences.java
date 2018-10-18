package ng.apmis.audreymumplus.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;


public class SettingPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PASSWORD = "password";

    EditTextPreference editTextPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(SharedPreferencesManager.PREF_NAME);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.settings);

        //TODO if firsttime launch
        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));

        editTextPreference = (EditTextPreference) findPreference(KEY_PASSWORD);

        editTextPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("password")) {

           // statePreference.setSummary(sharedPreferences.getString("states_list", ""));
        }
    }
}
