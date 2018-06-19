package ng.apmis.audreymumplus.ui;

import android.os.Bundle;

import ng.apmis.audreymumplus.ui.Dashboard.SettingFragment;

public class PreferenceActivity extends android.preference.PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();

        //setContentView(R.layout.activity_preference);
    }

   /* @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.header, target);

    }*/
}