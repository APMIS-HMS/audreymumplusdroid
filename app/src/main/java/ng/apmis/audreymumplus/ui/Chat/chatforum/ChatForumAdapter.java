package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class ChatForumAdapter extends BaseAdapter {
    List<ChatForumModel> chatForums;
    Context mContext;

    public ChatForumAdapter(Context context){
        mContext = context;
        chatForums = new ArrayList<>();
    }

    public void setForums (List<ChatForumModel> forums) {
        chatForums = forums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatForums.size();
    }
    @Override
    public Object getItem(int position) {
        return chatForums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.chat_forum_item, parent, false);
        }

        // get current item to be displayed
        ChatForumModel currentItem = (ChatForumModel) getItem(position);

        // get the TextView for item name and item description
        ImageView forumImage = convertView.findViewById(R.id.forum_icon);
        TextView forumName = convertView.findViewById(R.id.forum_topic);
        TextView forumMemberCount = convertView.findViewById(R.id.forum_member_count);
        TextView forumNewMessageCounter = convertView.findViewById(R.id.forum_new_message_counter);

        if (currentItem.getForumIcon() == 0) {
            Glide.with(mContext).load(currentItem.getForumIcon()).into(forumImage);
        }
        forumName.setText(currentItem.getName());
        forumNewMessageCounter.setText(currentItem.getForumNewChatsCount());
        forumMemberCount.setText(mContext.getString(R.string.forum_member_counter,currentItem.getForumMemberCount()));

        // returns the view for the current row
        return convertView;
    }
}
