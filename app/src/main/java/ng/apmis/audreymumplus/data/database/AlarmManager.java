package ng.apmis.audreymumplus.data.database;

import android.content.Context;

public class AlarmManager {
    private static AlarmManager sAlarmManager;



    public static AlarmManager getAlarmManagerInstance(Context sContext ) {
        if (sAlarmManager == null && sContext != null)
            //sContext = sContext.getApplicationContext();
            sAlarmManager = (AlarmManager)sContext.getSystemService(sContext.getPackageName());
        return sAlarmManager;
    }
}
