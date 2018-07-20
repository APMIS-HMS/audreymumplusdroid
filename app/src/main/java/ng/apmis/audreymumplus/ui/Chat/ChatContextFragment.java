package ng.apmis.audreymumplus.ui.Chat;

import android.app.ProgressDialog;
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
import ng.apmis.audreymumplus.AudreyMumplus;
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
        mSocket.connect();
        dataSource = InjectorUtils.provideJournalNetworkDataSource(activity);

        chats = new ArrayList<>();
        chatRecycler = rootView.findViewById(R.id.chat_list);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        ((DashboardActivity)getActivity()).getPersonLive().observe(activity, person -> {
            personal = person;
            activity.runOnUiThread(() -> {
                chatContextAdapter = new ChatContextAdapter(getActivity(), person.getEmail(), getActivity());
                chatRecycler.setAdapter(chatContextAdapter);
            });
        });


        if (getArguments() != null) {
            forumName = getArguments().getString("forumName");

            //Get saved chats from database
            AudreyMumplus.getInstance().diskIO().execute(() -> {
                InjectorUtils.provideRepository(activity).getUpdatedChats().observe(activity, chats -> {
                    if (chats != null) {
                        activity.runOnUiThread(() -> {
                            Log.v("chats", String.valueOf(chats));
                            chatContextAdapter.swapChats(chats);
                        });
                    }
                });
            });

            try {
                JSONObject getChats = new JSONObject().put("forumName", forumName);
                mSocket.emit("getChats", getChats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        mSocket.on("getChats", args -> {

            try {
                JSONArray jar = (JSONArray) args[0];

                for (int i = 0; i < jar.length(); i++) {
                    JSONObject chatObj = (JSONObject) jar.get(i);
                    ChatContextModel eachChat = new Gson().fromJson(chatObj.toString(), ChatContextModel.class);
                    Log.v("each chat", String.valueOf(eachChat));
                    dataSource.fetchUserName(eachChat.getEmail());
                    dataSource.getPersonEmail().observe(activity, person -> {
                        eachChat.setUserName((person != null ? person.getFirstName() : "") + " " + (person != null ? person.getLastName() : ""));
                    });
                    chats.add(eachChat);
                }

                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertAllChats(chats);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        mSocket.on("created", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertChat(oneChat);
                });

                if (!oneChat.getEmail().equals(personal.getEmail())) {
                    activity.runOnUiThread(() -> {
                        ArrayList<ChatContextModel> chats = new ArrayList<>();
                        MumplusNetworkDataSource dataSource = InjectorUtils.provideJournalNetworkDataSource(activity);
                        dataSource.fetchUserName(oneChat.getEmail());
                        dataSource.getPersonEmail().observe(activity, person -> {
                            oneChat.setUserName((person != null ? person.getFirstName() : "") + " " + (person != null ? person.getLastName() : ""));
                        });
                        chats.add(oneChat);
                        chatContextAdapter.addChats(chats);
                        chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        sendBtn.setOnClickListener((view) -> {
            if (chatMessageEditText.getText().toString().length() > 0) {
                ChatContextModel oneChat = new ChatContextModel(forumName, chatMessageEditText.getText().toString(), personal.getEmail(), "");
                ArrayList<ChatContextModel> chats = new ArrayList<>();
                chats.add(oneChat);
                postChat(oneChat);
                chatContextAdapter.addChats(chats);
                chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                chatMessageEditText.setText("");
            }
        });


        return rootView;
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
    }

}
