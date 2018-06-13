package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class PregnancyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pregnancy_details, container, false);
        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), getActivity().getSupportFragmentManager());


        TabLayout tabLayout = rootView.findViewById(R.id.tabview);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "My Pregnancy");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }


}

