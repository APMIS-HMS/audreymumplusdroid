package ng.apmis.audreymumplus.ui.pills;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.AlarmBroadcast;
import ng.apmis.audreymumplus.utils.AlarmMangerSingleton;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 8/16/2018.
 */

public class AddPillReminder extends Fragment implements PillTimesAdapter.TimeRemoverListener{

    @BindView(R.id.pill_name)
    EditText pillNameEt;
    @BindView(R.id.qty_per_time)
    EditText quantityPerTimeEt;
    @BindView(R.id.frequency)
    EditText frequency;
    @BindView(R.id.time_unit_picker)
    NumberPicker numberPicker;
    @BindView(R.id.duration_spinner)
    Spinner durationSpinner;
    @BindView(R.id.instruction)
    EditText instructionsEt;
    @BindView(R.id.reminder)
    Switch reminderSwitch;
/*
    @BindView(R.id.repeat)
    Switch repeatSwitch;*/
    @BindView(R.id.save_btn)
    Button saveButton;
    @BindView(R.id.reminder_container)
    View reminderContainer;
    @BindView(R.id.pill_time_recycler)
    RecyclerView pillTimeRecyler;
    static PillTimesAdapter pillTimesAdapter;

    @BindView(R.id.time_container)
    RelativeLayout timeContainer;

    String selectedUnit;
    String selectedDuration;

    DialogFragment dialogfragment;



    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_pill_reminder, container, false);
        ButterKnife.bind(this, rootView);

        pillTimeRecyler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pillTimesAdapter = new PillTimesAdapter(getActivity(), this);
        pillTimeRecyler.setAdapter(pillTimesAdapter);

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedDuration = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedUnit = String.valueOf(newVal);
        });


        reminderSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                reminderContainer.setVisibility(View.VISIBLE);
            } else {
                reminderContainer.setVisibility(View.GONE);
                pillTimesAdapter.removeAllPills();
            }
        });

        timeContainer.setOnClickListener((view -> {
            dialogfragment = new AddPillReminder.DialogTime();
            dialogfragment.show(getActivity().getFragmentManager(), "Select Time");
        }));


        saveButton.setOnClickListener((view -> {
            if (checkFields()) {
                PillModel newPill = new PillModel(pillNameEt.getText().toString(), quantityPerTimeEt.getText().toString(), frequency.getText().toString(), selectedUnit, selectedDuration, instructionsEt.getText().toString(), pillTimesAdapter.pillTime, 0);

                AudreyMumplus.getInstance().diskIO().execute(() -> {

                    long reminderId = InjectorUtils.provideRepository(getActivity()).insertPillReminder(newPill);
                    Intent alarmIntent = new Intent(getActivity(), AlarmBroadcast.class);

                    alarmIntent.setAction("pillreminder");
                    alarmIntent.putExtra("pillreminder", reminderId);


                    AlarmManager alarmManager = new AlarmMangerSingleton(getActivity()).getInstance().getAlarmManager();

                    //Set pending intent for every alarmtime selected
                    for (long x : pillTimesAdapter.pillTime) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date(x));
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) c.getTimeInMillis(), alarmIntent, PendingIntent.FLAG_ONE_SHOT);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    }


                    getActivity().getSupportFragmentManager().popBackStack("ADD_PILL", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                });
            } else {
                Toast.makeText(getActivity(), "Check fields!!!", Toast.LENGTH_SHORT).show();
            }
        }));
        return rootView;
    }

    boolean checkFields () {
        if (TextUtils.isEmpty(pillNameEt.getText().toString())) {
            pillNameEt.setError("Please enter pill name");
            return false;
        }
        if (TextUtils.isEmpty(quantityPerTimeEt.getText().toString())) {
            quantityPerTimeEt.setError("Please enter quantity");
            return false;
        }
        if (TextUtils.isEmpty(frequency.getText().toString())) {
            frequency.setError("Please enter frequency");
            return false;
        }


        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Add Pill");
    }

    @Override
    public void onRemove(int postion) {
        pillTimesAdapter.removePill(postion);
    }

    public static class DialogTime extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {

            Calendar c = Calendar.getInstance();

            c.set(Calendar.HOUR_OF_DAY, i);
            c.set(Calendar.MINUTE, i1);
            c.set(Calendar.SECOND, 0);

            pillTimesAdapter.addPill(c.getTimeInMillis());
            //et.setText(hour + ":" + minutes);
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
