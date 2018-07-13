package ng.apmis.audreymumplus.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 7/13/2018.
 */

public class HelpFragment extends Fragment {

    @BindView(R.id.report_btn)
    Button reportButton;
    AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this, rootView);

        reportButton.setOnClickListener((view) -> {

            Intent reportIntent = new Intent(Intent.ACTION_SEND);
            reportIntent.setType("message/rfc822");
            reportIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"help@audreypack.com"});
            reportIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");

            if (reportIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(Intent.createChooser(reportIntent, "Select email application"));
            } else {
                Toast.makeText(activity, "There is no application to handle request", Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Help");
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
