package ng.apmis.audreymumplus;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.kickcounter.KickCounterFragment;
import ng.apmis.audreymumplus.ui.pills.PillsTypeConverter;
import ng.apmis.audreymumplus.utils.Utils;

import static ng.apmis.audreymumplus.utils.Constants.BASE_URL;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void typeConverterToStringTest () {
        long time = new Date().getTime();
        ArrayList<Long> longArrayList = new ArrayList<>();
        longArrayList.add(time);
        longArrayList.add(time);
        String longString =  PillsTypeConverter.toString(longArrayList);
        assertEquals(String.valueOf(time + "," + time), longString);
    }

    @Test
    public void typeConverterToLongArrayListTest () {
        ArrayList<Long> returnArrayList;

        long time = new Date().getTime();
        long secondTime = new Date().getTime() + 1;

        ArrayList<Long> longArrayList = new ArrayList<>();
        longArrayList.add(time);
        longArrayList.add(secondTime);

        String longString =  PillsTypeConverter.toString(longArrayList);

        returnArrayList = PillsTypeConverter.toLongArrayList(longString);

        assertEquals(longArrayList, returnArrayList);
        assertEquals(longArrayList.get(1), returnArrayList.get(1));
    }

    @Test
    public void testFormatKickCounter () {
        String res = KickCounterFragment.formatDurationTracker(3);
        assertEquals("00:00:03", res);
        String res1 = KickCounterFragment.formatDurationTracker(12);
        assertEquals("00:00:12",res1);
    }

    @Test
    public void testUrlFormat () {
        String url = String.format(BASE_URL + "forum?approved=%1$s", true);
        assertEquals(BASE_URL + "forum?approved=true", url);
    }

    @Test
    public void testListToString () {
        List<ChatContextFragment.ForumNameAndLastDate> list = new ArrayList<>();
        int i = 0;
      /*  while (i < 2) {
            list.add(new ChatContextFragment.ForumNameAndLastDate("Test Forum " + i, new Date()));
            i++;
        }*/
        list.add(new ChatContextFragment.ForumNameAndLastDate("Test Forum " + i, new Date().toString()));
        list.add(new ChatContextFragment.ForumNameAndLastDate("Test Forum " + i, new Date().toString()));

        for (ChatContextFragment.ForumNameAndLastDate j : list) {
            System.out.println(" js" + j.toString());
        }

        String listToString = Utils.convertListToString(list);

        List<ChatContextFragment.ForumNameAndLastDate> kdkdk = Utils.convertStringToList(listToString);
        for (ChatContextFragment.ForumNameAndLastDate x : kdkdk) {
            System.out.println(" xs" +x.toString());
        }

        assertEquals(list.toString(), kdkdk.toString());
    }

}