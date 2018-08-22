package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.getaudrey.GetAudreyFragment;

public class PregnancyWeeklyProgressFragment extends Fragment{

    PregnancyWeeklyProgressAdapter weeklyProgressAdapter;
    ArrayList<PregnancyWeeklyProgressModel> weeklyProgressModelArrayList;

   /* @BindView(R.id.todays_progress_intro)
    TextView todaysProgressTitle;*/
    @BindView(R.id.weekly_progress_recycler)
    RecyclerView weeklyProgressRecycler;
    /*@BindView(R.id.todays_day)
    TextView todaysDay;
    @BindView(R.id.today_progress_group_card)
    CardView todaysProgressCard;*/

    PregnancyWeeklyProgressModel todaysModelItem;
    String currentWeek;
    String currentDay;

    RequestQueue queue;

    @BindView(R.id.content_view)
    View contentView;
    @BindView(R.id.get_audrey_view)
    View getAudreyView;

    @BindView(R.id.get_audrey_btn)
    Button getAudreyButton;
    @BindView(R.id.current_week_tv)
    TextView currentWeekTv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String edd;

    AppCompatActivity activity;
    String week;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypregnancy_list,container,false);
        ButterKnife.bind(this, rootView);
        weeklyProgressModelArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(getActivity());

        weeklyProgressRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        weeklyProgressAdapter = new PregnancyWeeklyProgressAdapter(activity, getChildFragmentManager());
        weeklyProgressRecycler.setAdapter(weeklyProgressAdapter);

        ((DashboardActivity)getActivity()).getPersonLive().observe(this, person -> {
            if (person != null) {
                week = person.getWeek();
                edd = person.getExpectedDateOfDelivery();
                currentDay = String.valueOf(person.getDay());
                currentWeek = String.valueOf(person.getWeek()).split(" ")[1];
                currentWeekTv.setText(getString(R.string.week_title, person.getWeek()));
            }
            if (edd != null) {
                getWeeklyProgress();
            }
        });


        getAudreyButton.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new GetAudreyFragment())
                    .addToBackStack("preg-frag")
                    .commit();
        });

        return rootView;
    }

    private void getWeeklyProgress() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://audrey-mum.herokuapp.com/weekly-progres", new JSONObject(),
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONArray remoteWeeksArray = response.getJSONArray("data");
//                        Log.v("remoteweekarray", remoteWeeksArray.toString());

                        JSONObject currentWeek = new JSONObject(remoteWeeksArray.get(0).toString());
                        if (currentWeek.getJSONArray("data").length() < 1) {
                            currentWeek = new JSONObject(remoteWeeksArray.get(0).toString());
                        }

                        JSONArray remoteWeekDaysArray = currentWeek.getJSONArray("data");

                        for (int day = 0; day < remoteWeekDaysArray.length(); day++) {
                            PregnancyWeeklyProgressModel oneItem = new Gson().fromJson(remoteWeekDaysArray.get(day).toString(), PregnancyWeeklyProgressModel.class);
                            //Todo uncomment logic when there's a way to fix days in a week according to progress
                          /*  Log.v("oneItem", oneItem.toString());
                            if (oneItem.getDay().equals(currentDay)) {
                                todaysModelItem = oneItem;
                                //TODO check if day doesn't exist
                            } else {
                                weeklyProgressModelArrayList.add(oneItem);
                            }*/
                            weeklyProgressModelArrayList.add(oneItem);

                         /*   if (todaysModelItem != null) {
                                todaysDay.setText(getString(R.string.todays_day_placeholder,todaysModelItem.getDay()));
                                todaysProgressTitle.setText(Html.fromHtml(getString(R.string.todays_progress, todaysModelItem.getTitle(), todaysModelItem.getIntro())));
                            }*/

                            weeklyProgressAdapter.addPregnancyProgress(weeklyProgressModelArrayList);

                           /* todaysProgressCard.setOnClickListener(view -> {
                                Bundle detailBundle = new Bundle();
                                detailBundle.putParcelable("today", todaysModelItem);
                                PregnancyWeeklyProgressDetail pregWeekDetail = new PregnancyWeeklyProgressDetail();
                                pregWeekDetail.setArguments(detailBundle);
                                pregWeekDetail.show(getChildFragmentManager(), "Day Details");
                            });*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (weeklyProgressAdapter.getItemCount() < 1 && edd != null) {
                        Snackbar.make(contentView, "Check Internet and Retry", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Ok", view -> {}).show();
                    }
                    if (weeklyProgressAdapter.getItemCount() < 1 && edd == null){
                        contentView.setVisibility(View.GONE);
                        getAudreyView.setVisibility(View.VISIBLE);
                    }
                    if (weeklyProgressAdapter.getItemCount() > 0 && edd != null) {
                        contentView.setVisibility(View.VISIBLE);
                        getAudreyView.setVisibility(View.GONE);
                    }

                },
                error -> {
                    Log.v("weekly error", String.valueOf(error));
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(contentView, "There was an error", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", view -> {}).show();
                });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        queue.cancelAll(this);
    }

    int checkWeekAndSelectWeekProgress () {
        int wk = Integer.parseInt(week.split(" ")[1]);
        if (wk < 1) {
            wk = 1;
        }
        Log.v("Week integer", String.valueOf(wk));
        return wk - 1;
    }
}
