package ng.apmis.audreymumplus.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.NotificationUtils;

/**
 * Created by Thadeus-APMIS on 7/26/2018.
 * Handles and set up chat socket listeners in the background
 */

public class ChatSocketService extends IntentService {

    private static final String name = "chat-socket-service";

    public ChatSocketService() {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Socket mSocket = InjectorUtils.provideSocketInstance();

        //Listen on all forum events
        if (intent.getExtras() != null && intent.hasExtra("forums")) {

            if (!mSocket.connected()) {
                List<String> forums = intent.getExtras().getStringArrayList("forums");

                //When any forum event occurs, get the chat messages on it via REST
                /**
                 * Set up listeners for the forums a person is joined to and appears in the forums array
                 */
                for (String forum : forums) {
                    Log.v("ForumEmit", "Listening to " + forum);

                    mSocket.on(forum, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.v("ForumEmit", "Listened on " + forum);
                            InjectorUtils.provideJournalNetworkDataSource(getApplicationContext())
                                    .getChat(forum);
                        }
                    });
                }
            }
        }
    }

}
