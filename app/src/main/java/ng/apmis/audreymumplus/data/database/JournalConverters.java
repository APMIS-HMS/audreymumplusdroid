package ng.apmis.audreymumplus.data.database;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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


    @TypeConverter
    public static List<String> toList(String value) {
        List<String> arrayList = new ArrayList<>();

        if (!TextUtils.isEmpty(value)) {
            String[] allStrings = value.split("---");

            //add strings to list
            arrayList.addAll(Arrays.asList(allStrings));
        }

        return arrayList;
    }

    @TypeConverter
    public static String toString(List<String> values) {
        StringBuilder arrayStringBuilder = new StringBuilder();

        if (values != null && values.size() > 0) {

            for (int i = 0; i<values.size(); ++i){
                String value = values.get(i);
                arrayStringBuilder.append(value);

                if (i < values.size()-1)
                    arrayStringBuilder.append("---");
            }
        }

        return arrayStringBuilder.toString();
    }

}
