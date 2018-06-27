package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.utils.Utils;

public class MyPregnancyContext extends Fragment{

    private static String CLASSNAME = "MOM plus";
    PregnancyWeeklyProgressAdapter weeklyProgressAdapter;
    ArrayList<PregnancyWeeklyProgressModel> weeklyProgressModelArrayList = new ArrayList<>();

    @BindView(R.id.todays_progress_intro)
    TextView todaysProgressTitle;
    @BindView(R.id.current_week_tv)
    TextView currentWeekTv;
    @BindView(R.id.weekly_progress_recycler)
    RecyclerView weeklyProgressRecycler;
    @BindView(R.id.todays_day)
    TextView todaysDay;

    PregnancyWeeklyProgressModel todaysModelItem;
    String currentWeek = "2";
    String currentDay = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypregnancy_list,container,false);
        ButterKnife.bind(this, rootView);

        weeklyProgressRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        weeklyProgressAdapter = new PregnancyWeeklyProgressAdapter(getActivity());
        weeklyProgressRecycler.setAdapter(weeklyProgressAdapter);

        try {
            JSONObject job = new JSONObject(new Utils().loadJSONFromAsset(getActivity()));
            JSONArray weeksArray = job.getJSONArray("weeks");

            //Iterate through all weeks
            for (int week = 0; week < weeksArray.length(); week++) {
                //Check for current week we are in
                //TODO check what week we are in
                JSONObject singleWeek = new JSONObject(weeksArray.get(week).toString());
                Log.v("Single week", singleWeek.toString());
                if (singleWeek.getString("week").equals(currentWeek)) {
                    JSONArray thisWeek = singleWeek.getJSONArray("data");
                    for (int day = 0; day < thisWeek.length(); day++) {
                        PregnancyWeeklyProgressModel oneItem = new Gson().fromJson(thisWeek.get(day).toString(), PregnancyWeeklyProgressModel.class);
                        Log.v("oneItem", oneItem.toString());
                        if (oneItem.getDay().equals(currentDay)) {
                            todaysModelItem = oneItem;
                            //TODO check if day doesn't exist
                        } else {
                            weeklyProgressModelArrayList.add(oneItem);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (todaysModelItem != null) {
            todaysDay.setText(getString(R.string.todays_day_placeholder,todaysModelItem.getDay()));
            todaysProgressTitle.setText(Html.fromHtml(getString(R.string.todays_progress, todaysModelItem.getTitle(), todaysModelItem.getIntro())));
            currentWeekTv.setText(Html.fromHtml(getString(R.string.week_title, currentWeek)));
        } else {
            //TODO set some empty state data
        }

        weeklyProgressAdapter.addPregnancyProgress(weeklyProgressModelArrayList);

        return rootView;
    }

    void sortWeeklyProgressUpToCurrentWeek (String week) {

    }

}
