package ng.apmis.audreymumplus.ui.Appointments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class AppointmentFragment extends android.support.v4.app.Fragment {

    List<AppointmentModel> appointmentModelList = new ArrayList<>();
    @BindView(R.id.fab2)
    FloatingActionButton fab2;

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);


        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);
        Toolbar toolbar = rootView.findViewById(R.id.my_toolbar);


        ListView listView = rootView.findViewById(R.id.appointment);

        appointmentModelList.add(new AppointmentModel("November 2018", "Antenatal", "11:30am", "30", "Monday", "Baby Scan", "11:30am", "20", "Saturday"));
        appointmentModelList.add(new AppointmentModel("January 2019", "Registration", "11:30am", "1", "Saturday", "General Checkup", "09:00am", "22", "Tuesday"));
        appointmentModelList.add(new AppointmentModel("February 2019", "Lab Test", "01:40am", "22", "Saturday", "Blood Test", "10:00am", "21", "Wednesday"));
        appointmentModelList.add(new AppointmentModel("November 2018", "Antenatal", "11:30am", "30", "Monday", "Baby Scan", "11:30am", "20", "Saturday"));
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(getActivity(), appointmentModelList);
        listView.setAdapter(appointmentAdapter);

//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AppointmentModel clicked = (AppointmentModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getEvent(), Toast.LENGTH_SHORT).show();
        });

        fab2.setOnClickListener((view) -> getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new AddAppointmentFragment())
                .addToBackStack("ADD_NEW")
                .commit());

        return rootView;

    }
}