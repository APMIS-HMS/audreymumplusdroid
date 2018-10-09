package ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.pregnancymodule.FragmentLifecycle;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalFragment;
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity.globalPerson;

/**
 * Created by Thadeus-APMIS on 9/25/2018.
 */

public class JournalWeekGroup extends Fragment {

    JournalWeekGroupViewModel journalWeekGroupViewModel;
    AppCompatActivity activity;

    JournalWeekGroupAdapter journalGroupAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_journal, container, false);
        ButterKnife.bind(this, rootView);

        rootView.findViewById(R.id.header).setVisibility(View.GONE);


        ListView listView = rootView.findViewById(R.id.journal);

        journalGroupAdapter = new JournalWeekGroupAdapter(getActivity());
        listView.setAdapter(journalGroupAdapter);

        JournalWeekGroupFactory pjgFactory = InjectorUtils.providePregnancyJournalGroupFactory(getActivity(), globalPerson.getWeek());
        journalWeekGroupViewModel = ViewModelProviders.of(this, pjgFactory).get(JournalWeekGroupViewModel.class);

        Observer<List<JournalWeekPlusKickCount>> mJournalWeekPlusKickCount = journalWeekPlusKickCounts -> {
            journalGroupAdapter.setList(journalWeekPlusKickCounts);
        };

        journalWeekGroupViewModel.getmJournalWeekPlusKickCount().observe(this, mJournalWeekPlusKickCount);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            JournalWeekPlusKickCount journalWeekPlusKick = (JournalWeekPlusKickCount) parent.getItemAtPosition(position);

            Bundle bundleToDetails = new Bundle();
            bundleToDetails.putString("week", journalWeekPlusKick.getWeek());

            Fragment journalFragment = new JournalFragment();
            journalFragment.setArguments(bundleToDetails);

            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, journalFragment)
                    .addToBackStack(null)
                    .commit();
        });
        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    static class JournalWeekPlusKickCount {
        String week;
        int journalEntryCount;
        int weekKicks;

        public JournalWeekPlusKickCount(String week, int journalEntryCount, int weekKicks) {
            this.week = week;
            this.journalEntryCount = journalEntryCount;
            this.weekKicks = weekKicks;
        }

        public String getWeek() {
            return week;
        }

        public int getJournalEntryCount() {
            return journalEntryCount;
        }

        public int getWeekKicks() {
            return weekKicks;
        }

        @Override
        public String toString() {
            return "JournalWeekPlusKickCount{" +
                    "week='" + week + '\'' +
                    ", journalEntryCount=" + journalEntryCount +
                    ", weekKicks=" + weekKicks +
                    '}';
        }
    }
}
