package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ng.apmis.audreymumplus.R;

public class MyPregnancyAdapter extends RecyclerView.Adapter<MyPregnancyAdapter.MyPregnancyViewHolder> {

    ArrayList<MyPregnancyModel> myPregnancyModels = new ArrayList<>();
    Context mContext;

    MyPregnancyAdapter (Context context, ArrayList<MyPregnancyModel> pregnancies) {
        mContext = context;
        myPregnancyModels = pregnancies;
    }

    public void addChats (ArrayList<MyPregnancyModel> newPregs) {
        myPregnancyModels.addAll(newPregs);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyPregnancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(mContext).inflate(R.layout.todays_progress_layout_item, parent, false);
                return new MyPregnancyViewHolder(view);
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.weekly_tip_item, parent, false);
                return new MyPregnancyViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO get today's date to determine what shows in the top viewand not pregnancy model
        int today = 3;
        if (myPregnancyModels.get(position).getViewId().equals("0")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyPregnancyViewHolder holder, int position) {
        MyPregnancyModel model = myPregnancyModels.get(position);
        if (model != null) {
            holder.textHeader.setText(model.getBabyProgcontentHead());
            holder.contentupdate.setText(model.getWeeklyHeader());
            holder.contentBody1.setText(model.getWeeklyContent1());
            holder.contentBody2.setText(model.getWeekContent2());
        }
    }

    @Override
    public int getItemCount() {
        return myPregnancyModels.size();
    }

    class MyPregnancyViewHolder extends RecyclerView.ViewHolder {

        TextView textHeader;
        TextView contentupdate;
        TextView contentBody1;
        TextView contentBody2;

        MyPregnancyViewHolder(View itemView) {
            super(itemView);
            textHeader = (TextView) itemView.findViewById(R.id.weeklyupdate);
            contentupdate=  itemView.findViewById(R.id.todays_progress_title);
            contentBody1 = (TextView) itemView.findViewById(R.id.todays_progress_intro);
            contentBody2 = itemView.findViewById(R.id.content2);

        }
    }

}
