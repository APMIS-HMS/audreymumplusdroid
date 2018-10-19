package ng.apmis.audreymumplus.ui.Appointments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.AlarmBroadcast;
import ng.apmis.audreymumplus.utils.AlarmMangerSingleton;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.InputUtils;

import static java.lang.Integer.parseInt;

/**
 * Created by Thadeus-APMIS on 6/20/2018.
 */

public class AddAppointmentFragment extends Fragment {

    private static final String TAG = AddAppointmentFragment.class.getSimpleName();

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @BindView(R.id.appointment_title)
    TextInputEditText appointmentTitle;

    @BindView(R.id.location_address)
    TextInputEditText locationAddress;

    @BindView(R.id.appointment_details)
    TextInputEditText appointmentDetails;

    @BindView(R.id.save_appointment)
    Button saveAppointment;

    @BindView(R.id.appointment_time)
    TextInputEditText appointmentTimeEdittext;


    DialogFragment dialogfragment;

    Appointment thisAppointment;

    Calendar appointmentTime;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment, container, false);
        ButterKnife.bind(this, rootView);
        appointmentTime = Calendar.getInstance();
        InputUtils.showKeyboard(getActivity(), appointmentTitle);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            appointmentTime.set(Calendar.YEAR, year);
            appointmentTime.set(Calendar.MONTH, month);
            appointmentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Toast.makeText(getActivity(), "" + year + " " + month + " " + dayOfMonth, Toast.LENGTH_SHORT).show();
        });

        saveAppointment.setOnClickListener((view) -> {
            if (checkFields()) {
                try {
                    insertAppointment();
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        appointmentTimeEdittext.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                dialogfragment = new DialogTime();
                dialogfragment.show(getActivity().getFragmentManager(), "Select Time");
                return true;
            }
            return false;
        });

        return rootView;
    }

    boolean checkFields() {
        if (appointmentTime.get(Calendar.MONTH) <= 0) {
            Toast.makeText(getActivity(), "Select date from calendar", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (appointmentTitle.getText().toString().equals("")) {
            appointmentTitle.setError("required");
            return false;
        }
        if (appointmentDetails.getText().toString().equals("")) {
            appointmentDetails.setError("required");
            return false;
        }
        if (appointmentTimeEdittext.getText().toString().equals("")) {
            appointmentTimeEdittext.setError("Select time");
            return false;
        }


        return true;
    }


    /**
     * Saves a scheduled appointment to database
     */
    public void insertAppointment() {
        String time[] = TextUtils.split(appointmentTimeEdittext.getText().toString(), ":");
        appointmentTime.set(Calendar.HOUR_OF_DAY, parseInt(time[0]));
        appointmentTime.set(Calendar.MINUTE, parseInt(time[1]));

        thisAppointment = new Appointment(appointmentTitle.getText().toString(), locationAddress.getText().toString(), appointmentDetails.getText().toString(), appointmentTime.getTimeInMillis(), 0);

        AudreyMumplus.getInstance().diskIO().execute(() -> {

            long _id = InjectorUtils.provideRepository(getActivity()).saveAppointment(thisAppointment);
            thisAppointment.set_id(_id);

            AlarmMangerSingleton.setSingleAppointmentAlarm(getActivity(), thisAppointment);

            getActivity().runOnUiThread(() -> {
                new Handler().postDelayed(() -> {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Success")
                            .setMessage("Appointment set successfully")
                            .setPositiveButton("Ok", (dialogInterface, i) -> getActivity().getSupportFragmentManager().popBackStack("ADD_APPOINTMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE)).show();


                }, 500);
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Add Appointment");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        InputUtils.hideKeyboard();
    }

    public static class DialogTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
            TextInputEditText et = getActivity().findViewById(R.id.appointment_time);

            String hour = i < 10 ? "0" + i : i + "";
            String minutes = i1 < 10 ? "0" + i1 : i1 + "";
            et.setText(hour + ":" + minutes);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), R.style.DialogTheme, this, hourOfDay, minute, false);
        }
    }

}
