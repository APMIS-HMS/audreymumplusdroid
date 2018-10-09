package ng.apmis.audreymumplus.ui.kickcounter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 8/22/2018.
 */

public class KickCounterListAdapter extends BaseAdapter {

    private List<KickCounterModel> data;
    private Context mContext;

    public KickCounterListAdapter(Context context){
        mContext = context;
        data = new ArrayList<>();
    }

    public void addKicks (List<KickCounterModel> kickCounterModel) {
        data = kickCounterModel;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.kick_list_item, viewGroup, false);
        }

        KickCounterModel kickCount = data.get(i);

        TextView dateTv = view.findViewById(R.id.date_header);
        TextView weekTv = view.findViewById(R.id.week_header);
        TextView durationTv = view.findViewById(R.id.duration_header);
        TextView kicksTv = view.findViewById(R.id.kick_header);

        String time = DateUtils.formatDateTime(mContext, new Date().getTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        dateTv.setText(time);
        weekTv.setText(kickCount.getWeek().split(" ")[1]);
        durationTv.setText(kickCount.getDuration());
        Log.v("kicks count", String.valueOf(kickCount.getKicks()));
        kicksTv.setText(kickCount.getKicks() != 0 ? String.valueOf(kickCount.getKicks()) : "0");

        return view;
    }
}
