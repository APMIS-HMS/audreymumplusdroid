package ng.apmis.audreymumplus.utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Date;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.pills.PillModel;

/**
 * Background task to manage alarm triggers
 */


public class AlarmIntentService extends IntentService {

    private static final String TAG = AlarmIntentService.class.getSimpleName();

    private static final String RESET_ALL_PILL_REMINDER_ALARMS = "set-pills";
    private static final String RESET_ALL_APPOINTMENT_ALARMS = "set-appointments";

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startResettingAllAppointmentAlarm(Context context) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.setAction(RESET_ALL_APPOINTMENT_ALARMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }

    public static void startResettingAllPillReminderAlarm(Context context) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.setAction(RESET_ALL_PILL_REMINDER_ALARMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Check intent type", intent.getType() + " bundle" + intent.getExtras());
        final String action = intent.getAction();
        Log.d(TAG, "Alarms are being rescheduled");
        if (RESET_ALL_APPOINTMENT_ALARMS.equals(action)) {
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                for (Appointment x : InjectorUtils.provideRepository(this).getStaticAppointmentList()) {
                    Log.v("Got list", x.toString());
                    if (new Date(x.getAppointmentTime()).after(new Date())) {
                        AlarmMangerSingleton.setSingleAppointmentAlarm(this, x);
                        Log.v(TAG + " active", x.toString());
                    } else {
                        Log.v(TAG + " passed", x.toString());
                    }
                }
            });

            AlarmMangerSingleton.setDailyWeekDayProgress(this);
        }
        if (RESET_ALL_PILL_REMINDER_ALARMS.equals(action)) {
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                for (PillModel x : InjectorUtils.provideRepository(this).getStaticPillReminderList()) {
                    Log.v(TAG + " pills", x.toString());
                    //TODO check time when date is set on pill object
                    AlarmMangerSingleton.setRepeatingPillReminderAlarm(this, x);
                }
            });
        }
    }

}
