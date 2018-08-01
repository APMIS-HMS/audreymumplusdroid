package ng.apmis.audreymumplus.data.network;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.DatabaseFirebaseJobService;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;
import ng.apmis.audreymumplus.utils.Week;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class MumplusNetworkDataSource {

    private static final Object LOCK = new Object();
    private static MumplusNetworkDataSource sInstance;
    private final Context mContext;

    private static final String LOG_TAG = MumplusNetworkDataSource.class.getSimpleName();

    private final AudreyMumplus mExecutors;

    private final MutableLiveData<List<JournalModel>> mDownloadedDailyJournal;
    private final MutableLiveData<Person> personName;
    SharedPreferencesManager sharedPreferencesManager;
    RequestQueue queue;
    private static final String BASE_URL = "https://audrey-mum.herokuapp.com/";

    private static final String WEEK_DAY_SYNC_TAG = "week-day";


    private MumplusNetworkDataSource(Context context, AudreyMumplus executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedDailyJournal = new MutableLiveData<>();
        queue = Volley.newRequestQueue(context);
        sharedPreferencesManager = new SharedPreferencesManager(context);
        personName = new MutableLiveData<>();
    }

    public LiveData<List<JournalModel>> getCurrentDailyJournal() {
        return mDownloadedDailyJournal;
    }

    public static MumplusNetworkDataSource getInstance(Context context, AudreyMumplus executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MumplusNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startDailyJournalSync() {
       /* Intent intentToFetch = new Intent(mContext, SunshineSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");*/
    }

    /**
     * Schedules a repeating job service which fetches the weather.
     */
   /* public void scheduleDailyDayWeekUpdate() {
        Driver driver = new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync Sunshine
        Job syncSunshineJob = dispatcher.newJobBuilder()
                *//* The Service that will be used to sync Sunshine's data *//*
                .setService(SunshineFirebaseJobService.class)
                *//* Set the UNIQUE tag used to identify this Job *//*
                .setTag(SUNSHINE_SYNC_TAG)
                *//*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
                 *//*
                .setConstraints(Constraint.ON_ANY_NETWORK)
                *//*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 *//*
                .setLifetime(Lifetime.FOREVER)
                *//*
                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
                 *//*
                .setRecurring(true)
                *//*
                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 *//*
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                *//*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 *//*
                .setReplaceCurrent(true)
                *//* Once the Job is ready, call the builder's build method to return the Job *//*
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
        Log.d(LOG_TAG, "Job scheduled");
    }*/
    public void fetchSinglePeople(String personId) {
        Log.d(LOG_TAG, "Fetch weather started");
        mExecutors.networkIO().execute(() -> {

            String url = String.format(BASE_URL + "people?personId=%1$s", personId);

            JsonObjectRequest peopleJob = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

                JSONObject dataObject = new JSONObject();

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    dataObject = (JSONObject) jsonArray.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Person personFromPeople = new Gson().fromJson(dataObject.toString(), Person.class);
                personFromPeople.setDay(0);
                personFromPeople.setWeek(Week.Week1.week);

                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    //deleting all user data locally
                    InjectorUtils.provideRepository(mContext).deletePerson();
                    //insert new gotten user
                    InjectorUtils.provideRepository(mContext).savePerson(personFromPeople);
                });

            }, error -> {

                Toast.makeText(mContext, "There was a problem", Toast.LENGTH_SHORT).show();

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }

            };

            queue.add(peopleJob);
        });

    }

    public LiveData<Person> getPersonEmail() {
        return personName;
    }

    public void fetchUserName(String email) {
        Log.d(LOG_TAG, "Fetch weather started");
        mExecutors.networkIO().execute(() -> {

            String url = String.format(BASE_URL + "people?email=%1$s", email);

            JsonObjectRequest peopleJob = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

                JSONObject dataObject = new JSONObject();

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    dataObject = (JSONObject) jsonArray.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Person personFromPeople = new Gson().fromJson(dataObject.toString(), Person.class);
                personName.postValue(personFromPeople);

            }, error -> {

                Toast.makeText(mContext, "There was a problem", Toast.LENGTH_SHORT).show();

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }

            };

            queue.add(peopleJob);
        });

    }

    public void updateProfileGetAudrey(String currentPersonId, JSONObject changeFields, FragmentActivity context, boolean getAudrey) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Updating Profile");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        mExecutors.networkIO().execute(() -> {

            String url = String.format(BASE_URL + "people?personId=%1$s", currentPersonId);

            StringRequest updateProfileRequest = new StringRequest(Request.Method.PATCH, url,
                    response -> {
                        Log.v("update profile result", response);
                        JSONObject job = new JSONObject();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            job = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Person updatedPerson = new Gson().fromJson(job.toString(), Person.class);
                        AudreyMumplus.getInstance().diskIO().execute(() -> {
                                InjectorUtils.provideRepository(mContext).deletePerson();
                                InjectorUtils.provideRepository(mContext).savePerson(updatedPerson);
                        });
                        pd.dismiss();
                        Toast.makeText(mContext, "Update update successful", Toast.LENGTH_SHORT).show();
                        context.getSupportFragmentManager().popBackStack("preg-frag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    },
                    error -> {
                        Log.v("profile update err", error.toString());
                        Toast.makeText(mContext, "There was an error updating profile", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return changeFields == null ? null : changeFields.toString().getBytes();
                }
            };
            queue.add(updateProfileRequest);
        });
    }

    public void updateProfileImage(JSONObject changeFields, Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Updating Profile Image");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        mExecutors.networkIO().execute(() -> {

            JsonObjectRequest updateProfileImageRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "profile-pix", changeFields,
                    response -> {
                        Log.v("update profile result", response.toString());
                        JSONObject job = new JSONObject();
                        try {
                            job = response.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Person updatedPerson = new Gson().fromJson(job.toString(), Person.class);
                        AudreyMumplus.getInstance().diskIO().execute(() -> {
                                InjectorUtils.provideRepository(mContext).deletePerson();
                                InjectorUtils.provideRepository(mContext).savePerson(updatedPerson);
                        });
                        pd.dismiss();
                        Toast.makeText(mContext, "Update update successful", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.v("profile update err", error.toString());
                        Toast.makeText(mContext, "There was an error updating profile", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            updateProfileImageRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(updateProfileImageRequest);
        });
    }


    /**
     * Schedules a repeating job service which fetches the weather.
     */
    public void scheduleDailyDayWeekUpdate () {

        final int periodicity = (int) TimeUnit.HOURS.toSeconds(12); // Every 12 hours periodicity expressed as seconds
        final int toleranceInterval = (int)TimeUnit.HOURS.toSeconds(1); // a small(ish) window of time when triggering is OK

        Driver driver = new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync Sunshine
        Job databaseSyncJob = dispatcher.newJobBuilder()
                // The Service that will be used to sync Sunshine's data
                .setService(DatabaseFirebaseJobService.class)
                //   Set the UNIQUE tag used to identify this Job
                .setTag(WEEK_DAY_SYNC_TAG)
/*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
*/
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(periodicity, periodicity + toleranceInterval))
                .setLifetime(Lifetime.FOREVER)
/*

                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
*/
                .setReplaceCurrent(true)
                //  Once the Job is ready, call the builder's build method to return the Job
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(databaseSyncJob);
        Log.d(LOG_TAG, "Job scheduled");
    }

}
