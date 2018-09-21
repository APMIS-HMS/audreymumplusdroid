package ng.apmis.audreymumplus.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.Calendar;

import ng.apmis.audreymumplus.ui.Appointments.Appointment;

/**
 * Created by Thadeus-APMIS on 8/8/2018.
 */

public class AlarmMangerSingleton {

    private static AlarmMangerSingleton sInstance;
    private static final Object LOCK = new Object();
    private static AlarmManager alarmManager;
    private static Context mContext;

    public AlarmMangerSingleton(Context context) {
        mContext = context;
    }

    public static AlarmMangerSingleton getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AlarmMangerSingleton(mContext);
                alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            }
        }
        return sInstance;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public static void setSingleAppointmentAlarm (Context context, Appointment thisAppointment) {

        Intent alarmIntent = new Intent(context, AlarmBroadcast.class);
        alarmIntent.setAction("appointment");
        alarmIntent.putExtra("appointment", thisAppointment.get_id());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) thisAppointment.get_id(), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager =  new AlarmMangerSingleton(context).getInstance().getAlarmManager();
        alarmManager.set(AlarmManager.RTC_WAKEUP, thisAppointment.getAppointmentTime(), pendingIntent);

    }

    public static void setDailyWeekDayProgress(Context context) {
         /*Set repeat alarm for week update start*/
        Intent alarmIntent = new Intent(context, AlarmBroadcast.class);
        alarmIntent.setAction("update-week");
        Log.d("Redo update-week", "true");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        AlarmManager alarmManager = new AlarmMangerSingleton(context).getInstance().getAlarmManager();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
