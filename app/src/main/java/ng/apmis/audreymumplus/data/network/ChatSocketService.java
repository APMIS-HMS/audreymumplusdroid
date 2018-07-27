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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Socket socket = InjectorUtils.provideSocketInstance();
        if (intent.getAction().equals("start-service")) {
            Log.v("socket service started", "Started");
            socket.connect();
            socket.on("created", onCreated());
        } else {
            Log.v("socket service ended", "Started");
            socket.disconnect();
        }
    }

    Emitter.Listener onCreated() {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(getApplicationContext()).insertChat(oneChat);
                });

                NotificationUtils.buildBackgroundChatNotification(this, oneChat.getForumName(), getString(R.string.notification_body, oneChat.getEmail(), oneChat.getMessage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}
