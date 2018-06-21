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

    MyPregnancyAdapter (Context context) {
        mContext = context;
    }

    public void setAllChats (ArrayList<MyPregnancyModel> pregs) {
        myPregnancyModels = pregs;
        notifyDataSetChanged();
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
                view = LayoutInflater.from(mContext).inflate(R.layout.baby_progress_view, parent, false);
                return new MyPregnancyViewHolder(view);
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.weekly_tips_view, parent, false);
                return new MyPregnancyViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
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
            contentupdate=  itemView.findViewById(R.id.header_text);
            contentBody1 = (TextView) itemView.findViewById(R.id.content1);
            contentBody2 = itemView.findViewById(R.id.content2);

        }
    }

}
