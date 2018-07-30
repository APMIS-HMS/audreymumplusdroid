package ng.apmis.audreymumplus.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.NotificationUtils;

/**
 * Created by Thadeus-APMIS on 7/26/2018.
 */

public class ChatSocketService extends IntentService {

    private static final String name = "chat-socket-service";

    public ChatSocketService() {
        super(name);
    }

    private static boolean isStarted = false;

    private static String globalEmail;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Socket socket = InjectorUtils.provideSocketInstance();

        if ((intent != null ? intent.getExtras() : null) != null) {
            globalEmail = intent.getExtras().getString("email");
            Log.v("email in service", globalEmail);
        }

        if (intent.getAction().equals("start-service")) {
            if (!isStarted) {
                Log.v("socket service started", "Started");
                socket.connect();
                socket.on("created", onCreated());
                isStarted = true;
            }
        }

        if (intent.getAction().equals("stop-service")) {
            Log.v("socket service ended", "Ended");
            socket.close();
            isStarted = false;
        }
    }

    Emitter.Listener onCreated() {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);

                Log.v("is-user-email ?", String.valueOf(!oneChat.getEmail().equals(globalEmail)));

                if (!oneChat.getEmail().equals(globalEmail)) {
                    AudreyMumplus.getInstance().diskIO().execute(() -> {
                        InjectorUtils.provideRepository(getApplicationContext()).insertChat(oneChat);
                    });
                    NotificationUtils.buildBackgroundChatNotification(this, oneChat);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}
