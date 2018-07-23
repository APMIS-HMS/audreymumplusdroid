package ng.apmis.audreymumplus.ui.Appointments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class AppointmentFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.fab2)
    FloatingActionButton fab2;
    List<Appointment> appointmentFromAndroidCalendar;
    Cursor cur;
    AppointmentAdapter appointmentAdapter;

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
        appointmentFromAndroidCalendar = new ArrayList<>();

        ListView listView = rootView.findViewById(R.id.appointment);

        appointmentAdapter = new AppointmentAdapter(getActivity());
        listView.setAdapter(appointmentAdapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Appointment clicked = (Appointment) parent.getItemAtPosition(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(clicked.getID())));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            getActivity().startActivity(intent);
        });

        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        fab2.setOnClickListener((view) -> getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new AddAppointmentFragment())
                .addToBackStack("ADD_NEW")
                .commit());


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                databaseQuery();
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALENDAR}, 9000);
                } else {
                    databaseQuery();
                }
            }


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


        AudreyMumplus.getInstance().diskIO().execute(() -> {

            ArrayList<Appointment> appointments = new ArrayList<>();

            InjectorUtils.provideRepository(getActivity()).getAllAppointments().observe(this, appointments1 -> {
                appointments.addAll(appointments1);

                ContentResolver cr = getActivity().getContentResolver();

                Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
                ContentUris.appendId(builder, new Date().getTime());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 1);
                Date oneMonthAhead = new Date(cal.getTimeInMillis());
                ContentUris.appendId(builder, oneMonthAhead.getTime());

                cur = cr.query(builder.build(),
                        INSTANCE_PROJECTION,
                        null,
                        null,
                        INSTANCE_PROJECTION[3] + " ASC");

                if (cur != null) {
                    while (cur.moveToNext()) {
                        String title = null;
                        long eventID = 0;
                        long beginVal = 0;

                        // Get the field values
                        eventID = cur.getLong(PROJECTION_ID_INDEX);
                        beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);


                        String appointmentTitle = cur.getString(PROJECTION_TITLE_INDEX);
                        String appointmentTime = new SimpleDateFormat("h:mma").format(beginVal);

                        // Do something with the values.
                        String[] monthDate = TextUtils.split(DateFormat.getDateInstance().format(beginVal).toString(), ",");
                        String dayOfWeek = new SimpleDateFormat("EEEE").format(beginVal);


                        Appointment thisAppointment = new Appointment(eventID, appointmentTitle, appointmentTime, monthDate[0], dayOfWeek);

                        for (int app = 0; app < appointments.size(); app++) {

                            if (appointments.get(app).getTitle().equals(thisAppointment.getTitle())) {
                                appointmentFromAndroidCalendar.add(thisAppointment);
                                Log.v("This appointment", thisAppointment.toString());
                            }
                        }

                    }
                }
                if (cur != null) {
                    cur.close();
                }
                getActivity().runOnUiThread(() -> {
                    appointmentAdapter.setAppointmentModels(appointmentFromAndroidCalendar);
                });


            });

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            databaseQuery();
        } else {
            Toast.makeText(getActivity(), "you need to grant permission to view appointments", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }
}
