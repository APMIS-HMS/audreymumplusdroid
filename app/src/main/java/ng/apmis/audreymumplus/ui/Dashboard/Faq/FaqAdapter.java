package ng.apmis.audreymumplus.ui.Dashboard.Faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class FaqAdapter extends BaseAdapter {
    List<FaqModel> faqdata;
    Context mContext;

    public FaqAdapter(Context context, List<FaqModel>fdata){
        mContext = context;
        faqdata = fdata;
    }

    @Override
    public int getCount() {
        return faqdata.size();
    }
    @Override
    public Object getItem(int position) {
        return faqdata.get(position);
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
                    inflate(R.layout.faq_each, parent, false);
        }

        // get current item to be displayed
        FaqModel currentItem = (FaqModel) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItem = convertView.findViewById(R.id.faq_text);

        //sets the text for item name and item description from the current item object

        textViewItem.setText(currentItem.getTitleText());

        // returns the view for the current row
        return convertView;
    }
}
