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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.data.network.ChatSocketService;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.InputUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

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

    ChatViewModel chatViewModel;


    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_context, container, false);
        ButterKnife.bind(this, rootView);
        InputUtils.showKeyboard(activity, chatMessageEditText);
        sharedPreferencesManager = new SharedPreferencesManager(activity);
        //globalPerson = ((DashboardActivity) getActivity()).globalPerson;
        if (getArguments() != null) {
            forumName = getArguments().getString("forumName");
            getActivity().startService(new Intent(getContext(), ChatSocketService.class).setAction("start-foreground").putExtra("forumName", forumName));
        }

        chatRecycler = rootView.findViewById(R.id.chat_recycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        InjectorUtils.provideRepository(getActivity()).getPerson().observe(this, person -> {
            if (person != null) {
                globalPerson = person;
                chatContextAdapter = new ChatContextAdapter(getActivity(), person.getEmail(), getActivity());
                chatRecycler.setAdapter(chatContextAdapter);

                ChatFactory forumFactory = InjectorUtils.provideChatFactory(getActivity(), forumName);
                chatViewModel = ViewModelProviders.of(this, forumFactory).get(ChatViewModel.class);

                chatViewModel.getUpdatedChats().observe(this, chatList -> {
                    progressBar.setVisibility(View.GONE);
                    if (chatList.size() > 0) {
                        chatContextAdapter.setAllChats(chatList);
                        chatRecycler.scrollToPosition(chatContextAdapter.getItemCount());
                    }
                });
            }
        });

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
        if (getArguments() != null && getArguments().getString("forumName") != null) {
            ((DashboardActivity) getActivity()).setActionBarButton(true, getArguments().getString("forumName"));
            DashboardActivity.globalOpenChatForum = getArguments().getString("forumName");
        }

        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }


    @Override
    public void onStop() {
        super.onStop();
        DashboardActivity.globalOpenChatForum = null;
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
        activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-background"));
    }

    @Override
    public void onPause() {
        //TODO confirm is new time of last chat is saved
        ChatContextModel chatContextModel = chatContextAdapter.getItem(chatContextAdapter.getItemCount());
        if (chatContextModel != null)
            sharedPreferencesManager.addForumNameAndLastCreatedAtAsStringInPrefs(forumName, chatContextAdapter.getItem(chatContextAdapter.getItemCount()).getCreatedAt(), chatContextAdapter.getItemCount());
        InputUtils.hideKeyboard();
        super.onPause();
    }

    public void postChat(ChatContextModel chat) {
        Gson gson = new Gson();
        String cht = gson.toJson(chat);
        try {
            JSONObject newChat = new JSONObject(cht);
            InjectorUtils.provideJournalNetworkDataSource(getContext()).postChat(newChat, DashboardActivity.globalOpenChatForum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class ForumNameAndLastDate {
        public String forumName;
        public String date;
        public int lastCount;

        public ForumNameAndLastDate() {
        }

        public ForumNameAndLastDate(String forumName, String date, int lastChatPosition) {
            this.forumName = forumName;
            this.date = date;
            this.lastCount = lastCount;
        }

        @Override
        public String toString() {
            return "ForumNameAndLastDate{" +
                    "forumName='" + forumName + '\'' +
                    ", date='" + date + '\'' +
                    ", lastCount=" + lastCount +
                    '}';
        }
    }

}
