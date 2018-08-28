package ng.apmis.audreymumplus.ui.kickcounter;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class KickCounterFragment extends Fragment {

    @BindView(R.id.kick_count)
    TextView kickCountTv;

    @BindView(R.id.kick_progress)
    ProgressBar kickProgress;

    @BindView(R.id.add_kick)
    FloatingActionButton addKick;

    @BindView(R.id.kick_count_list)
    ListView kickCountList;

    KickCounterListAdapter kickCounterListAdapter;

    Handler handler;
    int durationSecondsTracker = 0;

    int kickCounter;

    Person person;

    Runnable runnable;

    long startTime;

    private int mShortAnimationDuration;

    public KickCounterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kick_counter, container, false);
        ButterKnife.bind(this, rootView);


        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        addKick.setAlpha(0f);
        addKick.setVisibility(View.VISIBLE);

        addKick.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);



        ((DashboardActivity) getActivity()).getPersonLive().observe(this, person -> this.person = person);

        kickCounterListAdapter = new KickCounterListAdapter(getActivity());
        kickCountList.setAdapter(kickCounterListAdapter);

        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(getActivity()).getAllKickCount().observe(this, allKicks -> {
                if (allKicks != null) {
                    Collections.reverse(allKicks);
                    kickCounterListAdapter.addKicks(allKicks);
                }
            });
        });

        runnable = runnable();

        addKick.setOnClickListener((view -> {
            if (handler == null) {
                handler = new Handler();
                handler.postDelayed(runnable, 0);
            }

            kickCountTv.setText(String.valueOf(++kickCounter));

        }));

        return rootView;
    }

    @Override
    public void onPause() {
        ((DashboardActivity) getActivity()).kickCounterFabVisibility(true);
        persistKickCounter();
        super.onPause();
    }

    Runnable runnable () {
        return new Runnable() {
            @Override
            public void run() {
                if (++durationSecondsTracker == 60) {
                    //Update adapter only when the count is 60secs
                    startTime = new Date().getTime();
                    persistKickCounter();
                } else {
                    kickProgress.setProgress(durationSecondsTracker);
                    handler.postDelayed(this, 1000);
                    kickCounter = Integer.parseInt(kickCountTv.getText().toString());
                }
            }
        };
    }

    void persistKickCounter() {
        if (handler != null) {
            KickCounterModel kickCounterModel = new KickCounterModel(kickCounter, person.getWeek(), formatDurationTracker(durationSecondsTracker), startTime);
            durationSecondsTracker = 0;
            kickCountTv.setText("0");
            kickProgress.setProgress(0);
            kickCounter = 0;
            handler.removeCallbacks(runnable);
            handler = null;

            //Persist kick in db
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                InjectorUtils.provideRepository(getActivity()).insertKickCount(kickCounterModel);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Kick Counter");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        ((DashboardActivity) getActivity()).kickCounterFabVisibility(false);
    }

    public static String formatDurationTracker(int kickCounter) {
        String returnString = "00:00:";
        if (kickCounter < 10)
            return returnString += "0" + kickCounter;
        return returnString += kickCounter;
    }

}
