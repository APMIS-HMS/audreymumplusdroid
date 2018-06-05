package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;

public class MyGalleryFragment extends android.support.v4.app.Fragment {
    private static final String CLASSNAME = "HOME";

    List<GalleryModel> galleryModelList = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);

        GridView gridView = rootView.findViewById(R.id.list_gallery);

        galleryModelList.add(new GalleryModel(R.drawable.ic_first_square));
        galleryModelList.add(new GalleryModel(R.drawable.ic_first_square));
        galleryModelList.add(new GalleryModel(R.drawable.ic_first_square));
        galleryModelList.add(new GalleryModel(R.drawable.ic_first_square));
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity(), galleryModelList);

        gridView.setAdapter(galleryAdapter);
//        gridView.setColumnWidth(1);

        //gridItems.setDivider(null);


        /*gridView.setOnItemClickListener((parent, view, position, id) -> {
            GalleryModel clicked = (ModuleModel) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(), clicked.getTitle() , Toast.LENGTH_SHORT).show();
            *//*if(position == 2){
                Intent i = new Intent(getActivity(), PregnancyDetailsActivity.class);
                startActivity(i);
            }*//*
        });
*/
        return rootView;
    }


}
