package ng.apmis.audreymumplus.ui.kickcounter;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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

import static ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity.globalPerson;

public class KickCounterFragment extends Fragment {

    @BindView(R.id.kick_count)
    TextView kickCountTv;

    @BindView(R.id.kick_progress)
    ProgressBar kickProgress;

    @BindView(R.id.add_kick)
    FloatingActionButton addKick;

    Handler handler;
    int durationSecondsTracker = 0;

    int kickCounter;

    Runnable runnable;

    long startTime;

    private int mShortAnimationDuration;

    @BindView(R.id.kick_count_val)
    TextView kickCountVal;

    @BindView(R.id.kick_week_val)
    TextView kickWeekVal;

    @BindView(R.id.kick_date_val)
    TextView kickDateVal;

    @BindView(R.id.date)
    TextView date;

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
        date.setText(DateUtils.getRelativeDateTimeString(getActivity(), Calendar.getInstance().getTimeInMillis(), 0, 0, DateUtils.FORMAT_ABBREV_MONTH));

        kickWeekVal.setText(globalPerson.getWeek().split(" ")[1]);

        kickDateVal.setText(DateUtils.getRelativeDateTimeString(getActivity(), Calendar.getInstance().getTimeInMillis(), 0, 0, DateUtils.FORMAT_NO_MONTH_DAY).toString().split(",")[0]);

        AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(getActivity()).getKickCountPerDay(globalPerson.getDay()).observe(this, kickCounter -> kickCountVal.setText(kickCounter != null ? String.valueOf(kickCounter) : "0")));

        runnable = runnable();

        addKick.setOnClickListener((view -> {
            if (handler == null) {
                handler = new Handler();
                handler.postDelayed(runnable, 0);
            }

            kickCountTv.setText(String.valueOf(++kickCounter));
            int incrementCount = Integer.parseInt(kickCountVal.getText().toString()) + 1;
            kickCountVal.setText(String.valueOf(incrementCount));

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
            KickCounterModel kickCounterModel = new KickCounterModel(kickCounter, globalPerson.getWeek(), formatDurationTracker(durationSecondsTracker), startTime, globalPerson.getDay());
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
