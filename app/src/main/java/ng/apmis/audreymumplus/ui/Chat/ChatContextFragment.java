package ng.apmis.audreymumplus.ui.Chat;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

                chatViewModel.getUpdatedChats().observeForever(chatList -> {
                    progressBar.setVisibility(View.GONE);
                    if ((chatList != null ? chatList.size() : 0) > 0) {
                        dbForums = (ArrayList<ChatContextModel>) chatList;
                /*progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);*/
                        chatContextAdapter.setAllChats(chatList);
                        chatRecycler.scrollToPosition(chatList.size() -1);
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

       // hideSoftInput(getActivity());
        touchScreenAndHideKeyboardOnFocus(rootView, getActivity());

        return rootView;
    }

    public static void touchScreenAndHideKeyboardOnFocus(View view, final Activity activity) {

        if (view instanceof EditText) {
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if(activity != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (activity.getCurrentFocus() != null) {
                                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    }
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                touchScreenAndHideKeyboardOnFocus(innerView, activity);
            }
        }
    }

    public static void hideSoftInput(FragmentActivity activity) {
        try {
            if (activity == null || activity.isFinishing()) return;
            Window window = activity.getWindow();
            if (window == null) return;
            View view = window.getCurrentFocus();
            //give decorView a chance
            if (view == null) view = window.getDecorView();
            if (view == null) return;

            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null || !imm.isActive()) return;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Throwable e) {
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


    public void postChat(ChatContextModel chat) {
        Gson gson = new Gson();
        String cht = gson.toJson(chat);
        try {
            JSONObject newChat = new JSONObject(cht);
            InjectorUtils.provideJournalNetworkDataSource(getContext()).postChat(newChat, DashboardActivity.globalOpenChatForum);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //activity.startService(new Intent(getContext(), ChatSocketService.class).setAction("start-foreground").putExtra("chat", cht));
    }

}
