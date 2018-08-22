package ng.apmis.audreymumplus.ui.Faq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class FaqFragment  extends Fragment {

    List<FaqModel> faqModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_faq, container, false);


        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);
        Toolbar toolbar = rootView.findViewById(R.id.my_toolbar);


        ListView listView = rootView.findViewById(R.id.faq);
        listView.setDivider(null);

        faqModelList.add(new FaqModel("I feel swelling in my feet as my pregnancy advances"));
        faqModelList.add(new FaqModel("I can’t feel my face, what’s wrong ?"));
        faqModelList.add(new FaqModel("I can’t feel my face, what’s wrong ?"));
        faqModelList.add(new FaqModel("I can’t feel my face, what’s wrong ?"));



        FaqAdapter faqAdapter = new FaqAdapter(getActivity(), faqModelList);
        listView.setAdapter(faqAdapter);

//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            FaqModel clicked = (FaqModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getTitleText(), Toast.LENGTH_SHORT).show();
        });

        /*fab2.setOnClickListener((view) -> {


         *//*
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new AddJournalFragment())
                    .addToBackStack("ADD_NEW")
                    .commit();*//*
        });
*/
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "FAQs");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }


}
