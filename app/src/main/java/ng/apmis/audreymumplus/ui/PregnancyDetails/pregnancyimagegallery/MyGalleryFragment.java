package ng.apmis.audreymumplus.ui.PregnancyDetails.pregnancyimagegallery;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Journal.JournalFactory;
import ng.apmis.audreymumplus.ui.Journal.JournalModel;
import ng.apmis.audreymumplus.ui.Journal.JournalViewModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class MyGalleryFragment extends android.support.v4.app.Fragment {
    private static final String CLASSNAME = "HOME";

    JournalViewModel journalViewModel;
    List<GalleryModel> galleryList;
    List<GalleryModel> pregnancyBelly, babyScan;
    @BindView(R.id.gallery_option_spinner)
    Spinner galleryOptionSpinner;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_gallery, container, false);

        ButterKnife.bind(this, rootView);

        GridView gridView = rootView.findViewById(R.id.list_gallery);

        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity());

        gridView.setAdapter(galleryAdapter);

        JournalFactory journalFactory = InjectorUtils.provideJournalFactory(getActivity());
        journalViewModel = ViewModelProviders.of(getActivity(), journalFactory).get(JournalViewModel.class);

        journalViewModel.getmJournalEntry().observe(getActivity(), journalModels -> {

            galleryList = new ArrayList<>();
            pregnancyBelly = new ArrayList<>();
            babyScan = new ArrayList<>();

            if (journalModels != null) {
            for (JournalModel x : journalModels) {
                if (!TextUtils.isEmpty(x.getPregnancyBellyUri())) {
                    galleryList.add(new GalleryModel(x.getPregnancyBellyUri()));
                    pregnancyBelly.add(new GalleryModel(x.getPregnancyBellyUri()));
                }
                if (!TextUtils.isEmpty(x.getBabyScanUri())) {
                    galleryList.add(new GalleryModel(x.getBabyScanUri()));
                    babyScan.add(new GalleryModel(x.getBabyScanUri()));
                }
            }
                galleryAdapter.setGalleryModels(galleryList == null ? new ArrayList<>() : galleryList);

            }
        });

        gridView.setEmptyView(rootView.findViewById(R.id.empty_view));

        galleryOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (((String)adapterView.getItemAtPosition(i))) {
                    case  "All":
                        galleryAdapter.setGalleryModels(galleryList);
                        break;
                    case "Baby Scan":
                        galleryAdapter.setGalleryModels(babyScan);
                        break;
                    case "Pregnancy Belly":
                        galleryAdapter.setGalleryModels(pregnancyBelly);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }


}
