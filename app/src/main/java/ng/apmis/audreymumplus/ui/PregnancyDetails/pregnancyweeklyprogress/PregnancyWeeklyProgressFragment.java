package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyweeklyprogress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apmis.audrey.DateCalculator;
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

    @BindView(R.id.todays_progress_intro)
    TextView todaysProgressTitle;
    @BindView(R.id.current_week_tv)
    TextView currentWeekTv;
    @BindView(R.id.weekly_progress_recycler)
    RecyclerView weeklyProgressRecycler;
    @BindView(R.id.todays_day)
    TextView todaysDay;
    @BindView(R.id.today_progress_group_card)
    CardView todaysProgressCard;

    PregnancyWeeklyProgressModel todaysModelItem;
    String currentWeek;
    String currentDay;

    RequestQueue queue;

    @BindView(R.id.content_view)
    View contentView;
    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.get_audrey_btn)
    Button getAudreyButton;

    private String edd;
    ProgressDialog pd;

    AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypregnancy_list,container,false);
        ButterKnife.bind(this, rootView);
        weeklyProgressModelArrayList = new ArrayList<>();
        queue = Volley.newRequestQueue(getActivity());
        pd = new ProgressDialog(this.getContext());
        pd.setIndeterminate(true);
        pd.setMessage("Please wait");
        pd.show();

        ((DashboardActivity)getActivity()).getPersonLive().observe(this, person -> {
            edd = person != null ? person.getExpectedDateOfDelivery() : null;
            currentDay = String.valueOf(person.getDay());
            currentWeek = String.valueOf(person.getWeek()).split(" ")[1];
            Log.v("person week prog", person.toString());
        });

        weeklyProgressRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        weeklyProgressAdapter = new PregnancyWeeklyProgressAdapter(activity, getChildFragmentManager());
        weeklyProgressRecycler.setAdapter(weeklyProgressAdapter);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://audrey-mum.herokuapp.com/weekly-progres", new JSONObject(),
                response -> {
                    try {
                        JSONArray remoteWeeksArray = response.getJSONArray("data");
                        Log.v("remoteweekarray", remoteWeeksArray.toString());

                        JSONObject weekOne = new JSONObject(remoteWeeksArray.get(0).toString());
                        JSONArray remoteWeekDaysArray = weekOne.getJSONArray("data");

                        for (int day = 0; day < remoteWeekDaysArray.length(); day++) {
                            PregnancyWeeklyProgressModel oneItem = new Gson().fromJson(remoteWeekDaysArray.get(day).toString(), PregnancyWeeklyProgressModel.class);
                            Log.v("oneItem", oneItem.toString());
                            if (oneItem.getDay().equals(currentDay)) {
                                todaysModelItem = oneItem;
                                //TODO check if day doesn't exist
                            } else {
                                weeklyProgressModelArrayList.add(oneItem);
                            }

                            if (todaysModelItem != null) {
                                todaysDay.setText(getString(R.string.todays_day_placeholder,todaysModelItem.getDay()));
                                todaysProgressTitle.setText(Html.fromHtml(getString(R.string.todays_progress, todaysModelItem.getTitle(), todaysModelItem.getIntro())));
                                currentWeekTv.setText(Html.fromHtml(getString(R.string.week_title, currentWeek)));
                            }

                            weeklyProgressAdapter.addPregnancyProgress(weeklyProgressModelArrayList);

                            todaysProgressCard.setOnClickListener(view -> {
                                Bundle detailBundle = new Bundle();
                                detailBundle.putParcelable("today", todaysModelItem);
                                PregnancyWeeklyProgressDetail pregWeekDetail = new PregnancyWeeklyProgressDetail();
                                pregWeekDetail.setArguments(detailBundle);
                                pregWeekDetail.show(getChildFragmentManager(), "Day Details");
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    pd.dismiss();

                    if (weeklyProgressAdapter.getItemCount() < 1) {
                        if (edd != null) {
                            Snackbar.make(contentView, "Check Internet and Retry", Snackbar.LENGTH_SHORT)
                                    .setAction("Ok", view -> {}).show();
                        }
                    } else {
                        if (edd != null) {
                            contentView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        } else {
                            contentView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    }

                },
                error -> {
                    Log.v("weekly error", String.valueOf(error));
                    pd.dismiss();
                    Snackbar.make(contentView, "Check Internet and Retry", Snackbar.LENGTH_SHORT)
                            .setAction("Ok", view -> {}).show();
                });
        queue.add(jsonObjectRequest);

        getAudreyButton.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new GetAudreyFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }
}
