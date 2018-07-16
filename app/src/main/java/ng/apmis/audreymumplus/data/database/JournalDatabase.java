package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@TypeConverters({JournalConverters.class})
@Database(entities = {JournalModel.class, Person.class, Appointment.class}, version = 2, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDao dailyJournalDao();

    private static final String DATABASE_NAME = "audrey-db";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile JournalDatabase sInstance;

    public static JournalDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            JournalDatabase.class, JournalDatabase.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return sInstance;
    }

   /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE person "
                    + " DROP COLUMN profileImage BLOB");
            database.execSQL("ALTER TABLE person "
                    + " ADD COLUMN profileImage TEXT");
        }
    };*/

   //DATA migration for adding or dropping column sample
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Book "
                    + " ADD COLUMN pub_year INTEGER");
        }
    };

    //DATA migration code for changing column fields
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE new_person ('id' INTEGER PRIMARY KEY NOT NULL, firstName TEXT, lastName TEXT, email TEXT, personId TEXT, dateOfBirth TEXT, motherMaidenName TEXT, primaryContactPhoneNo TEXT, expectedDateOfDelivery TEXT, profileImage TEXT)");
// Copy the data
            database.execSQL(
                    "INSERT INTO new_person (id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage) SELECT id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage FROM person");
// Remove the old table
            database.execSQL("DROP TABLE person");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE new_person RENAME TO person");
        }
    };

}
