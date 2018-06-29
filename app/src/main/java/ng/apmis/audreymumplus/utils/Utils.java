package ng.apmis.audreymumplus.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

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

}
