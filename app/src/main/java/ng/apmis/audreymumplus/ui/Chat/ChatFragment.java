package ng.apmis.audreymumplus.ui.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;

public class ChatFragment extends Fragment {
    List<ChatModel> chats = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);

        ListView listView = rootView.findViewById(R.id.chat_listn);

        chats.add(new ChatModel(R.drawable.ic_first_square,"Mom_101","Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui.Proin eget tortor risus. Proin.Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui."));
        chats.add(new ChatModel(R.drawable.ic_first_square, "Coder_mom", "Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui. "));
        chats.add(new ChatModel(R.drawable.ic_first_square, "mama_ibeji", "Proin eget tortor risus. Proin eget tortor risus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Vestibulum ac diam sit amet quam vehicula elementum sed sit amet dui."));
        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), chats);

        listView.setAdapter(chatAdapter);
//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ChatModel clicked = (ChatModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getUsername() , Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

}
