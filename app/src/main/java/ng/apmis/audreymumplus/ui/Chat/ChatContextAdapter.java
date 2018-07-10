package ng.apmis.audreymumplus.ui.Chat;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContextAdapter extends RecyclerView.Adapter<ChatContextAdapter.ChatContextViewHolder> {

    private ArrayList<ChatContextModel> allChats = new ArrayList<>();
    private Context mContext;
    private String mEmail;
    private FragmentActivity owner;

    ChatContextAdapter(Context context, String email, FragmentActivity frag) {
        mContext = context;
        mEmail = email;
        owner = frag;
    }

    public void setAllChats (ArrayList<ChatContextModel> allChats) {
        this.allChats = allChats;
        notifyDataSetChanged();
    }

    public void addChats (ArrayList<ChatContextModel> newMessages) {
        this.allChats.addAll(newMessages);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatContextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_item_right, parent, false);
                return new ChatContextViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_item_left, parent, false);
                return new ChatContextViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (allChats.get(position).getEmail().equals(mEmail)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatContextViewHolder holder, int position) {
        ChatContextModel currentChat = allChats.get(position);
        if (currentChat != null) {
            holder.chatText.setText(currentChat.getMessage());
            MumplusNetworkDataSource dataSource = InjectorUtils.provideJournalNetworkDataSource(mContext);
            dataSource.fetchUserName(currentChat.getEmail());
            dataSource.getPersonEmail().observe(owner, person -> {
                holder.userName.setText(person.getFirstName());
                //holder.userImage.setImageResource(person.getImageUri());
            });
        }
    }

    @Override
    public int getItemCount() {
        return allChats.size();
    }

    class ChatContextViewHolder extends RecyclerView.ViewHolder {

        TextView chatText;
        ImageView userImage;
        TextView userName;

        ChatContextViewHolder(View itemView) {
            super(itemView);
            chatText = itemView.findViewById(R.id.chat_message);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.username);

        }
    }
}
