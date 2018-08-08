package ng.apmis.audreymumplus.utils;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by Thadeus-APMIS on 8/8/2018.
 */

public class AlarmMangerSingleton {

    private static AlarmMangerSingleton sInstance;
    private static final Object LOCK = new Object();
    private static AlarmManager alarmManager;
    private static Context mContext;

    public AlarmMangerSingleton (Context context) {
        mContext = context;
    }

    public static AlarmMangerSingleton getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance  = new AlarmMangerSingleton(mContext);
                alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            }
        }
        return sInstance;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }
}
