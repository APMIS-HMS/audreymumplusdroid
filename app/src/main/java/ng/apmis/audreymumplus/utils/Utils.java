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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        /*String[] array = new String[forumNameAndLastDateList.size()];
        int index = 0;
        for (ChatContextFragment.ForumNameAndLastDate value : forumNameAndLastDateList) {
            array[index] = value.toString();
            index++;
        }*/
        Gson gson = new Gson();
        return gson.toJson(forumNameAndLastDateList);
        //return Arrays.toString(array);
    }

    public static List<ChatContextFragment.ForumNameAndLastDate> convertStringToList (String convertedList) {
        List<ChatContextFragment.ForumNameAndLastDate> unConvertList = null;

        unConvertList = Arrays.asList(new Gson().fromJson(convertedList, ChatContextFragment.ForumNameAndLastDate[].class));
        return unConvertList;
    }

}
