package ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.pregnancymodule.journalweekgroup.JournalWeekGroup.JournalWeekPlusKickCount;

/**
 * Created by Thadeus-APMIS on 9/25/2018.
 */

public class JournalWeekGroupAdapter extends BaseAdapter {

    List<JournalWeekPlusKickCount> weekPlusKickCountsList;
    Context mContext;

    JournalWeekGroupAdapter(Context context) {
        mContext = context;
        weekPlusKickCountsList = new ArrayList<>();
    }

    public void setList (List<JournalWeekPlusKickCount> journalWeekPlusKick) {
        if (weekPlusKickCountsList != null) {
            weekPlusKickCountsList = new ArrayList<>();
        }
        weekPlusKickCountsList = journalWeekPlusKick;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return weekPlusKickCountsList == null ? 0 : weekPlusKickCountsList.size();
    }

    @Override
    public Object getItem(int i) {
        return weekPlusKickCountsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.journal_group_item, viewGroup, false);
        }

        JournalWeekPlusKickCount list = (JournalWeekPlusKickCount) getItem(i);

        TextView week = view.findViewById(R.id.week);

        TextView kicks = view.findViewById(R.id.kicks);

        TextView entryCount = view.findViewById(R.id.entry_count);

        week.setText(list.getWeek());

        kicks.setText(list.getWeekKicks() + " kick(s)");

        entryCount.setText(list.getJournalEntryCount() + " entrie(s)");

        return view;
    }
}
