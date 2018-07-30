package ng.apmis.audreymumplus.ui.Chat;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.ChatSocketService;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class ChatContextFragment extends Fragment {

    @BindView(R.id.send_chat)
    ImageView sendBtn;
    @BindView(R.id.add_chat)
    EditText chatMessageEditText;
    ChatContextAdapter chatContextAdapter;
    RecyclerView chatRecycler;
    String forumName;
    AppCompatActivity activity;
    Person globalPerson;

    ArrayList<ChatContextModel> dbForums;
    ChatViewModel chatViewModel;


    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_context, container, false);
        ButterKnife.bind(this, rootView);
        globalPerson = ((DashboardActivity) getActivity()).globalPerson;
        chatRecycler = rootView.findViewById(R.id.chat_recycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        chatContextAdapter = new ChatContextAdapter(getActivity(), globalPerson.getEmail(), getActivity());
        chatRecycler.setAdapter(chatContextAdapter);

        if (getArguments() != null) {
            forumName = getArguments().getString("forumName");
            activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-foreground").putExtra("forumName", forumName));
        }


        ChatFactory forumFactory = InjectorUtils.provideChatFactory(getActivity(), forumName);
        chatViewModel = ViewModelProviders.of(this, forumFactory).get(ChatViewModel.class);

        chatViewModel.getUpdatedChats().observe(this, chatList -> {
            if (chatList.size() > 0) {
                progressBar.setVisibility(View.GONE);
                dbForums = (ArrayList<ChatContextModel>) chatList;
                /*progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);*/
                chatContextAdapter.setAllChats(chatList);
                chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
            } else {
                //check internet connectivity if true setLoading and emit  get forums
               /* progressBar.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);*/
                progressBar.setVisibility(View.GONE);
            }
        });


        sendBtn.setOnClickListener((view) -> {
            if (chatMessageEditText.getText().toString().length() > 0) {
                ChatContextModel oneChat = new ChatContextModel(forumName, chatMessageEditText.getText().toString(), globalPerson.getEmail(), globalPerson.getFirstName() + " " + globalPerson.getLastName());

                postChat(oneChat);
                chatContextAdapter.addChat(oneChat);
                chatRecycler.scrollToPosition(chatContextAdapter.getItemCount());
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
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("get-chats"));
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-foreground").putExtra("email", globalPerson.getEmail()));
    }


    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-background"));
    }


    public void postChat(ChatContextModel chat) {

        Gson gson = new Gson();
        String cht = gson.toJson(chat);
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-foreground").putExtra("chat", cht));
    }

}
