package ng.apmis.audreymumplus.ui.Chat.chatforum;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    List<ChatForumModel> chatForums;
    Context mContext;
    public ClickForumListener clickForumListener;

    public ForumAdapter(Context context, ClickForumListener listener) {
        mContext = context;
        chatForums = new ArrayList<>();
        clickForumListener = listener;
    }

    public void setForums(List<ChatForumModel> forums) {
        chatForums = forums;
        notifyDataSetChanged();
    }

    void swapForums (final List<ChatForumModel> newChatForums) {
        // If there was no forecast data, then recreate all of the list
        Log.v("swap forums", String.valueOf(newChatForums));
        if (chatForums == null) {
            chatForums = newChatForums;
            notifyDataSetChanged();
        } else {
            /*
             * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mForecast, where the new list is the new
             * values passed in from the observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return chatForums.size();
                }

                @Override
                public int getNewListSize() {
                    return newChatForums.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return chatForums.get(oldItemPosition).get_id().equals(newChatForums.get(newItemPosition).get_id());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ChatForumModel newWeather = newChatForums.get(newItemPosition);
                    ChatForumModel oldWeather = chatForums.get(oldItemPosition);
                    return newWeather.get_id().equals(oldWeather.get_id());
                }
            });
            chatForums = newChatForums;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ForumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_forum_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {

        ChatForumModel modelOne = chatForums.get(position);

        holder.forumName.setText(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, modelOne.getName()));
        holder.forumNewMessageCounter.setText(modelOne.getForumNewChatsCount());
        holder.forumMemberCount.setText(mContext.getString(R.string.forum_member_counter, modelOne.getForumMemberCount()));

    }

    @Override
    public int getItemCount() {
        return chatForums.size();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView forumImage;
        TextView forumName;
        TextView forumMemberCount;
        TextView forumNewMessageCounter;

        public ForumViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            forumImage = itemView.findViewById(R.id.forum_icon);
            forumName = itemView.findViewById(R.id.forum_topic);
            forumMemberCount = itemView.findViewById(R.id.forum_member_count);
            forumNewMessageCounter = itemView.findViewById(R.id.forum_new_message_counter);
        }

        @Override
        public void onClick(View view) {
            clickForumListener.onForumClick(chatForums.get(getAdapterPosition()));
        }
    }

    interface ClickForumListener {
        void onForumClick (ChatForumModel chatForums);
    }


}
