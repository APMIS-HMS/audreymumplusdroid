package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class ChatForumFragment extends Fragment {

    @BindView(R.id.forums_list)
    ListView chatParentList;
    ArrayList<ChatForumModel> allForums;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_forums, container, false);
        ButterKnife.bind(this, rootView);
        allForums = new ArrayList<>();

        ChatForumAdapter chatForumAdapter = new ChatForumAdapter(getActivity());

        chatParentList.setAdapter(chatForumAdapter);

        allForums.add(new ChatForumModel(R.drawable.audrey_icon, "General", "32", "5"));
        allForums.add(new ChatForumModel(R.drawable.audrey_icon, "Mainland Mums", "132", "2"));
        allForums.add(new ChatForumModel(R.drawable.audrey_icon, "Expectant Mums", "500", "20"));

        chatForumAdapter.setForums(allForums);

        chatParentList.setOnItemClickListener((parent, view, position, id) -> {
            ChatForumModel clicked = (ChatForumModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getForumName() , Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ChatContextFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return rootView;
    }
}
