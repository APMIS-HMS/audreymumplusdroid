package ng.apmis.audreymumplus.data.network;

import android.app.Dialog;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.DatabaseFirebaseJobService;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.NotificationUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;
import ng.apmis.audreymumplus.utils.Utils;
import ng.apmis.audreymumplus.utils.Week;

import static ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity.UNKWOWN_PROGRESS;
import static ng.apmis.audreymumplus.utils.Constants.BASE_URL;

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
    private SharedPreferencesManager sharedPreferencesManager;
    private RequestQueue queue;

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

    public void fetchPeopleAndSaveToDb(String personId) {
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
                    InjectorUtils.provideRepository(mContext).savePerson(personFromPeople);
                });

            }, error -> {

                Toast.makeText(mContext, "There was a problem", Toast.LENGTH_SHORT).show();

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
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

    public void postChat(Object chat, String forum) {
        AudreyMumplus.getInstance().networkIO().execute(() -> {
            JsonObjectRequest job = new JsonObjectRequest(Request.Method.POST, BASE_URL + "chat", (JSONObject) chat,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("ForumEmit", "Posted on " + forum);
                            InjectorUtils.provideSocketInstance().emit("forum", forum);
                            AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(mContext).insertChat(new Gson().fromJson(response.toString(), ChatContextModel.class))
                            );

                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ForumEmit error", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            queue.add(job);
        });

    }

    public void getChat(String forumName) {

        JSONObject getChatJsonObject = new JSONObject();

        try {
            getChatJsonObject.put("forumName", forumName);
            getChatJsonObject.put("createdAt", sharedPreferencesManager.getLastChatForumAndCreatedAt(forumName).date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("createdAt bbb", String.valueOf(sharedPreferencesManager.getLastChatForumAndCreatedAt(forumName).toString()));

        AudreyMumplus.getInstance().networkIO().execute(() -> {
            JsonObjectRequest job = new JsonObjectRequest(Request.Method.POST, BASE_URL + "get-chat", getChatJsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                List<ChatContextModel> allListFromServer = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.getJSONArray("data").toString(), ChatContextModel[].class)));

                                Log.e("all returned chats", allListFromServer.toString());

                                if (allListFromServer.size() > 0) {
                                    if (DashboardActivity.globalOpenChatForum != null && DashboardActivity.globalOpenChatForum.equals(forumName)) {
                                        NotificationUtils.buildForegroundChatNotification(mContext);
                                    } else {
                                        NotificationUtils.buildBackgroundChatNotification(mContext, allListFromServer.get(allListFromServer.size() -1));
                                    }
                                    AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(mContext).insertAllChats(allListFromServer));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ForumEmit error", error.toString());
                        }
                    })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            queue.add(job);
        });
    }

    public void getChats(String forumName) {

        sharedPreferencesManager.addForumNameAndLastCreatedAtAsStringInPrefs(forumName, Utils.localDateToDbString(Calendar.getInstance().getTime()), 0);

        JSONObject getChatJsonObject = new JSONObject();
        try {
            getChatJsonObject.put("forumName", forumName);
            getChatJsonObject.put("createdAt", sharedPreferencesManager.getLastChatForumAndCreatedAt(forumName).date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("createdAt getChats", String.valueOf(sharedPreferencesManager.getLastChatForumAndCreatedAt(forumName).toString()));

        AudreyMumplus.getInstance().networkIO().execute(() -> {
            JsonObjectRequest job = new JsonObjectRequest(Request.Method.POST, BASE_URL + "get-chat", getChatJsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                List<ChatContextModel> chatContextModel = new ArrayList<>(Arrays.asList(new Gson().fromJson(response.getJSONArray("data").toString(), ChatContextModel[].class)));

                                Log.e("all returned chats", chatContextModel.toString());

                                AudreyMumplus.getInstance().diskIO().execute(() -> InjectorUtils.provideRepository(mContext).insertAllChats(chatContextModel));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ForumEmit error", error.toString());
                        }
                    })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            queue.add(job);
        });


    }


    public void updateProfileGetAudrey(String personId, JSONObject changeFields, FragmentActivity context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Updating Profile");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        mExecutors.networkIO().execute(() -> {

            String url = String.format(BASE_URL + "people?personId=%1$s", personId);

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
                        //set days to -1, signifying unknown time since days aren't stored in online server
                        updatedPerson.setDay(UNKWOWN_PROGRESS);

                        AudreyMumplus.getInstance().diskIO().execute(() -> {
                            InjectorUtils.provideRepository(mContext).updatePerson(updatedPerson);
                            Log.v("Tag", updatedPerson.toString());

                        });
                        pd.dismiss();
                        Toast.makeText(mContext, "Update update successful", Toast.LENGTH_SHORT).show();
                        try {
                            context.getSupportFragmentManager().popBackStack("preg-frag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } catch (IllegalStateException e) {
                            Log.e(LOG_TAG + ": Illegalstate", e.getMessage());
                        }
                    },
                    error -> {
                        Log.v("profile update err", error.toString());
                        Toast.makeText(mContext, "There was an error updating profile", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
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
                            InjectorUtils.provideRepository(mContext).updatePerson(updatedPerson);
                        });
                        pd.dismiss();
                        Toast.makeText(mContext, "Update update successful", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        if (error.getMessage() != null) {
                            Log.e("profile update err", "message: " + error.getMessage());
                        }
                        Log.e("profile update err", "message: " + error.toString());
                        Toast.makeText(mContext, "There was an error updating profile", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            updateProfileImageRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(updateProfileImageRequest);
        });
    }


    public void getForums(int skipCount) {

        String url = String.format(BASE_URL + "forum?approved=%1$s&$skip=" + skipCount, true);
        Log.e("forum url", url);

        JsonObjectRequest getForumRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<ChatForumModel> allForums = new ArrayList<>();
                    try {

                        new SharedPreferencesManager(mContext).setTotalRoomCountOnserver(response.getInt("total"));

                        JSONObject job = new JSONObject(response.toString());
                        JSONArray jar = job.getJSONArray("data");

                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject forumObj = (JSONObject) jar.get(i);
                            ChatForumModel eachForum = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                            allForums.add(eachForum);
                        }

                        AudreyMumplus.getInstance().diskIO().execute(() -> {
                            InjectorUtils.provideRepository(mContext).insertAllForums(allForums);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.getMessage() != null) {
                        Log.e("get forums err", "message: " + error.getMessage());
                    }
                    Log.e("get forums err", "message: " + error.toString());
                    Toast.makeText(mContext, "There was an error getting forums", Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                return params;
            }
        };

        getForumRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

        queue.add(getForumRequest);
    }


    /**
     * Schedules a repeating job service which fetches the weather.
     */
    public void scheduleDailyDayWeekUpdate() {

        final int periodicity = (int) TimeUnit.HOURS.toSeconds(12); // Every 12 hours periodicity expressed as seconds
        final int toleranceInterval = (int) TimeUnit.HOURS.toSeconds(1); // a small(ish) window of time when triggering is OK

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


    public void joinForum(Context context, String personId, String forumName) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Requesting Forum Creation");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        JSONObject joinForumObject = new JSONObject();
        try {
            joinForumObject.put("personId", personId);
            joinForumObject.put("forumName", forumName);
        } catch (JSONException e) {
        }

        AudreyMumplus.getInstance().networkIO().execute(() -> {

            JsonObjectRequest joinForumRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "join-forum", joinForumObject,
                    response -> {
                        Log.d("joinforum response", response.toString());

                        pd.dismiss();
                        Toast.makeText(mContext, "Request sent to admin", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        if (error.getMessage() != null) {
                            Log.e("join forums err", "message: " + error.getMessage());
                        }
                        Log.e("join forums err", "message: " + error.toString());
                        pd.dismiss();
                        Toast.makeText(mContext, "There was an error joining forums", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            joinForumRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(joinForumRequest);
        });

    }

    public void createForum(String forumName, Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Requesting Forum Creation");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        JSONObject createForumObject = new JSONObject();
        try {
            createForumObject.put("name", forumName);
        } catch (JSONException e) {
        }

        AudreyMumplus.getInstance().networkIO().execute(() -> {

            JsonObjectRequest createForumRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "forum", createForumObject,
                    response -> {
                        Log.d("create forum response", response.toString());

                        pd.dismiss();
                        Toast.makeText(mContext, "Please wait admin approval", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        if (error.getMessage() != null) {
                            Log.e("create forums err", "message: " + error.getMessage());
                        }
                        Log.e("create forums err", "message: " + error.toString());
                        pd.dismiss();
                        Toast.makeText(mContext, "There was an error creating forums", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + sharedPreferencesManager.getUserToken());
                    return params;
                }
            };

            createForumRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(createForumRequest);
        });

    }

    public void changePassword (Context context, String password) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Changing password");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        JSONObject changePasswordObject = new JSONObject();
        try {
            changePasswordObject.put("newPassword", password);
            changePasswordObject.put("reEnterPassword", password);
        } catch (JSONException e) {
        }

        AudreyMumplus.getInstance().networkIO().execute(() -> {

            JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "reset-password", changePasswordObject,
                    response -> {
                        Log.d("Change pwd response", response.toString());

                        pd.dismiss();
                        Toast.makeText(mContext, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        if (error.getMessage() != null) {
                            Log.e("Change password err", "message: " + error.getMessage());
                        }
                        Log.e("Change password err", "message: " + error.toString());
                        pd.dismiss();

                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                    });

            changePasswordRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(changePasswordRequest);
        });

    }

    public void resetPassword(Context context, String email) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Resetting password");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        JSONObject resetPasswordObject = new JSONObject();
        try {
            resetPasswordObject.put("email", email);
        } catch (JSONException e) {
        }

        AudreyMumplus.getInstance().networkIO().execute(() -> {

            JsonObjectRequest resetPasswordRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "reset-password", resetPasswordObject,
                    response -> {
                        Log.d("Reset password response", response.toString());

                        pd.dismiss();
                        Toast.makeText(mContext, "Check email for new password", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        if (error.getMessage() != null) {
                            Log.e("Reset password err", "message: " + error.getMessage());
                        }
                        Log.e("Reset password err", "message: " + error.toString());
                        pd.dismiss();

                        Toast.makeText(mContext, "Check entered email", Toast.LENGTH_SHORT).show();
                    });

            resetPasswordRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));

            queue.add(resetPasswordRequest);
        });

    }
}
