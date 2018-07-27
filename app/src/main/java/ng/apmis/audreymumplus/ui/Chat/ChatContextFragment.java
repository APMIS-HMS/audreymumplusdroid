package ng.apmis.audreymumplus.ui.Chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.ChatSocketService;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.NotificationUtils;

public class ChatContextFragment extends Fragment {

    @BindView(R.id.send_chat)
    ImageView sendBtn;
    @BindView(R.id.add_chat)
    EditText chatMessageEditText;
    Socket mSocket;
    ChatContextAdapter chatContextAdapter;
    RecyclerView chatRecycler;
    String forumName;
    AppCompatActivity activity;
    Person globalPerson;

    ArrayList<ChatContextModel> dbForums;
    ArrayList<ChatContextModel> allchats;
    ChatViewModel chatViewModel;


    @BindView(R.id.progress_bar)
    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_context, container, false);
        ButterKnife.bind(this, rootView);
        mSocket = InjectorUtils.provideSocketInstance();
        globalPerson = ((DashboardActivity) getActivity()).globalPerson;
        allchats = new ArrayList<>();
        chatRecycler = rootView.findViewById(R.id.chat_recycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        chatContextAdapter = new ChatContextAdapter(getActivity(), globalPerson.getEmail(), getActivity());
        chatRecycler.setAdapter(chatContextAdapter);


        if (getArguments() != null) {
            forumName = getArguments().getString("forumName");

            try {
                JSONObject getChats = new JSONObject().put("forumName", forumName);
                mSocket.emit("getChats", getChats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mSocket.on("getChats", getChats());

        ChatFactory forumFactory = InjectorUtils.provideChatFactory(getActivity(), forumName);
        chatViewModel = ViewModelProviders.of(this, forumFactory).get(ChatViewModel.class);

        chatViewModel.getUpdatedChats().observeForever(chatList -> {
            if (chatList.size() > 0) {
                progressBar.setVisibility(View.GONE);
                dbForums = (ArrayList<ChatContextModel>) chatList;
                /*progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);*/
                chatContextAdapter.setAllChats(chatList);
            } else {
                //check internet connectivity if true setLoading and emit  get forums
               /* progressBar.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);*/
                progressBar.setVisibility(View.GONE);
            }
        });

        mSocket.on("created", onCreated());

        sendBtn.setOnClickListener((view) -> {
            if (chatMessageEditText.getText().toString().length() > 0) {
                ChatContextModel oneChat = new ChatContextModel(forumName, chatMessageEditText.getText().toString(), globalPerson.getEmail(), globalPerson.getFirstName() + " " + globalPerson.getLastName());

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
        mSocket.connect();
        activity.stopService(new Intent(getContext(), ChatSocketService.class).setAction("stop-service"));
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
        mSocket.disconnect();
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-service"));
    }


    public void postChat(ChatContextModel chat) {

        Gson gson = new Gson();
        String cht = gson.toJson(chat);

        JSONObject newChat;
        try {
            newChat = new JSONObject(cht);
            mSocket.emit("chat", newChat);
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
                    allchats.add(eachChat);

                }
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(activity).insertAllChats(allchats);
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
    }

    Emitter.Listener onCreated () {
        return args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                ChatContextModel oneChat = new Gson().fromJson(jsonObject.getJSONObject("message").toString(), ChatContextModel.class);

                if (!oneChat.getEmail().equals(globalPerson.getEmail())) {
                    AudreyMumplus.getInstance().networkIO().execute(() -> {
                        MumplusNetworkDataSource dataSource = InjectorUtils.provideJournalNetworkDataSource(activity);
                        dataSource.fetchUserName(oneChat.getEmail());
                        dataSource.getPersonEmail().observe(activity, person -> {
                            oneChat.setUserName((person != null ? person.getFirstName() : "") + " " + (person != null ? person.getLastName() : ""));
                            AudreyMumplus.getInstance().diskIO().execute(() -> {
                                InjectorUtils.provideRepository(activity).insertChat(oneChat);
                            });
                        });
                    });
                    NotificationUtils.buildForegroundChatNotification(getContext());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }
}
