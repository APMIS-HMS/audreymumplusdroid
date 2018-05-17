package ng.apmis.audreymumplus.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.Date;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.DailyJournal;
import ng.apmis.audreymumplus.data.database.DailyJournalDao;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class AudreyRepository {

    private static final String LOG_TAG = AudreyRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AudreyRepository sInstance;
    private final DailyJournalDao mDailyJournalDao;
    private final MumplusNetworkDataSource mJournalNetworkData;
    private final AudreyMumplus mExecutors;
    private boolean mInitialized = false;

    private AudreyRepository(DailyJournalDao weatherDao,
                               MumplusNetworkDataSource weatherNetworkDataSource,
                               AudreyMumplus executors) {
        mDailyJournalDao = weatherDao;
        mJournalNetworkData = weatherNetworkDataSource;
        mExecutors = executors;
        LiveData<DailyJournal[]> networkData = mJournalNetworkData.getCurrentDailyJournal();

        networkData.observeForever(networkdata -> mExecutors.diskIO().execute(() -> {
            // Insert our new weather data into Sunshine's database
            //deleteOldData();
            Log.d(LOG_TAG, "Old weather deleted");
            // Insert our new weather data into Sunshine's database
            mDailyJournalDao.bulkInsert(networkdata);
            Log.d(LOG_TAG, "New values inserted");
        }));

    }

    public LiveData<DailyJournal> getWeatherByDate(Date date) {
        //initializeData();
        return mDailyJournalDao.getJournalByDate(date);
    }
/*

    public LiveData<List<DailyJournal>> getCurrentWeatherForecasts() {
        //initializeData();
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        return mWeatherDao.getCurrentWeatherForecasts(today);
    }
*/

    public synchronized static AudreyRepository getInstance (DailyJournalDao dailyJournalDao, MumplusNetworkDataSource mumplusNetworkDataSource, AudreyMumplus audreyMumplus) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AudreyRepository(dailyJournalDao, mumplusNetworkDataSource,
                        audreyMumplus);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

}
