package ng.apmis.audreymumplus;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import ng.apmis.audreymumplus.ui.kickcounter.KickCounterFragment;
import ng.apmis.audreymumplus.ui.pills.PillsTypeConverter;

import static ng.apmis.audreymumplus.utils.Constants.BASE_URL;
import static org.junit.Assert.assertEquals;

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

}