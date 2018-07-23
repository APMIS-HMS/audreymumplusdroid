package ng.apmis.audreymumplus.ui.Appointments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;

/**
 * Created by Thadeus-APMIS on 6/20/2018.
 */

public class AddAppointmentFragment extends Fragment {

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
    TextInputEditText appointmentTime;


    DialogFragment dialogfragment;

    Appointment thisAppointment;


    int mYear, mMonth, mDate = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment, container, false);
        ButterKnife.bind(this, rootView);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            mYear = year;
            mMonth = month;
            mDate = dayOfMonth;
            Toast.makeText(getActivity(), "" + year + " " + month + " " + dayOfMonth, Toast.LENGTH_SHORT).show();
        });

        saveAppointment.setOnClickListener((view) -> {
            if (checkFields()) {
                insertCalendarEvent();
            }
        });

        appointmentTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                dialogfragment = new DialogTime();
                dialogfragment.show(getActivity().getFragmentManager(), "Select Time");
                return true;
            }
            return false;
        });


        return rootView;
    }

    boolean checkFields () {
        if (mYear == 0 || mMonth == 0 || mDate == 0) {
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
        if (appointmentTime.getText().toString().equals("")) {
            appointmentTime.setError("Select time");
            return false;
        }


        return true;
    }

    public void insertCalendarEvent () {
        String time[] = TextUtils.split(appointmentTime.getText().toString(), ":");
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(mYear, mMonth, mDate, parseInt(time[0]), parseInt(time[1]));
        Calendar endTime = Calendar.getInstance();
        endTime.set(mYear, mMonth, mDate, parseInt(time[0]) + 1, parseInt(time[1]));
        thisAppointment = new Appointment(appointmentTitle.getText().toString(),"","","");
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, appointmentTitle.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, appointmentDetails.getText().toString())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, locationAddress.getText().toString())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(true, "Add Appointment");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Appointments");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    public static class DialogTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
            TextInputEditText et = getActivity().findViewById(R.id.appointment_time);
            String hour = i < 10 ? "0" + i : i+"";
            String minutes = i1 < 10 ? "0" + i1 : i1+"";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(getActivity()).saveAppointment(thisAppointment));
            getActivity().getSupportFragmentManager().popBackStack("ADD_NEW", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getActivity().onBackPressed();
        }
    }
}
