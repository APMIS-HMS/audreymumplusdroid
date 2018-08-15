package ng.apmis.audreymumplus.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 8/15/2018.
 */

public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new SettingPreferences(), "settings")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(true, "Settings");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);

    }

    @Override
    public void onStop() {
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
        super.onStop();
    }

}
