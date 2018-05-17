package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@TypeConverters({JournalConverters.class})
@Database(entities = {DailyJournal.class}, version = 1)
public abstract class DailyJournalDatabase extends RoomDatabase {
    public abstract DailyJournalDao dailyJournalDao();

    private static final String DATABASE_NAME = "dailyjournal";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile DailyJournalDatabase sInstance;

    public static DailyJournalDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DailyJournalDatabase.class, DailyJournalDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
