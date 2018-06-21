package ng.apmis.audreymumplus.ui.Journal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class MyJournalFragment extends Fragment {

    List<JournalModel>journalModelList = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_journal, container, false);


        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);
        Toolbar toolbar = rootView.findViewById(R.id.my_toolbar);


        ListView listView = rootView.findViewById(R.id.journal);

        journalModelList.add(new JournalModel("My week 3 Journal"));
        journalModelList.add(new JournalModel("My week 10 Journal"));
        journalModelList.add(new JournalModel("My week 25 Journal"));
        journalModelList.add(new JournalModel("My week 30 Journal"));
        journalModelList.add(new JournalModel("My week 32 Journal"));
        journalModelList.add(new JournalModel("My week 36 Journal"));
        journalModelList.add(new JournalModel("My week 40 Journal"));

        JournalAdapter journalAdapter = new JournalAdapter(getActivity(),journalModelList);
        listView.setAdapter(journalAdapter);

//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            JournalModel clicked = (JournalModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getTitle() , Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }



}
