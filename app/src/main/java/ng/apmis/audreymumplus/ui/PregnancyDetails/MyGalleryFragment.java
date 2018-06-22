package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Journal.JournalFactory;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;
import ng.apmis.audreymumplus.ui.Journal.JournalViewModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static android.app.Activity.RESULT_OK;

public class MyGalleryFragment extends android.support.v4.app.Fragment {
    private static final String CLASSNAME = "HOME";

    JournalViewModel journalViewModel;
    List<GalleryModel> galleryList;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        ButterKnife.bind(this, rootView);

        //  ((DashboardActivity)getActivity()).setToolBarTitle(CLASSNAME);

        GridView gridView = rootView.findViewById(R.id.list_gallery);

        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity(), new ArrayList<>());

        gridView.setAdapter(galleryAdapter);

        JournalFactory journalFactory = InjectorUtils.provideJournalFactory(getActivity());
        journalViewModel = ViewModelProviders.of(getActivity(), journalFactory).get(JournalViewModel.class);
        View emptyView = rootView.findViewById(R.id.empty_view);

        journalViewModel.getmJournalEntry().observe(getActivity(), journalModels -> {
            if (journalModels != null) {
            for (JournalModel x : journalModels) {
                if (!TextUtils.isEmpty(x.getPregnancyBellyUri())) {
                    galleryList.add(new GalleryModel(x.getPregnancyBellyUri()));
                }
                if (!TextUtils.isEmpty(x.getBabyScanUri())) {
                    galleryList.add(new GalleryModel(x.getBabyScanUri()));
                }
            }
                galleryAdapter.setGalleryModels(galleryList == null ? new ArrayList<>() : galleryList);

            } else {

                gridView.setEmptyView(emptyView);
            }
        });

        return rootView;
    }


}
