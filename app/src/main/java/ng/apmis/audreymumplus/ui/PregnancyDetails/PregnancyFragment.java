package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class PregnancyFragment extends Fragment {

    public String currentDay;
    public String currentWeek;
    @BindView(R.id.day_indicator)
    TextView dayIndicator;
    @BindView(R.id.week_indicator)
    TextView weekIndicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pregnancy_details, container, false);
        ButterKnife.bind(this, rootView);
        currentDay = "3";
        currentWeek = "2";

        dayIndicator.setText(getString(R.string.day_indicator, currentDay));
        weekIndicator.setText(getString(R.string.week_indicator, currentWeek));

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), getChildFragmentManager());


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

