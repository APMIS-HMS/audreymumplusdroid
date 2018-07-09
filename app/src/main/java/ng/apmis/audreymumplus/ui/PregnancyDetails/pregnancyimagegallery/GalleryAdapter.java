package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyimagegallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<GalleryModel> galleryModels;
    private Context gContext;

    GalleryAdapter(Context context){
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
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(gContext)
                .inflate(R.layout.each_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        GalleryModel currentItem = galleryModels.get(position);

        if (!TextUtils.isEmpty(currentItem.getImageUrl())) {
            Glide.with(gContext).load(Uri.parse(currentItem.getImageUrl())).into(holder.galleryImage);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return !galleryModels.isEmpty() ? galleryModels.size(): 0;
    }


    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView galleryImage;
        ImageView shareButton;

        GalleryViewHolder(View itemView) {
            super(itemView);
            galleryImage = itemView.findViewById(R.id.gallery_image);
            shareButton = itemView.findViewById(R.id.share_image);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(galleryModels.get(getAdapterPosition()).getImageUrl()));

            try {
                gContext.startActivity(Intent.createChooser(shareIntent, "Audrey Share"));
            } catch (android.content.ActivityNotFoundException ex) {

                ex.printStackTrace();
            }
        }
    }

}
