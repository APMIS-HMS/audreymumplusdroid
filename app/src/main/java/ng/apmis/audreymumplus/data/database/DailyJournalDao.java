package ng.apmis.audreymumplus.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */

@Dao
public interface DailyJournalDao {

    @Query("SELECT * FROM weather WHERE date >= :date")
    LiveData<List<DailyJournal>> getCurrentWeatherForecasts(Date date);

    @Query("SELECT COUNT(id) FROM weather WHERE date >= :date")
    int countAllFutureWeather(Date date);

    @Query("SELECT * FROM weather WHERE date = :date")
    LiveData<DailyJournal> getWeatherByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(DailyJournal... weatherEntries);

    @Query("DELETE FROM weather WHERE date < :date")
    void deleteOldWeather(Date date);

}
