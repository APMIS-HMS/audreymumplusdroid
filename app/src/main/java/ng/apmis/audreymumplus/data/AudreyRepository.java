package ng.apmis.audreymumplus.data;

import android.arch.lifecycle.LiveData;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.JournalDao;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.database.WeeklyProgressData;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.ui.kickcounter.KickCounterModel;
import ng.apmis.audreymumplus.ui.pills.PillModel;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalModel;
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
            mJournalDao.bulkInsertJournal(networkdata);
            Log.d(LOG_TAG, "New values inserted");
        }));

    }

    public LiveData<List<JournalModel>> getAllJournals() {
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

    public void updatePerson (Person person) {
        Log.e("TAGGED", "Updated person DAO "+person.getFirstName()  +" "+person.getId());
        mJournalDao.updatePerson(person);
    }

    public void deletePerson () {
        mJournalDao.deletePerson();
    }

    public LiveData<Person> getPerson () {
        return mJournalDao.getPerson();
    }

    public Person getStaticPerson () {
        return mJournalDao.getStaticPerson();
    }

    public long saveAppointment (Appointment appointment) {
        return mJournalDao.insertAppointment(appointment);
    }

    public void updateAppointment (Appointment appointment) {
        mJournalDao.updateAppointment(appointment);
    }

    public LiveData<List<Appointment>> getAllAppointments () {
        return mJournalDao.getSavedAppointments();
    }

    public List<Appointment> getStaticAppointmentList () {
        return mJournalDao.getStaticAppointmentList();
    }

    public Appointment getStaticAppointment (long appointment_id) {
        return mJournalDao.getStaticAppointment(appointment_id);
    }

    public void deleteAppointment (Appointment appointment) {
        mJournalDao.deleteAppointment(appointment);
    }

    public void updatePersonWithPregWeekDay (Person person) {
        mJournalDao.updatePerson(person);
    }

    public int getJournalCountOnWeek(String week) {
        return mJournalDao.countAllJournalOnWeek(week);
    }

    public int getTotalWeekKickCount(String week) {
        return mJournalDao.totalKicksInWeek(week);
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

            int whatDay = eddDate.getDayOfYear();
            Log.v("diff of year", String.valueOf(whatDay));

            //Number of days left to get to EDD
            Days howMany = Days.daysBetween(LocalDate.fromDateFields(new Date()), eddDate);


            Log.v("diff days remaining", String.valueOf(howMany.getDays()));

            //Get current week divide totals days spent by 7
            int getWeek = (totalPregDays - howMany.getDays()) / 7;

            if ((totalPregDays - howMany.getDays()) % 7 > 0) {
                getWeek = getWeek + 1;
            }

            if (getWeek < 0) {
                getWeek = 1;
            }

            String currentWeekProgress = Week.valueOf("Week" + getWeek).getWeek();
            Log.v("current week", currentWeekProgress);

            //The actual day we are on in the pregnancy progress
            int currentDayProgress = totalPregDays - howMany.getDays();

            person.setWeek(currentWeekProgress);
            person.setDay(currentDayProgress);

            AudreyMumplus.getInstance().diskIO().execute(() -> {
                    updatePersonWithPregWeekDay(person);
                Log.v("FIFTEEN MINUTES", person.toString());
            });

        }

    }

    public void insertAllForums (List<ChatForumModel> allForums) {
        mJournalDao.bulkInsertForums(allForums);
    }

    public LiveData<List<ChatForumModel>> getUpdatedForums () {
        return mJournalDao.getChatForums();
    }

    public void insertAllChats (List<ChatContextModel> allForums) {
        mJournalDao.bulkInsertChats(allForums);
    }

    public LiveData<List<ChatContextModel>> getUpdatedChats (String forumName) {
        return mJournalDao.getChats(forumName);
    }

    public void insertChat (ChatContextModel chatContextModel) {
        mJournalDao.insertChat(chatContextModel);
    }

    public long insertPillReminder (PillModel pillModel) {
        return mJournalDao.insertPillReminder(pillModel);
    }

    public void deletePillReminder (PillModel pillModel) {
        mJournalDao.deletePillReminder(pillModel);
    }

    public void updatePillReminder (PillModel pillModel) {
        mJournalDao.updatePillReminder(pillModel);
    }

    public PillModel getPillModel (long _id) {
        return mJournalDao.getPillModel(_id);
    }

    public LiveData<List<PillModel>> getAllPills () {
        return mJournalDao.getAllPills();
    }

    public LiveData<List<KickCounterModel>> getAllKickCount () {
        return mJournalDao.getAllKickData();
    }

    public long insertKickCount (KickCounterModel kickCounterModel) {
        return mJournalDao.insertKickCounter(kickCounterModel);
    }
    public LiveData<Integer> getKickCountPerDay (int day) {
        return mJournalDao.getKickCountPerDay(day);
    }


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

    public LiveData<JournalModel> getDaysJournal(String day) {
        return mJournalDao.getTodaysJournal(day);
    }

    public LiveData<List<WeeklyProgressData>> getWeeklyProgressData(){
        return mJournalDao.getAllWeeklyProgressData();
    }

    public void bulkInsertWeeklyProgressData(List<WeeklyProgressData> progressData){
        mExecutors.diskIO().execute(() -> mJournalDao.bulkInsertWeeklyProgress(progressData));
    }
}
