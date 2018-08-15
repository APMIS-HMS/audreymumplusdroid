package ng.apmis.audreymumplus.ui.pregnancymodule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class PregnancyFragment extends Fragment {

    @BindView(R.id.day_indicator)
    TextView dayIndicator;
    @BindView(R.id.week_indicator)
    TextView weekIndicator;
    @BindView(R.id.datedelivery)
    TextView deliveryDate;

    AppCompatActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pregnancy, container, false);
        ButterKnife.bind(this, rootView);

        ((DashboardActivity)getActivity()).getPersonLive().observe(this, person -> {
            if (person != null) {
                String[] week = String.valueOf(person.getWeek()).split(" ");
                weekIndicator.setText(getString(R.string.week_indicator, week[1]));
                dayIndicator.setText(getString(R.string.day_indicator, String.valueOf(person.getDay())));

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                String edd = !TextUtils.isEmpty(person.getExpectedDateOfDelivery()) ? person.getExpectedDateOfDelivery() : null;
                if (edd != null) {
                    try {
                        date = inputFormat.parse(person.getExpectedDateOfDelivery());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                deliveryDate.setText(edd != null ? getString(R.string.expectedDateOfDelivery, DateUtils.getRelativeTimeSpanString(date.getTime())) : getString(R.string.expectedDateOfDelivery, "Not set"));
            }

        });

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        PregnancyFragmentCategoryAdapter adapter = new PregnancyFragmentCategoryAdapter(getActivity(), getChildFragmentManager());

        TabLayout tabLayout = rootView.findViewById(R.id.tabview);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (getString(R.string.pregJournal).equals(tab.getText()))
                    ((DashboardActivity)getActivity()).fabVisibility(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (getString(R.string.pregJournal).equals(tab.getText()))
                    ((DashboardActivity)getActivity()).fabVisibility(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(false, "My Pregnancy");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

}

