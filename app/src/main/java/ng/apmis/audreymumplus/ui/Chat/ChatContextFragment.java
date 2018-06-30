package ng.apmis.audreymumplus.ui.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Chat.chatforum.ChatForumAdapter;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

public class ChatContextFragment extends Fragment {

    ArrayList<ChatContextModel> chats;
    @BindView(R.id.send_chat)
    ImageView sendBtn;
    @BindView(R.id.add_chat)
    EditText chatMessageEditText;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_context, container, false);
        ButterKnife.bind(this, rootView);
        chats = new ArrayList<>();
        RecyclerView chatRecycler = rootView.findViewById(R.id.chat_list);
        ChatContextAdapter chatContextAdapter = new ChatContextAdapter(getActivity());

        chats.add(new ChatContextModel("121d", R.drawable.ic_first_square,"Mom_101","Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui.Proin eget tortor risus. Proin.Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui."));
        chats.add(new ChatContextModel("132a",R.drawable.face_of_audrey, "me", "Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui. "));
        chats.add(new ChatContextModel("747d", R.drawable.ic_first_square, "mama_ibeji", "Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui."));
        chats.add(new ChatContextModel("747d", R.drawable.face_of_audrey, "me", "Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui."));

        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        chatRecycler.setAdapter(chatContextAdapter);

        chatContextAdapter.setAllChats(chats);

        chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());

        sendBtn.setOnClickListener((view) -> {
            if (chatMessageEditText.getText().toString().length() > 0) {
                ChatContextModel oneChat = new ChatContextModel("343d", R.drawable.face_of_audrey, "me", chatMessageEditText.getText().toString());
                ArrayList<ChatContextModel> chats = new ArrayList<>();
                chats.add(oneChat);
                chatContextAdapter.addChats(chats);
                chatRecycler.smoothScrollToPosition(chatContextAdapter.getItemCount());
                chatMessageEditText.setText("");
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(false, "Chat ChatForumModel");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
    }

}
