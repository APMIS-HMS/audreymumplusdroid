package ng.apmis.audreymumplus.ui.Dashboard.Journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class JournalAdapter extends BaseAdapter {
    List<JournalModel> modelList;
    Context mCon;
    public JournalAdapter(Context context, List<JournalModel>list){
        mCon = context;
        modelList = list;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mCon).
                    inflate(R.layout.my_journal, parent, false);
        }

        // get current item to be displayed
        JournalModel journalModel = (JournalModel) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemDescription = convertView.findViewById(R.id.journal_text);

        //sets the text for item name and item description from the current item object
        textViewItemDescription.setText(journalModel.getTitle());

        // returns the view for the current row
        return convertView;
    }
}
