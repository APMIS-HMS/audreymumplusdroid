package ng.apmis.audreymumplus.ui.Appointments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        ((DashboardActivity) getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
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

        databaseQuery();

        return rootView;

    }

    public void databaseQuery() {

        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_BEGIN_INDEX = 1;
        final int PROJECTION_TITLE_INDEX = 2;

        final String[] INSTANCE_PROJECTION = new String[]{
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE,          // 2
                CalendarContract.Instances.START_DAY,       //3
                CalendarContract.Instances.DESCRIPTION,     //4
                CalendarContract.Instances.EVENT_LOCATION   //5
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 9000);
        }


        Cursor cur;
        ContentResolver cr = getActivity().getContentResolver();

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
       /* ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);*/

        cur = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                null);

        while (cur.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i("TITLE", "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.v("DATE", "Date: " + formatter.format(calendar.getTime()));
        }
    }


}
