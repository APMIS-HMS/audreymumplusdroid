package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyimagegallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class GalleryAdapter extends BaseAdapter {
    List<GalleryModel> galleryModels;
    Context gContext;

    public GalleryAdapter(Context context){
        gContext = context;
        galleryModels = new ArrayList<>();
    }

    void setGalleryModels (List<GalleryModel> images) {
        if (galleryModels != null) {
            galleryModels = new ArrayList<>();
        }
        galleryModels = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return !galleryModels.isEmpty() ? galleryModels.size(): 0;
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

        if (!TextUtils.isEmpty(currentItem.getImageUrl())) {
            Glide.with(gContext).load(Uri.parse(currentItem.getImageUrl())).into(galleryImage);
           /* Glide.with(gContext)
                    .load(Uri.parse(currentItem.getImageUrl())).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (resource != null) {
                        galleryImage.setImageDrawable(resource);
                    }
                }
            });*/
        }

        return convertView;
    }
}
