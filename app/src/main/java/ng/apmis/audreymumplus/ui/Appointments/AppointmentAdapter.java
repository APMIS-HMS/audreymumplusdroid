package ng.apmis.audreymumplus.ui.Appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class AppointmentAdapter extends BaseAdapter {
    List<Appointment> appointmentModels;
    Context mCon;
    public AppointmentAdapter(Context context, List<Appointment>appoint){
        mCon = context;
        appointmentModels = appoint;
    }

    public void setAppointmentModels(List<Appointment> appointmentModels) {
        this.appointmentModels = appointmentModels;
        notifyDataSetChanged();
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
        Appointment appointmentModel = (Appointment) getItem(position);

        //sets the text for item name and item description from the current item object
     //   textViewItemDescription.setText(appointmentModel.getT);

        TextView appointmentText = convertView.findViewById(R.id.appointment_text);

        appointmentText.setText(appointmentModel.getTitle());

        TextView appointmentTime = convertView.findViewById(R.id.time1);
        appointmentTime.setText(appointmentModel.getStartTime());

        TextView appointmentDate = convertView.findViewById(R.id.date1);
        appointmentDate.setText(appointmentModel.getDateMonth());

        TextView appointmentDay = convertView.findViewById(R.id.day1);
        appointmentDay.setText(appointmentModel.getDayOfWeek());


        // returns the view for the current row
        return convertView;
    }
}
