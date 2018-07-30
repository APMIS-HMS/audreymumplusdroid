package ng.apmis.audreymumplus.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumModel;
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

    private static boolean isForeground = false;

    private static boolean isConnected = false;

    private static String globalEmail;

    private static Socket mSocket;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        mSocket = InjectorUtils.provideSocketInstance();

        if ((intent != null ? intent.getExtras() : null) != null) {
            globalEmail = intent.getExtras().getString("email");
        }

        if (intent.getAction().equals("start-background")) {
            Log.v("socket background start", "background");
            isForeground = false;
            mSocket.on("created", onCreated());
        }

        if (intent.getAction().equals("start-foreground")) {
            Log.v("socket foreground start", "foreground");
            isForeground = true;
            if (!TextUtils.isEmpty(intent.getExtras().getString("chat"))) {
                String cht = intent.getExtras().getString("chat");
                JSONObject newChat;
                try {
                    newChat = new JSONObject(cht);
                    mSocket.emit("chat", newChat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(intent.getExtras().getString("forumName"))) {
                String forumName = intent.getExtras().getString("forumName");
                try {
                    JSONObject getChats = new JSONObject().put("forumName", forumName);
                    mSocket.emit("getChats", getChats);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mSocket.on("created", onCreated());
        }

        if (intent.getAction().equals("get-forums")) {
            Log.v("socket get forums", "forums");
            mSocket.emit("getForums", new JSONObject());

            mSocket.on("getForums", onGetForums());
        }

        if (intent.getAction().equals("get-chats")) {
            Log.v("Get chats", "getting chats");
            mSocket.on("getChats", getChats());
        }

    }

    Emitter.Listener onCreated() {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);

                Log.v("is-user-email ?", String.valueOf(!oneChat.getEmail().equals(globalEmail)));

                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(getApplicationContext()).insertChat(oneChat);
                });

                if (!oneChat.getEmail().equals(globalEmail)) {
                    if (isForeground) {
                        NotificationUtils.buildForegroundChatNotification(this);
                    } else {
                        NotificationUtils.buildBackgroundChatNotification(this, oneChat);
                    }
                }

                mSocket.off("created", onCreated());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    Emitter.Listener onGetForums() {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];

            ArrayList<ChatForumModel> allForums = new ArrayList<>();

            try {
                JSONObject job = new JSONObject(jsonObject.toString());
                JSONArray jar = job.getJSONArray("data");

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject forumObj = (JSONObject) jar.get(i);
                    ChatForumModel eachForum = new Gson().fromJson(forumObj.toString(), ChatForumModel.class);
                    allForums.add(eachForum);
                }
                // checkForumChanges(allForums);
                //TODO Move to check forum changes when it works
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(this).insertAllForums(allForums);
                });

                mSocket.off("getForums", onGetForums());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    Emitter.Listener getChats() {
        return args -> {

            ArrayList<ChatContextModel> allchats = new ArrayList<>();
            try {
                JSONArray jar = (JSONArray) args[0];

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject chatObj = (JSONObject) jar.get(i);
                    ChatContextModel eachChat = new Gson().fromJson(chatObj.toString(), ChatContextModel.class);
                    allchats.add(eachChat);

                }
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(this).insertAllChats(allchats);
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    }
}
