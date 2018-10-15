package ng.apmis.audreymumplus.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.ui.kickcounter.KickCounterModel;
import ng.apmis.audreymumplus.ui.pills.PillModel;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal")
    LiveData<List<JournalModel>> getAllJournalEntries();

    @Query("SELECT * FROM journal WHERE week = :week")
    LiveData<List<JournalModel>> getJournalByWeek(String week);

    @Query("SELECT COUNT(*) FROM journal WHERE week = :week")
    int countAllJournalOnWeek (String week);

    @Query("SELECT * FROM journal WHERE day = :day")
    LiveData<JournalModel> getTodaysJournal(String day);

    @Query("SELECT * FROM appointments")
    LiveData<List<Appointment>> getSavedAppointments();

    @Query("SELECT * FROM appointments WHERE _id = :appointment_id")
    Appointment getStaticAppointment(long appointment_id);

    @Query("SELECT * FROM appointments")
    List<Appointment> getStaticAppointmentList();

    @Insert()
    long insertAppointment(Appointment appointment);

    @Delete()
    void deleteAppointment(Appointment appointment);

    @Update()
    void updateAppointment(Appointment appointment);

    @Query("SELECT * FROM journal WHERE date = :date")
    LiveData<JournalModel> getJournalByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertJournal (List<JournalModel> allJournals);

    @Insert()
    void insertJournal (JournalModel journalModel);

    //@Update(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updatePerson (Person person);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerson (Person person);

    @Query("DELETE FROM person")
    void deletePerson();

    @Query("SELECT * FROM person")
    LiveData<Person> getPerson();

    @Query("SELECT * FROM person")
    Person getStaticPerson();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertForums (List<ChatForumModel> allForums);


    /*
    * Chats and Forum queries
    * */
    @Query("SELECT * FROM forums")
    LiveData<List<ChatForumModel>> getChatForums();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsertChats (List<ChatContextModel> allForums);

    @Query("SELECT * FROM chat WHERE forumName = :forumName")
    LiveData<List<ChatContextModel>> getChats(String forumName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChat(ChatContextModel chatContextModel);

    @Insert()
    long insertPillReminder(PillModel pillModel);

    @Delete()
    void deletePillReminder(PillModel pillModel);

    @Update()
    void updatePillReminder(PillModel pillModel);

    @Query("SELECT * FROM pillreminder WHERE _id = :id")
    PillModel getPillModel(long id);

    @Query("SELECT * FROM pillreminder")
    LiveData<List<PillModel>> getAllPills();

    @Query("SELECT * FROM kickcounter")
    LiveData<List<KickCounterModel>> getAllKickData();

    @Insert()
    long insertKickCounter(KickCounterModel kickCounterModel);

    @Query("SELECT SUM(kicks) FROM kickcounter WHERE week = :week")
    int totalKicksInWeek (String week);

    @Query("SELECT SUM(kicks) FROM kickcounter WHERE day = :day")
    LiveData<Integer> getKickCountPerDay(int day);


    /*
        WEEKLY PROGRESS DATA
     */
    @Query("SELECT * FROM weeklyprogressdata")
    LiveData<List<WeeklyProgressData>> getAllWeeklyProgressData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsertWeeklyProgress(List<WeeklyProgressData> data);

}
