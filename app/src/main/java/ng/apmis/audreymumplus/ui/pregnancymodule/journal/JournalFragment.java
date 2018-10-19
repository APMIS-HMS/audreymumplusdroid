package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.Week;

public class JournalFragment extends Fragment {

    JournalViewModel journalViewModel;
    AppCompatActivity activity;

    @BindView(R.id.week_option_spinner)
    Spinner weekOption;

    JournalAdapter journalAdapter;

    List<JournalModel> allJournals;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_journal, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            allJournals = new ArrayList<>();

            ListView listView = rootView.findViewById(R.id.journal);

            journalAdapter = new JournalAdapter(getActivity());
            listView.setAdapter(journalAdapter);
            Log.v("week selected", getArguments().getString("week"));

            JournalFactory journalFactory = InjectorUtils.provideJournalFactory(getActivity(), getArguments().getString("week"));
            journalViewModel = ViewModelProviders.of(this, journalFactory).get(JournalViewModel.class);

            Observer<List<JournalModel>> journalObserver = journalModels -> {
                allJournals.clear();
                if (journalModels != null) {
                    allJournals = journalModels;
                    journalAdapter.setJournals(allJournals);
                } else {
                    Toast.makeText(getActivity(), "No journals found", Toast.LENGTH_SHORT).show();
                }
            };

            journalViewModel.getSortedJournalEntries().observe(this, journalObserver);

            onSpinnerOptionsSelection(weekOption);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                JournalModel clickedJournal = (JournalModel) parent.getItemAtPosition(position);

                Bundle bundleToDetails = new Bundle();
                bundleToDetails.putParcelable("journal", clickedJournal);

                Fragment journalFragment = new JournalDetailFragment();
                journalFragment.setArguments(bundleToDetails);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, journalFragment)
                        .addToBackStack(null)
                        .commit();
            });

            listView.setEmptyView(rootView.findViewById(R.id.empty_view));
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onStop() {
        if (journalViewModel != null)
            journalViewModel.getSortedJournalEntries().removeObservers(this);
        super.onStop();
    }

    @SuppressLint("ClickableViewAccessibility")
    void onSpinnerOptionsSelection(Spinner any) {
        any.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = adapterView.getItemAtPosition(i).toString();
                getWeekModel(selectedString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getWeekModel(String week) {
        if (week.equals(Week.ALL.getWeek())) {
            journalAdapter.setJournals(allJournals);
            return;
        }
        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(activity).getJournalByWeek(week).observe(this, journals -> {
                if (journals != null) {
                    journalAdapter.setSortedJournals(journals);
                }
            });
        });
    }


}
