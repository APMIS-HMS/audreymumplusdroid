package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 7/13/2018.
 */

public class JournalDetailFragment extends Fragment {

    @BindView(R.id.journal_date)
    TextView journalDate;
    @BindView(R.id.cravings_body)
    TextView cravingsBody;
    @BindView(R.id.symptoms_body)
    TextView symptomsBody;
    @BindView(R.id.weight_body)
    TextView weightBody;
    @BindView(R.id.scan_image)
    ImageView scanImage;
    @BindView(R.id.pregnancy_belly_image)
    ImageView pregBellyImage;
    @BindView(R.id.personal_note_body)
    TextView personalNoteBody;

    AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journal_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (getArguments().getParcelable("journal") != null) {
            JournalModel thisJournal = getArguments().getParcelable("journal");
            journalDate.setText(getString(R.string.journal_date, DateUtils.getRelativeTimeSpanString(activity, thisJournal.getDate())));
            cravingsBody.setText(thisJournal.getCravings());
            symptomsBody.setText(thisJournal.getSymptoms());
            weightBody.setText(thisJournal.getWeight());

            if (thisJournal.getBabyScanUri() != null) {
                Glide.with(activity)
                        .load(thisJournal.getBabyScanUri())
                        .into(scanImage);
            }

            if (thisJournal.getPregnancyBellyUri() != null) {
                Glide.with(activity)
                        .load(thisJournal.getPregnancyBellyUri())
                        .into(pregBellyImage);
            }

            personalNoteBody.setText(thisJournal.getMood() + "\n" + thisJournal.getBabyMovement());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Journal Detail");
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }
}
