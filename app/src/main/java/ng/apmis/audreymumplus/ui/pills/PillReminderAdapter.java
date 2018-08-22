package ng.apmis.audreymumplus.ui.pills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 8/21/2018.
 */

public class PillReminderAdapter extends RecyclerView.Adapter<PillReminderAdapter.PillReminderViewHolder> {

    private Context mContext;
    private List<PillModel> pillModelArrayList;
    private PopupClickListener mPopupClickListener;

    PillReminderAdapter(Context context, PopupClickListener mPopupClickListener) {
        mContext = context;
        pillModelArrayList = new ArrayList<>();
        this.mPopupClickListener = mPopupClickListener;
    }

    void addPillReminders (List<PillModel> pillModel) {
        if (pillModelArrayList != null) {
            pillModelArrayList = new ArrayList<>();
        }
        pillModelArrayList = pillModel;
        notifyDataSetChanged();
    }

    @Override
    public PillReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PillReminderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pill_reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PillReminderViewHolder holder, int position) {
        PillModel pillModel = pillModelArrayList.get(position);
        holder.pillNameTv.setText(pillModel.getPillName());

        StringBuilder sb = new StringBuilder();

        for (long x: pillModel.getPillTimes()) {

            DateTime time = new DateTime(x);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a");
            sb.append(fmt.print(time));
            sb.append("\t");
        }

        holder.pillInstructionsTv.setText(pillModel.getInstruction() + "\n" + Html.fromHtml("<b>Times: </b>")+ sb);
    }

    @Override
    public int getItemCount() {
        return pillModelArrayList.size();
    }

    class PillReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView pillNameTv;
        TextView pillInstructionsTv;
        ImageView popupOption;

        PillReminderViewHolder(View itemView) {
            super(itemView);
            pillNameTv = itemView.findViewById(R.id.pill_name);
            pillInstructionsTv = itemView.findViewById(R.id.pill_intructions);
            popupOption = itemView.findViewById(R.id.popup_option);
            popupOption.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mPopupClickListener.onPopupClick(view, pillModelArrayList.get(getAdapterPosition()));
        }
    }

    interface PopupClickListener {
        void onPopupClick (View v, PillModel pillModel);
    }
}
