package ng.apmis.audreymumplus.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URL;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.DailyJournal;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class MumplusNetworkDataSource {

    private static Object LOCK;
    private static MumplusNetworkDataSource sInstance;
    private final Context mContext;

    private static final String LOG_TAG = MumplusNetworkDataSource.class.getSimpleName();

    private final AudreyMumplus mExecutors;

    private final MutableLiveData<DailyJournal[]> mDownloadedDailyJournal;

    MumplusNetworkDataSource(Context context, AudreyMumplus executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedDailyJournal = new MutableLiveData<>();
    }

    public LiveData<DailyJournal[]> getCurrentDailyJournal() {
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
   /* public void scheduleRecurringFetchWeatherSync() {
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

    /**
     * Gets the newest weather
     */
    void fetchWeather() {
        Log.d(LOG_TAG, "Fetch weather started");
        mExecutors.networkIO().execute(() -> {

            //mDownloadedDailyJournal.postValue(response.getWeatherForecast());

        });
    }

}
