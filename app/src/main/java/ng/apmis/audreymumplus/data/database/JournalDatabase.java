package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.ui.kickcounter.KickCounterModel;
import ng.apmis.audreymumplus.ui.pills.PillModel;
import ng.apmis.audreymumplus.ui.pills.PillsTypeConverter;
import ng.apmis.audreymumplus.ui.pregnancymodule.journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@Database(entities = {JournalModel.class, Person.class, Appointment.class, ChatForumModel.class,
        ChatContextModel.class, PillModel.class, KickCounterModel.class, WeeklyProgressData.class}, version = 3, exportSchema = false)
@TypeConverters({JournalConverters.class, PillsTypeConverter.class})
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
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return sInstance;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("DROP TABLE appointments");

            database.execSQL(
                    "CREATE TABLE appointments ('_id' INTEGER PRIMARY KEY NOT NULL, title TEXT, appointmentAddress TEXT, appointmentDetails TEXT, appointmentTime INTEGER NOT NULL, muteAlarm INTEGER NOT NULL)");

            database.execSQL(
                    "CREATE TABLE pillreminder ('_id' INTEGER PRIMARY KEY NOT NULL, pillName TEXT, qtyPerTime TEXT, frequency TEXT, unit TEXT, duration TEXT, instruction TEXT, pillTimes TEXT, muteReminder INTEGER NOT NULL)"
            );

            database.execSQL(
                    "CREATE TABLE kickcounter ('_id' INTEGER PRIMARY KEY NOT NULL, kicks INTEGER NOT NULL, week TEXT, duration TEXT, date INTEGER NOT NULL)");

        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE black_person ('id' INTEGER PRIMARY KEY NOT NULL, _id TEXT UNIQUE, firstName TEXT, lastName TEXT, email TEXT, personId TEXT, dateOfBirth TEXT, motherMaidenName TEXT, primaryContactPhoneNo TEXT, ExpectedDateOfDelivery TEXT, profileImage TEXT, week TEXT, day INTEGER NOT NULL, forums TEXT)");
// Copy the data
            database.execSQL(
                    "INSERT INTO black_person (id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage, day) SELECT id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage, 0 FROM person");

            database.execSQL("DROP TABLE person");

            database.execSQL("ALTER TABLE black_person RENAME TO person");

            database.execSQL("CREATE UNIQUE INDEX `index_person__id` ON `person` (`_id`)");

            database.execSQL("ALTER TABLE kickcounter ADD COLUMN day INTEGER NOT NULL DEFAULT 0");

            database.execSQL("CREATE TABLE `weeklyprogressdata` (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, _id TEXT, `week` INTEGER NOT NULL, `day` INTEGER NOT NULL, `title` TEXT, `intro` TEXT, `body` TEXT)");
            database.execSQL("CREATE UNIQUE INDEX `index_weeklyprogressdata__id` ON `weeklyprogressdata` (`_id`)");

            database.execSQL("ALTER TABLE chat ADD COLUMN createdAt TEXT");

            database.execSQL("ALTER TABLE chat ADD COLUMN updatedAt TEXT");

        }
    };

}
