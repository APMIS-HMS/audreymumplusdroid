package ng.apmis.audreymumplus.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;

import java.util.Date;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Appointments.Appointment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.pills.PillModel;

/**
 * Created by Thadeus-APMIS on 7/26/2018.
 */

public class NotificationUtils {

    private static final String CHANNEL_ID = "chat-notification";
    private static final int NOTIFICATION_ID = 5000;


    public static void buildBackgroundChatNotification (Context mContext, ChatContextModel oneChat) {

        String notificationTitle = oneChat.getForumName();
        String notificationBody = mContext.getString(R.string.notification_body, oneChat.getEmail(), oneChat.getMessage());

        Intent intent = new Intent(mContext.getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("forumName", oneChat.getForumName());

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.audrey_icon)
                .setContentTitle(notificationTitle)
                .setContentText(Html.fromHtml(notificationBody))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(notificationBody)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(new Date().getTime());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void buildAppointmentNotification (Context mContext, Appointment appointment) {

        String notificationTitle = appointment.getTitle();
        String notificationBody = mContext.getString(R.string.notification_body, appointment.getAppointmentDetails(), appointment.getAppointmentAddress());

        Intent intent = new Intent(mContext.getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.audrey_icon)
                .setContentTitle(Html.fromHtml("<b>Reminder for </b> " +notificationTitle))
                .setContentText(Html.fromHtml(notificationBody))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(notificationBody)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setWhen(new Date().getTime());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void buildPillReminderNotification (Context mContext, PillModel pillModel) {

        String notificationTitle = pillModel.getPillName();
        String notificationBody = mContext.getString(R.string.notification_body_pills, pillModel.getInstruction(), pillModel.getQtyPerTime());

        Intent intent = new Intent(mContext.getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.audrey_icon)
                .setContentTitle(Html.fromHtml("<b>Take </b> " +notificationTitle))
                .setContentText(Html.fromHtml(notificationBody))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(notificationBody)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setWhen(new Date().getTime());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void buildForegroundChatNotification (Context mContext) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, defaultSoundUri);

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer thePlayer = MediaPlayer.create(mContext.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        try {
            thePlayer.setVolume(((float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0)),
                    (float) (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) / 7.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (thePlayer.isPlaying()) {
            thePlayer.stop();
            thePlayer.release();
        }
        thePlayer.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context mContext) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
            CharSequence name = mContext.getString(R.string.channel_name);
            String description = mContext.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

    }

}
