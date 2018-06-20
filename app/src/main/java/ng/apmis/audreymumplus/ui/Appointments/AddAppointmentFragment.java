package ng.apmis.audreymumplus.ui.Appointments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/20/2018.
 */

public class AddAppointmentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(true, "Add Appointment");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

}
