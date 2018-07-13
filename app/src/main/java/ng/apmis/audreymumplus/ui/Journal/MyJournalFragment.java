package ng.apmis.audreymumplus.ui.Journal;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.JournalDetailFragment;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class MyJournalFragment extends Fragment {

    JournalViewModel journalViewModel;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    AppCompatActivity activity;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_journal, container, false);
        ButterKnife.bind(this, rootView);

        ListView listView = rootView.findViewById(R.id.journal);

        JournalAdapter journalAdapter = new JournalAdapter(getActivity());
        listView.setAdapter(journalAdapter);

        JournalFactory journalFactory = InjectorUtils.provideJournalFactory(getActivity());
        journalViewModel = ViewModelProviders.of(getActivity(), journalFactory).get(JournalViewModel.class);

        journalViewModel.getmJournalEntry().observe(getActivity(), journalModels -> {
            if (journalModels != null) {
                journalAdapter.setJournals(journalModels);
            } else {
                Toast.makeText(getActivity(), "No journals found", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            JournalModel clickedJournal = (JournalModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clickedJournal.getMood() , Toast.LENGTH_SHORT).show();

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
        activity = (AppCompatActivity)context;
    }
}
