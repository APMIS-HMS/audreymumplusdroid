package ng.apmis.audreymumplus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Thadeus-APMIS on 8/8/2018.
 */

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("alarm rang", String.valueOf("Alarm rang"));
        Toast.makeText(context.getApplicationContext(), String.valueOf(intent.getExtras().getString("appointment")), Toast.LENGTH_SHORT).show();
    }
}
