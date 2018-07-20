package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@TypeConverters({JournalConverters.class})
@Database(entities = {JournalModel.class, Person.class, Appointment.class, ChatForumModel.class, ChatContextModel.class}, version = 1, exportSchema = false)
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
                            JournalDatabase.class, JournalDatabase.DATABASE_NAME)/*
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)*/
                            .build();
                }
            }
        }
        return sInstance;
    }

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE black_person ('id' INTEGER PRIMARY KEY NOT NULL, _id TEXT, firstName TEXT, lastName TEXT, email TEXT, personId TEXT, dateOfBirth TEXT, motherMaidenName TEXT, primaryContactPhoneNo TEXT, ExpectedDateOfDelivery TEXT, profileImage TEXT, week TEXT, day INTEGER NOT NULL DEFAULT 0)");
// Copy the data
            database.execSQL(
                    "INSERT INTO black_person (id, _id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, ExpectedDateOfDelivery, profileImage) SELECT id, _id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, ExpectedDateOfDelivery, profileImage FROM person");

// Remove the old table
            database.execSQL("DROP TABLE person");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE black_person RENAME TO person");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE black_person ('id' INTEGER PRIMARY KEY NOT NULL, _id TEXT, firstName TEXT, lastName TEXT, email TEXT, personId TEXT, dateOfBirth TEXT, motherMaidenName TEXT, primaryContactPhoneNo TEXT, ExpectedDateOfDelivery TEXT, profileImage TEXT)");
// Copy the data
            database.execSQL(
                    "INSERT INTO black_person (id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, ExpectedDateOfDelivery, profileImage) SELECT id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage FROM person");

// Remove the old table
            database.execSQL("DROP TABLE person");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE black_person RENAME TO person");

        }
    };

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

    //DATA migration code for changing column fields
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE black_person ('id' INTEGER PRIMARY KEY NOT NULL, _id TEXT, firstName TEXT, lastName TEXT, email TEXT, personId TEXT, dateOfBirth TEXT, motherMaidenName TEXT, primaryContactPhoneNo TEXT, expectedDateOfDelivery TEXT, profileImage TEXT)");
// Copy the data
            database.execSQL(
                    "INSERT INTO black_person (id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage) SELECT id, firstName, lastName, email, personId, dateOfBirth, motherMaidenName, primaryContactPhoneNo, expectedDateOfDelivery, profileImage FROM person");

// Remove the old table
            database.execSQL("DROP TABLE person");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE black_person RENAME TO person");

            // Create the new table
            database.execSQL(
                    "CREATE TABLE new_journal ('id' INTEGER PRIMARY KEY NOT NULL, mood TEXT, cravings TEXT, weight TEXT, symptoms TEXT, babyScanUri TEXT, pregnancyBellyUri TEXT, babyMovement TEXT, date INTEGER NOT NULL, day TEXT, week TEXT)");
// Copy the data
            database.execSQL(
                    "INSERT INTO new_journal (id, mood, cravings, weight, symptoms, babyScanUri, pregnancyBellyUri, babyMovement, date, day) SELECT id, mood, cravings, weight, symptoms, babyScanUri, pregnancyBellyUri, babyMovement, date, day FROM journal");


// Remove the old table
            database.execSQL("DROP TABLE journal");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE new_journal RENAME TO journal");
        }
    };

}
