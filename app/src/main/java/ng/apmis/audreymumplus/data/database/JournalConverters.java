package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Thadeus-APMIS on 5/17/2018.
 */

public class JournalConverters {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }

}
