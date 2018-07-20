package ng.apmis.audreymumplus.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.DailyJournal;
import ng.apmis.audreymumplus.data.database.JournalDao;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.Week;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

public class AudreyRepository {

    private static final String LOG_TAG = AudreyRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AudreyRepository sInstance;
    private final JournalDao mJournalDao;
    private final MumplusNetworkDataSource mJournalNetworkData;
    private final AudreyMumplus mExecutors;
    private boolean mInitialized = false;

    private AudreyRepository(JournalDao weatherDao,
                               MumplusNetworkDataSource weatherNetworkDataSource,
                               AudreyMumplus executors) {
        mJournalDao = weatherDao;
        mJournalNetworkData = weatherNetworkDataSource;
        mExecutors = executors;
        LiveData<List<JournalModel>> networkData = mJournalNetworkData.getCurrentDailyJournal();

        networkData.observeForever(networkdata -> mExecutors.diskIO().execute(() -> {
            // Insert our new weather data into Sunshine's database
            //deleteOldData();
            Log.d(LOG_TAG, "Old weather deleted");
            // Insert our new weather data into Sunshine's database
            mJournalDao.bulkInsert(networkdata);
            Log.d(LOG_TAG, "New values inserted");
        }));

    }

    public LiveData<JournalModel> getWeatherByDate(Date date) {
        //initializeData();
        return mJournalDao.getJournalByDate(date);
    }

    public LiveData<List<JournalModel>> getAllJournals () {
        return mJournalDao.getAllJournalEntries();
    }

    public LiveData<List<JournalModel>> getJournalByWeek (String week) {
        return mJournalDao.getJournalByWeek(week);
    }

    public void saveJournal (JournalModel journalModel) {
        mJournalDao.insertJournal(journalModel);
    }

    public void savePerson (Person person) {
        mJournalDao.insertPerson(person);
    }

    public void deletePerson () {
        mJournalDao.deletePerson();
    }

    public LiveData<Person> getPerson () {
        return mJournalDao.getPerson();
    }

    public void saveAppointment (Appointment appointment) {
        mJournalDao.insertAppointment(appointment);
    }

    public LiveData<List<Appointment>> getAllAppointments () {
        return mJournalDao.getSavedAppointments();
    }

    public void updatePersonWithPregWeekDay (Person person) {
        mJournalDao.updatePerson(person);
    }

    public void getDayWeek (Person person) {

        if (!TextUtils.isEmpty(person.getExpectedDateOfDelivery())) {

            //Estimated regular days of pregnancy @40 weeks
            int totalPregDays = 280;

            //Format edd with Joda datetime
            DateTime dateTime = DateTime.parse(person.getExpectedDateOfDelivery());

            //Convert to regular Date object
            Date convertDateTime = new Date(dateTime.getMillis());

            //Convert to Joda LocalDate for comparison
            LocalDate eddDate = LocalDate.fromDateFields(convertDateTime);

            //Difference between today and edd
            Days howMany = Days.daysBetween(LocalDate.fromDateFields(new Date()), eddDate);

            //Get current week divide totals days spent by 7
            int getWeek = (totalPregDays - howMany.getDays()) / 7;

            String currentWeekProgress = Week.valueOf("Week" + getWeek).getWeek();
            Log.v("current week", currentWeekProgress);
            int currentDayProgress = howMany.getDays();


            person.setWeek(currentWeekProgress);
            person.setDay(currentDayProgress);

            Log.v("person of update", person.toString());

            AudreyMumplus.getInstance().diskIO().execute(() -> {
                    updatePersonWithPregWeekDay(person);
            });

        }

    }

/*

    public LiveData<List<DailyJournal>> getCurrentWeatherForecasts() {
        //initializeData();
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        return mWeatherDao.getCurrentWeatherForecasts(today);
    }
*/

    public synchronized static AudreyRepository getInstance (JournalDao journalDao, MumplusNetworkDataSource mumplusNetworkDataSource, AudreyMumplus audreyMumplus) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AudreyRepository(journalDao, mumplusNetworkDataSource,
                        audreyMumplus);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

}
