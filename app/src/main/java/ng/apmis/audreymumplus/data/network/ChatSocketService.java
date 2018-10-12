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
 */

public class ChatSocketService extends IntentService {

    private static final String name = "chat-socket-service";

    public ChatSocketService() {
        super(name);
    }

    private static boolean isForeground = false;

    private static boolean isConnected = false;

    private static String globalEmail;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Socket mSocket = InjectorUtils.provideSocketInstance();
        mSocket.connect();

        //Listen on all forum events
        if (intent.getExtras() != null && intent.hasExtra("forums")) {

            if (!mSocket.connected()) {
                List<String> forums = intent.getExtras().getStringArrayList("forums");

                //When any forum event occurs, get the chat messages on it via REST
                for (String forum : forums){
                    Log.v("ForumEmit", "Listening to "+forum);

                    mSocket.on(forum, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.v("ForumEmit", "Listened on "+forum);
                            InjectorUtils.provideJournalNetworkDataSource(getApplicationContext())
                                    .getChat(getApplicationContext(), forum);
                        }
                    });
                }
            }
        }


//        if (intent.getExtras() != null && !TextUtils.isEmpty(intent.getExtras().getString("email"))) {
//            if (!mSocket.connected()) {
//                globalEmail = intent.getExtras().getString("email");
//                //mSocket.on("created", onCreated());
//                mSocket.on("getForums", onGetForums());
//                mSocket.on("getChats", getChats());
//            }
//        }

//        if (intent.getAction().equals("start-background")) {
//            Log.d("socket background start", "background");
//            isForeground = false;
//        }
//
//
//        if (intent.getAction().equals("start-foreground")) {
//            Log.d("socket foreground start", "foreground");
//            isForeground = true;
//            if (!TextUtils.isEmpty(intent.getExtras().getString("chat"))) {
//                String cht = intent.getExtras().getString("chat");
//                JSONObject newChat;
//                try {
//                    newChat = new JSONObject(cht);
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (!TextUtils.isEmpty(intent.getExtras().getString("forumName"))) {
//                String forumName = intent.getExtras().getString("forumName");
//                try {
//                    JSONObject getChats = new JSONObject().put("forumName", forumName);
//
//                    mSocket.emit("getChats", getChats);
//
//                    Log.d("socket", getChats+" chats were emitted");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (intent.getAction().equals("get-forums")) {
//            mSocket.emit("getForums", new JSONObject());
//        }

    }

    Emitter.Listener onCreated() {
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Socket", "Notification should be here");
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);

                    AudreyMumplus.getInstance().diskIO().execute(() -> {
                        InjectorUtils.provideRepository(ChatSocketService.this.getApplicationContext()).insertChat(oneChat);
                    });

                    if (!oneChat.getEmail().equals(globalEmail)) {
                        if (isForeground) {
                            NotificationUtils.buildForegroundChatNotification(ChatSocketService.this);
                        } else {
                            NotificationUtils.buildBackgroundChatNotification(ChatSocketService.this, oneChat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    Log.v("forum obj", forumObj.toString());
                }

                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(this).insertAllForums(allForums);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    Emitter.Listener getChats() {
        return args -> {

            Log.d("Socket", "All chats should be received here");

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
