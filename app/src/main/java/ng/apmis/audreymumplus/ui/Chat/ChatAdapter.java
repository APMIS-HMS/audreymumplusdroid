package ng.apmis.audreymumplus.ui.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class ChatAdapter extends BaseAdapter {
    List<ChatModel> chatModelList;
    Context mContext;

    public ChatAdapter(Context context, List<ChatModel>chat){
        mContext = context;
        chatModelList = chat;
    }

    @Override
    public int getCount() {
        return chatModelList.size();
    }
    @Override
    public Object getItem(int position) {
        return chatModelList.get(position);
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
                    inflate(R.layout.each_chatforum, parent, false);
        }

        // get current item to be displayed
        ChatModel currentItem = (ChatModel) getItem(position);

        // get the TextView for item name and item description
        ImageView listImage = convertView.findViewById(R.id.chat_avatar);
        TextView textViewItemDescription = convertView.findViewById(R.id.username);
        TextView content = convertView.findViewById(R.id.text_content);
        listImage.setImageResource(currentItem.getImageUri());
        textViewItemDescription.setText(currentItem.getUsername());
        content.setText(currentItem.getContent());

        //sets the text for item name and item description from the current item object
       // listImage.setImageResource(currentItem.getImageIcon());
        //textViewItemDescription.setText(currentItem.getTitle());

        // returns the view for the current row
        return convertView;
    }
}
