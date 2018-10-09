package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class JournalAdapter extends BaseAdapter {
    List<JournalModel> modelList;
    Context mCon;
    public JournalAdapter(Context context){
        mCon = context;
        modelList = new ArrayList<>();
    }

    public void setJournals (List<JournalModel> journalModels) {
        if (modelList != null) {
            modelList = new ArrayList<>();
        }
        modelList = journalModels;
        notifyDataSetChanged();
    }

    public void setSortedJournals (List<JournalModel> journals) {
        if (modelList != null) {
            modelList = new ArrayList<>();
        }
        modelList = journals;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
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
                    inflate(R.layout.my_journal, parent, false);
        }

        // get current item to be displayed
        JournalModel journalModel = (JournalModel) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemDescription = convertView.findViewById(R.id.week_day_cravings);

        TextView moodTextView = convertView.findViewById(R.id.mood);

        TextView timeTakenTextView = convertView.findViewById(R.id.time_taken);

        //sets the text for item name and item description from the current item object
        textViewItemDescription.setText(mCon.getString(R.string.journal_day, journalModel.getDay()));

        moodTextView.setText(mCon.getString(R.string.mood, journalModel.getMood()));

        timeTakenTextView.setText(DateUtils.getRelativeDateTimeString(mCon, journalModel.getDate(), 0, 0, DateUtils.FORMAT_SHOW_TIME));

        // returns the view for the current row
        return convertView;
    }
}
