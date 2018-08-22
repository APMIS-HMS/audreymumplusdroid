package ng.apmis.audreymumplus.ui.pills;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Thadeus-APMIS on 8/21/2018.
 */

public class PillsTypeConverter {

    @TypeConverter
    public static ArrayList<Long> toLongArrayList(String value) {
        if (!TextUtils.isEmpty(value)) {
            ArrayList<Long> returnLongList = new ArrayList<>();
            String[] allLongString = value.split(",");

            for (String anAllLongString : allLongString) {
                returnLongList.add(Long.parseLong(anAllLongString));
            }
            return returnLongList;
        }
        return new ArrayList<>();
    }

    @TypeConverter
    public static String toString(ArrayList<Long> pillTimes) {
        if (pillTimes.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (long x : pillTimes) {
                stringBuilder.append(String.valueOf(x + ","));
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return TextUtils.isEmpty(stringBuilder.toString()) ? null : stringBuilder.toString();
        }
        return "";
    }

}
