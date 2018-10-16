package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyweeklyprogress;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.WeeklyProgress;
import ng.apmis.audreymumplus.data.database.WeeklyProgressData;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.getaudrey.GetAudreyFragment;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class PregnancyWeeklyProgressFragment extends Fragment {

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
    @BindView(R.id.current_week_spinner)
    Spinner currentWeekSpinner;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Observer<List<WeeklyProgressData>> weeklyProgressDataObserver;
    PregnancyWeeklyProgressViewModel pregnancyWeeklyProgressViewModel;

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

        if (weeklyProgressAdapter != null) {
            weeklyProgressRecycler.setAdapter(weeklyProgressAdapter);
            if (weeklyProgressAdapter.getItemCount() > 0 && edd != null) {
                contentView.setVisibility(View.VISIBLE);
                getAudreyView.setVisibility(View.GONE);
            }
        }

        ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getContext().getResources().getStringArray(R.array.week_history));
        weekAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        ((DashboardActivity)getActivity()).getPersonLive().observe(this, person -> {
            if (person != null) {
                week = person.getWeek();
                edd = person.getExpectedDateOfDelivery();
                currentDay = String.valueOf(person.getDay());
                currentWeek = String.valueOf(person.getWeek()).split(" ")[1];
                currentWeekSpinner.setAdapter(weekAdapter);
            }

            if (edd != null) {
                getAudreyButton.setVisibility(View.GONE);//remove this button in case weekly data is still loading
                initViewModel();
                getWeeklyProgress();
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        getAudreyButton.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new GetAudreyFragment())
                    .addToBackStack("preg-frag")
                    .commit();
        });

        currentWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshAndObserveWeeklyProgressPerWeek(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void initViewModel(){
        PregnancyWeeklyProgressViewModelFactory factory = InjectorUtils.providePregnancyWeeklyProgressViewModelFactory(getActivity().getApplicationContext());
        pregnancyWeeklyProgressViewModel = ViewModelProviders.of(getActivity(), factory).get(PregnancyWeeklyProgressViewModel.class);

        weeklyProgressDataObserver = weeklyProgressData -> {

            if (weeklyProgressAdapter == null) {

                weeklyProgressAdapter = new PregnancyWeeklyProgressAdapter(getContext(), getFragmentManager());
                weeklyProgressAdapter.addPregnancyProgress(weeklyProgressData);
                weeklyProgressRecycler.setAdapter(weeklyProgressAdapter);

//                if (weeklyProgressAdapter.getItemCount() < 1 && edd != null) {
//                    Snackbar.make(contentView, "Check Internet and Retry", Snackbar.LENGTH_INDEFINITE)
//                            .setAction("Ok", view -> {}).show();
//                }

            } else {
                weeklyProgressAdapter.clear();
                weeklyProgressAdapter.addPregnancyProgress(weeklyProgressData);
                weeklyProgressAdapter.notifyDataSetChanged();
            }

            if (weeklyProgressAdapter.getItemCount() > 0 && edd != null) {
                progressBar.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
                getAudreyView.setVisibility(View.GONE);
            }

        };

        refreshAndObserveWeeklyProgressPerWeek(0);

    }

    private void refreshAndObserveWeeklyProgressPerWeek(int week){
        if (weeklyProgressDataObserver != null) {
            pregnancyWeeklyProgressViewModel.getWeeklyProgressData(0).removeObservers(getActivity());
            pregnancyWeeklyProgressViewModel.getWeeklyProgressData(week).observe(getActivity(), weeklyProgressDataObserver);
        }
    }

    private void getWeeklyProgress() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://audrey-mum.herokuapp.com/weekly-progres", new JSONObject(),
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            List<WeeklyProgress> progress = new ArrayList<>(Arrays.asList(new Gson().fromJson(jsonArray.toString(), WeeklyProgress[].class)));
                            saveWeeklyProgressDataToDb(progress);
                        }
//                        JSONArray remoteWeeksArray = response.getJSONArray("data");
////                        Log.v("remoteweekarray", remoteWeeksArray.toString());
//
//                        JSONObject currentWeek = new JSONObject(remoteWeeksArray.get(1).toString());
//                        if (currentWeek.getJSONArray("data").length() < 1) {
//                            currentWeek = new JSONObject(remoteWeeksArray.get(0).toString());
//                        }
//
//                        JSONArray remoteWeekDaysArray = currentWeek.getJSONArray("data");
//
//                        for (int day = 0; day < remoteWeekDaysArray.length(); day++) {
//                            PregnancyWeeklyProgressModel oneItem = new Gson().fromJson(remoteWeekDaysArray.get(day).toString(), PregnancyWeeklyProgressModel.class);
//                            //Todo uncomment logic when there's a way to fix days in a week according to progress
//                          /*  Log.v("oneItem", oneItem.toString());
//                            if (oneItem.getDay().equals(currentDay)) {
//                                todaysModelItem = oneItem;
//                                //TODO check if day doesn't exist
//                            } else {
//                                weeklyProgressModelArrayList.add(oneItem);
//                            }*/
//                            weeklyProgressModelArrayList.add(oneItem);
//
//                         /*   if (todaysModelItem != null) {
//                                todaysDay.setText(getString(R.string.todays_day_placeholder,todaysModelItem.getDay()));
//                                todaysProgressTitle.setText(Html.fromHtml(getString(R.string.todays_progress, todaysModelItem.getTitle(), todaysModelItem.getIntro())));
//                            }*/
//
//                            weeklyProgressAdapter.addPregnancyProgress(weeklyProgressModelArrayList);
//
//                           /* todaysProgressCard.setOnClickListener(view -> {
//                                Bundle detailBundle = new Bundle();
//                                detailBundle.putParcelable("today", todaysModelItem);
//                                PregnancyWeeklyProgressDetail pregWeekDetail = new PregnancyWeeklyProgressDetail();
//                                pregWeekDetail.setArguments(detailBundle);
//                                pregWeekDetail.show(getChildFragmentManager(), "Day Details");
//                            });*/
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    Log.v("weekly error", String.valueOf(error));
                    progressBar.setVisibility(View.GONE);

                    if (weeklyProgressAdapter.getItemCount() == 0)
                        Snackbar.make(currentWeekSpinner, "Error fetching data", Snackbar.LENGTH_SHORT)
                            .setAction("Ok", view -> {}).show();
                });

        queue.add(jsonObjectRequest);
    }

    public void saveWeeklyProgressDataToDb(List<WeeklyProgress> progress){
        for (WeeklyProgress weeklyProgress : progress){
            //ensure weeks have a value
            weeklyProgress.populateWeekData();
            //save each data to DB
            pregnancyWeeklyProgressViewModel.bulkInsertWeeklyProgress(weeklyProgress.getData());
        }
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
