package ng.apmis.audreymumplus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.pills.PillModel;

/**
 * Created by Thadeus-APMIS on 8/8/2018.
 */

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmIntentService.startResettingAllAppointmentAlarm(context);
            AlarmIntentService.startResettingAllPillReminderAlarm(context);
        }

        if (intent.getAction().equals("update-week")) {

            AudreyMumplus.getInstance().diskIO().execute(() -> {
                Log.v("update", "Started");
                Person person = InjectorUtils.provideRepository(context).getStaticPerson();
                InjectorUtils.provideRepository(context).getDayWeek(person);
            });
        }
        if (intent.getAction().equals("appointment")) {
            long appointment_id = intent.getExtras().getLong("appointment");
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                Appointment appointment = InjectorUtils.provideRepository(context).getStaticAppointment(appointment_id);
                 NotificationUtils.buildAppointmentNotification(context, appointment);
            });
        }

        if (intent.getAction().equals("pillreminder")) {
            long pillReminderId = intent.getExtras().getLong("pillreminder");
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                PillModel pillModel = InjectorUtils.provideRepository(context).getPillModel(pillReminderId);
                NotificationUtils.buildPillReminderNotification(context, pillModel);
            });
        }
    }
}
