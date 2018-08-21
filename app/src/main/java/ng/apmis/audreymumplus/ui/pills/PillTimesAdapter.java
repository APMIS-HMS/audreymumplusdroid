package ng.apmis.audreymumplus.ui.pills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 8/20/2018.
 */

public class PillTimesAdapter extends RecyclerView.Adapter<PillTimesAdapter.PillTimesViewHolder> {

    public ArrayList<Long> pillTime;
    Context mContext;
    TimeRemoverListener timeRemoverListener;

    public PillTimesAdapter (Context context, TimeRemoverListener timeRemoverListener) {
        mContext = context;
        pillTime = new ArrayList<>();
        this.timeRemoverListener = timeRemoverListener;
    }

    public void addPill (long pillTime) {
        this.pillTime.add(pillTime);
        notifyDataSetChanged();
    }

    public void removePill (int index) {
        pillTime.remove(index);
        notifyDataSetChanged();
    }


    @Override
    public PillTimesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PillTimesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pill_time_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PillTimesViewHolder holder, int position) {

        Date dt = new Date(pillTime.get(position));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time = sdf.format(dt);
        holder.pillTimeTv.setText(time);
    }


    @Override
    public int getItemCount() {
        return pillTime.size();
    }

    public void removeAllPills() {
        pillTime = new ArrayList<>();
        notifyDataSetChanged();
    }

    class PillTimesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView pillTimeTv;
        ImageButton deleteTimeImgBtn;

        PillTimesViewHolder(View itemView) {
            super(itemView);
            pillTimeTv = itemView.findViewById(R.id.pill_time_tv);
            deleteTimeImgBtn = itemView.findViewById(R.id.pill_time_delete);
            deleteTimeImgBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, String.valueOf(pillTime.get(getAdapterPosition())), Toast.LENGTH_SHORT).show();
            timeRemoverListener.onRemove(getAdapterPosition());
        }
    }

    interface TimeRemoverListener {
        void onRemove(int postion);
    }

}
