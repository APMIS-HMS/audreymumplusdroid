package ng.apmis.audreymumplus.ui.Journal;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class MyJournalFragment extends Fragment {

    JournalViewModel journalViewModel;
    @BindView(R.id.fab)
    FloatingActionButton fab;

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
            JournalModel clicked = (JournalModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getMood() , Toast.LENGTH_SHORT).show();
        });

        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        fab.setOnClickListener((view) -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new AddJournalFragment())
                    .addToBackStack("ADD_NEW")
                    .commit();
        });


        return rootView;
    }


}
