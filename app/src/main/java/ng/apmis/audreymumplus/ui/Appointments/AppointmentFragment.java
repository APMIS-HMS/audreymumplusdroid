package ng.apmis.audreymumplus.ui.Appointments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.AlarmBroadcast;
import ng.apmis.audreymumplus.utils.AlarmMangerSingleton;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class AppointmentFragment extends Fragment implements AppointmentAdapter.OptionPopupListener, PopupMenu.OnMenuItemClickListener{

    @BindView(R.id.fab2)
    FloatingActionButton fab2;
    List<Appointment> appointmentFromAndroidCalendar;
    AppointmentAdapter appointmentAdapter;
    Appointment selectedAppointment;

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
        } else {
            getAppointmentsFromDb();
        }
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

        appointmentAdapter = new AppointmentAdapter(getActivity(), this);
        listView.setAdapter(appointmentAdapter);

        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        fab2.setOnClickListener((view) -> getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new AddAppointmentFragment())
                .addToBackStack("ADD_NEW")
                .commit());

        return rootView;

    }

    public void getAppointmentsFromDb() {
        AudreyMumplus.getInstance().diskIO().execute(() -> {
            InjectorUtils.provideRepository(getActivity()).getAllAppointments().observe(this, appointments1 -> {
                Collections.reverse(appointments1);
                appointmentAdapter.setAppointmentModels(appointments1);
            });

        });

    }

    @Override
    public void onPopupOptionPressed(View v, Appointment appointment) {
        selectedAppointment = appointment;
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_actions, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mute:

                return true;
            case R.id.details:
                Toast.makeText(getActivity(), "Details", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                deleteAppointment(selectedAppointment);
                return true;
            default:
                return false;
        }
    }

    void deleteAppointment (Appointment appointment) {
        AudreyMumplus.getInstance().diskIO().execute(()-> {

           //Build alarm intent same as was created
            Intent alarmIntent = new Intent(getActivity(), AlarmBroadcast.class);
            alarmIntent.setAction("appointment");
            alarmIntent.putExtra("appointment", appointment.get_id());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) appointment.get_id(), alarmIntent, PendingIntent.FLAG_ONE_SHOT);

            //Cancel pending intent
            new AlarmMangerSingleton(getActivity()).getInstance().getAlarmManager().cancel(pendingIntent);

            //Delete appointment from the list
            InjectorUtils.provideRepository(getActivity()).deleteAppointment(appointment);
        });
    }
}
