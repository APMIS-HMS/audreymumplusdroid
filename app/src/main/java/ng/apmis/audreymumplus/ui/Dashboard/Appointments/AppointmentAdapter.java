package ng.apmis.audreymumplus.ui.Dashboard.Appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class AppointmentAdapter extends BaseAdapter {
    List<AppointmentModel> appointmentModels;
    Context mCon;
    public AppointmentAdapter(Context context, List<AppointmentModel>appoint){
        mCon = context;
        appointmentModels = appoint;
    }

    @Override
    public int getCount() {
        return appointmentModels.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mCon).
                    inflate(R.layout.my_appointments, parent, false);
        }

        // get current item to be displayed
        AppointmentModel appointmentModel = (AppointmentModel) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemDescription = convertView.findViewById(R.id.month);

        //sets the text for item name and item description from the current item object
        textViewItemDescription.setText(appointmentModel.getMonth());

        TextView appointmentText = convertView.findViewById(R.id.appointment_text);

        appointmentText.setText(appointmentModel.getEvent());

        TextView appointmentTime = convertView.findViewById(R.id.time1);
        appointmentTime.setText(appointmentModel.getTime());

        TextView appointmentDate = convertView.findViewById(R.id.date1);
        appointmentDate.setText(appointmentModel.getDate());

        TextView appointmentDay = convertView.findViewById(R.id.day1);
        appointmentDay.setText(appointmentModel.getDay());

        TextView appointmentEvent2 = convertView.findViewById(R.id.appointment_text2);
        appointmentEvent2.setText(appointmentModel.getEvent2());

        TextView appointmentTime2 = convertView.findViewById(R.id.time2);
        appointmentTime2.setText(appointmentModel.getTime2());

        TextView appointmentDate2 = convertView.findViewById(R.id.date2);
        appointmentDate2.setText(appointmentModel.getDate2());

        TextView appointmentDay2 = convertView.findViewById(R.id.day2);
        appointmentDay2.setText(appointmentModel.getDay2());





        // returns the view for the current row
        return convertView;
    }
}
