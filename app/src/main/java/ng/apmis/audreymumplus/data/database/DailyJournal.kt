package ng.apmis.audreymumplus.data.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Thadeus-APMIS on 5/15/2018.
 */
@Entity(tableName = "journal")
data class DailyJournal (@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")var _id: Long?,
                        /* @ColumnInfo(name = "mood")val mood: String?,*/
                        /* @ColumnInfo(name = "baby_movement")val babyMovement: String?*/
                         @ColumnInfo(name = "weight")val weight: String?,
                         @ColumnInfo(name = "symptoms")val symptoms: String?,
                         /*@ColumnInfo(name = "pill_reminder")val pillReminder: String?,*/
                         @ColumnInfo(name = "cravings")val cravings: String?,
                         @ColumnInfo(name = "notes")val notes: String?,
                         @ColumnInfo(name = "images")val images: String?,
                         @ColumnInfo(name =  "date")val date: Long?)
