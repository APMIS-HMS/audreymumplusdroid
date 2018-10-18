package ng.apmis.audreymumplus.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;

/**
 * Created by Thadeus on 6/12/2018.
 */

public class Utils {

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("week.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static String convertListToString (List<ChatContextFragment.ForumNameAndLastDate> forumNameAndLastDateList) {

        Gson gson = new Gson();
        return gson.toJson(forumNameAndLastDateList);

    }

    public static List<ChatContextFragment.ForumNameAndLastDate> convertStringToList (String convertedList) {
        if (convertedList.equals("")) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(new Gson().fromJson(convertedList, ChatContextFragment.ForumNameAndLastDate[].class)));
        }
    }

    public static String localDateToDbString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String dateString;

        dateString = format.format(date);

        return dateString;
    }

}
