package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ng.apmis.audreymumplus.R;

public class GalleryAdapter extends BaseAdapter {
    List<GalleryModel> galleryModels;
    Context gContext;

    public GalleryAdapter(Context context, List<GalleryModel>mgal){
        gContext = context;
        galleryModels = mgal;
    }

    @Override
    public int getCount() {
        return galleryModels.size();
    }
    @Override
    public Object getItem(int position) {
        return galleryModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(gContext).
                    inflate(R.layout.each_gallery, parent, false);
        }

        // get current item to be displayed
        GalleryModel currentItem = (GalleryModel) getItem(position);

        // get the TextView for item name and item description
        ImageView galleryImage = convertView.findViewById(R.id.gallery_image);

        //sets the text for item name and item description from the current item object
        galleryImage.setImageResource(currentItem.getImageUrl());
        // returns the view for the current row
        return convertView;
    }
}
