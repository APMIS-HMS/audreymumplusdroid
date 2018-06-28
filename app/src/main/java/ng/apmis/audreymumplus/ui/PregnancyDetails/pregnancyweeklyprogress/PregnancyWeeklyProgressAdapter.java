package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 6/27/2018.
 */

public class PregnancyWeeklyProgressAdapter extends RecyclerView.Adapter<PregnancyWeeklyProgressAdapter.PregnancyWeeklyProgressViewHolder> {


    Context mContext;
    ArrayList<PregnancyWeeklyProgressModel> weeklyProgressModels;
    FragmentManager fragmentManager;

    PregnancyWeeklyProgressAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        weeklyProgressModels = new ArrayList<>();
        this.fragmentManager = fragmentManager;
    }

    public void addPregnancyProgress (ArrayList<PregnancyWeeklyProgressModel> weekProgress) {
        if (weeklyProgressModels != null) {
            weeklyProgressModels = new ArrayList<>();
        }
        weeklyProgressModels.addAll(weekProgress);
        notifyDataSetChanged();
    }


    @Override
    public PregnancyWeeklyProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new PregnancyWeeklyProgressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.weekly_tip_item, parent, false));

    }

    @Override
    public void onBindViewHolder(PregnancyWeeklyProgressViewHolder holder, int position) {
        PregnancyWeeklyProgressModel eachDay = weeklyProgressModels.get(position);
        holder.someDaysday.setText(mContext.getString(R.string.todays_day_placeholder, eachDay.getDay()));
        holder.someDaysTitleIntro.setText(Html.fromHtml(mContext.getString(R.string.todays_progress,eachDay.getTitle(), eachDay.getIntro())));
    }

    @Override
    public int getItemCount() {
        return weeklyProgressModels != null ? weeklyProgressModels.size() : 0;
    }

    class PregnancyWeeklyProgressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView someDaysday;
        TextView someDaysTitleIntro;

        public PregnancyWeeklyProgressViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            someDaysday = itemView.findViewById(R.id.some_days_day);
            someDaysTitleIntro = itemView.findViewById(R.id.some_days_title_intro);
        }

        @Override
        public void onClick(View view) {
            Bundle detailBundle = new Bundle();
            detailBundle.putParcelable("today", weeklyProgressModels.get(getAdapterPosition()));
            PregnancyWeeklyProgressDetail pregWeekDetail = new PregnancyWeeklyProgressDetail();
            pregWeekDetail.setArguments(detailBundle);
            pregWeekDetail.show(fragmentManager, "Day Details");

        }
    }


}
