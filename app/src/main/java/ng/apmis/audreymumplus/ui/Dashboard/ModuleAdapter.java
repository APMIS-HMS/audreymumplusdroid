package ng.apmis.audreymumplus.ui.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class ModuleAdapter extends BaseAdapter {
    List<ModuleModel>data;
    Context mContext;

    public ModuleAdapter(Context context, List<ModuleModel> mdata){
        mContext = context;
        if (data != null) {
            data = new ArrayList<>();
        }
        data = mdata;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
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
                    inflate(R.layout.module_eachitem, parent, false);
        }

        // get current item to be displayed
        ModuleModel currentItem = (ModuleModel) getItem(position);

        // get the TextView for item name and item description
        ImageView listImage = convertView.findViewById(R.id.list_image);
        TextView textViewItemDescription = convertView.findViewById(R.id.list_text);

        //sets the text for item name and item description from the current item object
        listImage.setImageResource(currentItem.getImageIcon());
        textViewItemDescription.setText(currentItem.getTitle());

        // returns the view for the current row
        return convertView;
    }
}
