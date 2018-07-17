package ng.apmis.audreymumplus.ui.Journal;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.apmis.audrey.DateCalculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.JournalDetailFragment;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.Week;

public class MyJournalFragment extends Fragment {

    JournalViewModel journalViewModel;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    AppCompatActivity activity;

    @BindView(R.id.week_option_spinner)
    Spinner weekOption;

    JournalAdapter journalAdapter;

    List<JournalModel> allJournals;
    String edd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_journal, container, false);
        ButterKnife.bind(this, rootView);
        allJournals = new ArrayList<>();

        ListView listView = rootView.findViewById(R.id.journal);
        ((DashboardActivity)getActivity()).getPersonLive().observe(this, person -> {
            edd = person != null ? person.getExpectedDateOfDelivery() : null;
            getDayWeek();
        });

        journalAdapter = new JournalAdapter(getActivity());
        listView.setAdapter(journalAdapter);

        setupSpinnerAdapters(getActivity().getResources().getStringArray(R.array.week_history), weekOption);
        onSpinnerOptionsSelection(weekOption);

        JournalFactory journalFactory = InjectorUtils.provideJournalFactory(getActivity());
        journalViewModel = ViewModelProviders.of(getActivity(), journalFactory).get(JournalViewModel.class);

        journalViewModel.getmJournalEntry().observe(getActivity(), journalModels -> {
            if (journalModels != null) {
                allJournals = journalModels;
                journalAdapter.setJournals(journalModels);
            } else {
                Toast.makeText(getActivity(), "No journals found", Toast.LENGTH_SHORT).show();
            }
        });



        listView.setOnItemClickListener((parent, view, position, id) -> {
            JournalModel clickedJournal = (JournalModel) parent.getItemAtPosition(position);

            Bundle bundleToDetails = new Bundle();
            bundleToDetails.putParcelable("journal", clickedJournal);

            Fragment journalFragment = new JournalDetailFragment();
            journalFragment.setArguments(bundleToDetails);

            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, journalFragment)
                    .addToBackStack(null)
                    .commit();
        });

        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        fab.setOnClickListener((view) -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new AddJournalFragment())
                    .addToBackStack("ADD_NEW")
                    .commit();
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    private void setupSpinnerAdapters(String[] dataList, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    void onSpinnerOptionsSelection(Spinner any) {
        any.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString)) {
                        getWeekModel(selectedString);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getWeekModel (String week) {
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

    public void getDayWeek () {
        Log.v("edd", edd);
      /*  Date date = new Date(Long.parseLong(edd));
        Log.v("mont, day, year", String.valueOf(date.getMonth() + " " + date.getDay() + " " + date.getYear()));
        DateCalculator dateCalculator = new DateCalculator();
        long weeks = dateCalculator.noOfWeeksSpentAsExpectantMum(1990, 12, 9);
        Log.v("Weeks", String.valueOf(weeks));*/
    }

}
