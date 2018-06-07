package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class PregnancyDetailsAdapter extends BaseAdapter {
    List<PregnancyModel> pregnancyModels;
    Context xcontext;
    public PregnancyDetailsAdapter(Context ncontext, List<PregnancyModel>nlist){
        xcontext = ncontext;
        pregnancyModels = nlist;
    }

    @Override
    public int getCount() {
        return pregnancyModels.size();
    }

    @Override
    public Object getItem(int position) {
        return pregnancyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(xcontext).
                    inflate(R.layout.each_preg, parent, false);
        }

        // get current item to be displayed
        PregnancyModel pregnancyModel = (PregnancyModel) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemDescription = convertView.findViewById(R.id.journal_text);

        //sets the text for item name and item description from the current item object
        textViewItemDescription.setText(pregnancyModel.getEachDay());

        // returns the view for the current row
        return convertView;
    }

}
