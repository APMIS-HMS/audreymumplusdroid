package ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyimagegallery;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal.JournalFactory;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal.JournalModel;
import ng.apmis.audreymumplus.ui.pregnancymodule.pregnancyjournal.JournalViewModel;
import ng.apmis.audreymumplus.utils.InjectorUtils;

public class MyGalleryFragment extends android.support.v4.app.Fragment {

    JournalViewModel journalViewModel;
    List<GalleryModel> galleryList;
    List<GalleryModel> pregnancyBelly, babyScan;
    @BindView(R.id.gallery_option_spinner)
    Spinner galleryOptionSpinner;

    @BindView(R.id.list_gallery)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    View emptyView;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_gallery, container, false);
        ButterKnife.bind(this, rootView);


        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(galleryAdapter);

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

            if (galleryAdapter.getItemCount() > 0) {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        });




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
