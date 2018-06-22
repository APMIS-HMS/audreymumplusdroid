package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ng.apmis.audreymumplus.ui.Journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@TypeConverters({JournalConverters.class})
@Database(entities = {JournalModel.class, Person.class}, version = 1, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDao dailyJournalDao();

    private static final String DATABASE_NAME = "dailyjournal";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile JournalDatabase sInstance;

    public static JournalDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            JournalDatabase.class, JournalDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
