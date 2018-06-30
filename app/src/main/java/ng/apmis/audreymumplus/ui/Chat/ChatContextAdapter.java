package ng.apmis.audreymumplus.ui.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class ChatContextAdapter extends RecyclerView.Adapter<ChatContextAdapter.ChatContextViewHolder> {

    ArrayList<ChatContextModel> allChats = new ArrayList<>();
    Context mContext;

    ChatContextAdapter(Context context) {
        mContext = context;
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
        if (allChats.get(position).getUsername().equals("me")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatContextViewHolder holder, int position) {
        ChatContextModel currentChat = allChats.get(position);
        if (currentChat != null) {
            holder.chatText.setText(currentChat.getContent());
            holder.userImage.setImageResource(currentChat.getImageUri());
            holder.userName.setText(currentChat.getUsername().toUpperCase());
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
