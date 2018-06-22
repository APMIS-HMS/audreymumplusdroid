package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import ng.apmis.audreymumplus.ui.Journal.AddJournalFragment;
import ng.apmis.audreymumplus.ui.Journal.JournalAdapter;
import ng.apmis.audreymumplus.ui.Journal.JournalFactory;
import ng.apmis.audreymumplus.ui.Journal.JournalViewModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;


public class PregnancyJournalFragment extends Fragment {

    JournalViewModel journalViewModel;
    List<PregnancyModel> pregnancyModelList = new ArrayList<>();
    @BindView(R.id.fab3)
    FloatingActionButton fab3;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pregnancy_journal, container, false);


        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);


        ListView listView = rootView.findViewById(R.id.list_pregjournal);

        JournalAdapter journalAdapter = new JournalAdapter(getActivity(), new ArrayList<>());
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
            PregnancyModel clicked = (PregnancyModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getEachDay() , Toast.LENGTH_SHORT).show();
        });

        fab3.setOnClickListener((view) -> {

            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new AddJournalFragment())
                    .addToBackStack("ADD_NEW")
                    .commit();
        });

        return rootView;
    }

}
