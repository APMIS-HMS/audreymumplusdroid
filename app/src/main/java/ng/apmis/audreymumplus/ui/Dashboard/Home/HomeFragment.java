package ng.apmis.audreymumplus.ui.Dashboard.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.ModuleAdapter;
import ng.apmis.audreymumplus.ui.Dashboard.ModuleModel;

public class HomeFragment extends android.support.v4.app.Fragment {

    /*@BindView(R.id.list_items)
    GridView gridItems;
*/    private static final String CLASSNAME = "HOME";

    List<ModuleModel> items = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

      //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);

        GridView gridView = rootView.findViewById(R.id.grid);

        items.add(new ModuleModel("My Pregnancy",R.drawable.ic_my_pregnancy));
        items.add(new ModuleModel("My Appointments", R.drawable.ic_my_appointments));
        items.add(new ModuleModel("Chatrooms",R.drawable.ic_chat_tab_icon));
        items.add(new ModuleModel("FAQs", R.drawable.ic_faq_icon));

        ModuleAdapter moduleAdapter = new ModuleAdapter(getActivity(), items);

        gridView.setAdapter(moduleAdapter);
//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            ModuleModel clicked = (ModuleModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getTitle() , Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

}
