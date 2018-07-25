package ng.apmis.audreymumplus.ui.Chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class ChatContextFragment extends Fragment {

    ArrayList<ChatContextModel> chats;
    @BindView(R.id.send_chat)
    ImageView sendBtn;
    @BindView(R.id.add_chat)
    EditText chatMessageEditText;
    Socket mSocket;
    ChatContextAdapter chatContextAdapter;
    RecyclerView chatRecycler;
    String forumName;
    AppCompatActivity activity;
    Person personal;

    MumplusNetworkDataSource dataSource;

    {
        try {
            mSocket = IO.socket("https://audrey-mum.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_context, container, false);
        ButterKnife.bind(this, rootView);
        chats = new ArrayList<>();
        chatRecycler = rootView.findViewById(R.id.chat_list);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        dataSource = InjectorUtils.provideJournalNetworkDataSource(activity);

        InjectorUtils.provideRepository(getContext()).getPerson().observe(this, person -> {
            personal = person;

            getActivity().runOnUiThread(() -> {
                chatContextAdapter = new ChatContextAdapter(getActivity(), person.getEmail(), getActivity());
                chatRecycler.setAdapter(chatContextAdapter);
            });
        });

        mSocket.connect();

        if (getArguments() != null) {
            forumName = getArguments().getString("forumName");

            try {
                JSONObject getChats = new JSONObject().put("forumName", forumName);
                mSocket.emit("getChats", getChats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

     /*   mSocket.on("getChats", args -> {

            try {
                JSONArray jar = (JSONArray) args[0];

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject chatObj = (JSONObject) jar.get(i);
                    ChatContextModel eachChat = new Gson().fromJson(chatObj.toString(), ChatContextModel.class);
                    dataSource.fetchUserName(eachChat.getEmail());
                    dataSource.getPersonEmail().observe(activity, person -> {
                        eachChat.setUserName((person != null ? person.getFirstName() : "") + " " + (person != null ? person.getLastName() : ""));
                    });
                    chats.add(eachChat);
                }

                activity.runOnUiThread(() -> {
                    chatContextAdapter.setAllChats(chats);
                    chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        });*/
     /*   mSocket.on("created", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);
                if (!oneChat.getEmail().equals(personal.getEmail())) {
                    activity.runOnUiThread(() -> {
                        ArrayList<ChatContextModel> chats = new ArrayList<>();
                        MumplusNetworkDataSource dataSource = InjectorUtils.provideJournalNetworkDataSource(activity);
                        dataSource.fetchUserName(oneChat.getEmail());
                        dataSource.getPersonEmail().observe(activity, person -> {
                            oneChat.setUserName((person != null ? person.getFirstName() : "") + " " + (person != null ? person.getLastName() : ""));
                        });
                        chats.add(oneChat);
                        chatContextAdapter.addChat(oneChat);
                        chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
*/

        sendBtn.setOnClickListener((view) -> {
            if (chatMessageEditText.getText().toString().length() > 0) {
                ChatContextModel oneChat = new ChatContextModel("sweet-mothers", chatMessageEditText.getText().toString(), personal.getEmail(), personal.getFirstName() + " " + personal.getLastName());
                ArrayList<ChatContextModel> chats = new ArrayList<>();
                chats.add(oneChat);
                postChat(oneChat);
                chatContextAdapter.addChat(oneChat);
                chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                chatMessageEditText.setText("");
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, getArguments().getString("forumName"));
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
        onDestroy();
    }


    public void postChat(ChatContextModel chat) {

        Gson gson = new Gson();
        String cht = gson.toJson(chat);

        JSONObject boboo;
        try {
            boboo = new JSONObject(cht);
            mSocket.emit("chat", boboo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Emitter.Listener getChats() {
        return args -> {

            try {
                JSONArray jar = (JSONArray) args[0];

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject chatObj = (JSONObject) jar.get(i);
                    ChatContextModel eachChat = new Gson().fromJson(chatObj.toString(), ChatContextModel.class);
                    chats.add(eachChat);
                }

                chatContextAdapter.setAllChats(chats);
                chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    }

    Emitter.Listener getChat() {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            Log.v("JsonObject", String.valueOf(jsonObject));
        };
    }

}
