package ng.apmis.audreymumplus.ui.Dashboard;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import ng.apmis.audreymumplus.R;


public class SettingFragment extends PreferenceFragment {

    public static final String KEY_PASSWORD = "password";
    public static final  String DELETE_KEY = "delete";
    public static final String REMINDER_KEY = "updateReminderSetting";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("settingsPreference");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.settings);


    }

}
