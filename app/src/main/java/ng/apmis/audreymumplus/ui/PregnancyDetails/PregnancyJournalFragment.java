package ng.apmis.audreymumplus.ui.PregnancyDetails;

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
import ng.apmis.audreymumplus.ui.Dashboard.Journal.AddJournalFragment;


public class PregnancyJournalFragment extends Fragment {
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

        pregnancyModelList.add(new PregnancyModel("Day 1"));
        pregnancyModelList.add(new PregnancyModel("Day 2"));
        pregnancyModelList.add(new PregnancyModel("Day 3"));
        pregnancyModelList.add(new PregnancyModel("Day 4"));
        pregnancyModelList.add(new PregnancyModel("Day 5"));
        /*journalModelList.add(new JournalModel("My week 36 Journal"));
        journalModelList.add(new JournalModel("My week 40 Journal"));
*/
        PregnancyDetailsAdapter pregnancyDetailsAdapter = new PregnancyDetailsAdapter(getActivity(),pregnancyModelList);
        listView.setAdapter(pregnancyDetailsAdapter);

//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);

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
