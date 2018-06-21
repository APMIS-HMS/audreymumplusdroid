package ng.apmis.audreymumplus.ui.Journal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 5/21/2018.
 */

public class AddJournalFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_journal, container, false);
        ButterKnife.bind(this, rootView);




        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Add Pregnancy");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "My Pregnancy");
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }
}
