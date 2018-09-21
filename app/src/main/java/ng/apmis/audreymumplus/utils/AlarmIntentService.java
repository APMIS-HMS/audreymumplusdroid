package ng.apmis.audreymumplus.utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.Date;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;


public class AlarmIntentService extends IntentService {

    private static final String TAG = AlarmIntentService.class.getSimpleName();

    private static final String RESET_ALL_ALARMS = "set-alarm";
    private static final String ACTION_BAZ = "ng.apmis.audreymumplus.utils.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ng.apmis.audreymumplus.utils.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ng.apmis.audreymumplus.utils.extra.PARAM2";

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startAlarmReset (Context context) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.setAction(RESET_ALL_ALARMS);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, "Alarms are being rescheduled");
            if (RESET_ALL_ALARMS.equals(action)) {
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
        }
    }

}
