package ng.apmis.audreymumplus.ui.pills;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.AlarmBroadcast;
import ng.apmis.audreymumplus.utils.AlarmMangerSingleton;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 8/20/2018.
 */

public class PillReminderFragment extends Fragment implements PillReminderAdapter.PopupClickListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.pill_recycler)
    RecyclerView pillRecycler;
    @BindView(R.id.add_pill_fab)
    FloatingActionButton addPill;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    PillReminderAdapter pillReminderAdapter;
    PillModel selectedPill;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pill_reminder, container, false);
        ButterKnife.bind(this, rootView);

        pillRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(getActivity()).getAllPills().observe(this, pills -> {
                pillReminderAdapter = new PillReminderAdapter(getActivity(), this);
                pillRecycler.setAdapter(pillReminderAdapter);

                if (pills != null) {
                    if (pills.size() > 0) {
                        pillReminderAdapter.addPillReminders(pills);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            });
        });

        addPill.setOnClickListener((view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddPillReminder())
                    .addToBackStack("ADD_PILL")
                    .commit();
        }));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Pill Reminder");
    }

    @Override
    public void onPopupClick(View v, PillModel pillModel) {
        selectedPill = pillModel;
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_actions, popup.getMenu());

        popup.getMenu().findItem(R.id.mute).setTitle(pillModel.getMuteReminder() == 0 ? "Mute" : "Unmute");
        popup.getMenu().findItem(R.id.details).setVisible(false);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mute:
                muteUnmuteReminder(selectedPill, selectedPill.getMuteReminder() == 0);
                return true;
            case R.id.delete:
                deleteReminder(selectedPill);
                return true;
            default:
                return false;
        }
    }

    void deleteReminder(PillModel pillModel) {
        AudreyMumplus.getInstance().diskIO().execute(() -> {

            //Mute alarms already set
            muteUnmuteReminder(pillModel, true);

            //Delete appointment from the list
            InjectorUtils.provideRepository(getActivity()).deletePillReminder(pillModel);
        });
    }

    void muteUnmuteReminder(PillModel pillModel, boolean muteReminder) {
        AudreyMumplus.getInstance().diskIO().execute(() -> {

            //Build alarm intent same as was created and mute them
            for (long x : pillModel.getPillTimes()) {
                Intent alarmIntent = new Intent(getActivity(), AlarmBroadcast.class);
                alarmIntent.setAction("pillreminder");
                alarmIntent.putExtra("pillreminder", (int) x);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) x, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

                if (muteReminder) {
                    //Cancel pending intent
                    new AlarmMangerSingleton(getActivity()).getInstance().getAlarmManager().cancel(pendingIntent);
                    pillModel.setMuteReminder(1);
                } else {
                    //Initiate pending intent
                    new AlarmMangerSingleton(getActivity()).getInstance().getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, x, AlarmManager.INTERVAL_DAY, pendingIntent);
                    pillModel.setMuteReminder(0);
                }
                InjectorUtils.provideRepository(getActivity()).updatePillReminder(pillModel);
            }

        });
    }

}
