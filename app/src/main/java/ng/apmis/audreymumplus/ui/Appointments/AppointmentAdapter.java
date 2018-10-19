package ng.apmis.audreymumplus.ui.Appointments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class AppointmentAdapter extends BaseAdapter {
    private List<Appointment> mAppointmentModels;
    private Context mCon;
    private OptionPopupListener optionPopupListener;

    AppointmentAdapter(Context context, OptionPopupListener optionPopupListener){
        mCon = context;
        mAppointmentModels = new ArrayList<>();
        this.optionPopupListener = optionPopupListener;
    }

    void setAppointmentModels(List<Appointment> appointmentModels) {
        if (appointmentModels != null) {
            mAppointmentModels = new ArrayList<>();
        }
        mAppointmentModels = appointmentModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAppointmentModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppointmentModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mCon).
                    inflate(R.layout.my_appointments, parent, false);
        }

        Appointment appointmentModel = (Appointment) getItem(position);

        ImageView optionImage = convertView.findViewById(R.id.popup_option);
        optionImage.setOnClickListener((view -> {
            optionPopupListener.onPopupOptionPressed(view, appointmentModel);
        }));

        TextView appointmentText = convertView.findViewById(R.id.appointment_text);
        appointmentText.setText(appointmentModel.getTitle());

        String day          = (String) DateFormat.format("dd", appointmentModel.getAppointmentTime()); // 20
        String monthString  = (String) DateFormat.format("MMM", appointmentModel.getAppointmentTime()); // Jun
        String dayOfTheWeek = (String) DateFormat.format("EEEE", appointmentModel.getAppointmentTime());

        long twentyMinute = 1200000;

        Date dt = new Date(appointmentModel.getAppointmentTime() - twentyMinute);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time = sdf.format(dt);

        TextView appointmentTime = convertView.findViewById(R.id.time1);
        appointmentTime.setText(time);

        TextView appointmentDate = convertView.findViewById(R.id.date1);
        appointmentDate.setText(day  + " " + monthString);

        TextView appointmentDay = convertView.findViewById(R.id.day1);
        appointmentDay.setText(dayOfTheWeek);

        ImageView muteImage = convertView.findViewById(R.id.mute_image);
        muteImage.setImageDrawable(appointmentModel.getMuteAlarm() == 0 ? mCon.getResources().getDrawable(R.drawable.ic_unmute_24dp) : mCon.getResources().getDrawable(R.drawable.ic_mute_black_24dp));

        return convertView;
    }

    interface OptionPopupListener {
        void onPopupOptionPressed(View view, Appointment appointment);
    }

}
