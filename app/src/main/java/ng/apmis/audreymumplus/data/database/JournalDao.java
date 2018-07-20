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
import ng.apmis.audreymumplus.ui.Journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal")
    LiveData<List<JournalModel>> getAllJournalEntries();

    @Query("SELECT * FROM journal WHERE week = :week")
    LiveData<List<JournalModel>> getJournalByWeek(String week);
/*

    @Query("SELECT COUNT(id) FROM journal WHERE date <= :date")
    int countAllPastJournal(Date date);
*/

    @Query("SELECT * FROM appointments")
    LiveData<List<Appointment>> getSavedAppointments();

    @Insert()
    void insertAppointment(Appointment appointment);

    @Query("SELECT * FROM journal WHERE date = :date")
    LiveData<JournalModel> getJournalByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertJournal (List<JournalModel> allJournals);

    @Insert()
    void insertJournal (JournalModel journalModel);

    @Update()
    void updatePerson (Person person);

    @Insert()
    void insertPerson (Person person);

    @Query("DELETE FROM person")
    void deletePerson ();

    @Query("SELECT * FROM person")
    LiveData<Person> getPerson();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertForums (List<ChatForumModel> allForums);

    @Query("SELECT * FROM forums")
    LiveData<List<ChatForumModel>> getChatForums();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsertChats (List<ChatContextModel> allForums);

    @Query("SELECT * FROM forums")
    LiveData<List<ChatContextModel>> getChats();

    @Insert()
    void insertChat(ChatContextModel chatContextModel);


    /*@Query("DELETE FROM journal WHERE date < :date")
    void deleteOldWeather(String date);*/

}
