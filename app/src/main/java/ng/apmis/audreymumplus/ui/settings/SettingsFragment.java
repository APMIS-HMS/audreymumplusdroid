package ng.apmis.audreymumplus.ui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.EncryptionUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

/**
 * Created by Thadeus-APMIS on 8/15/2018.
 */

public class SettingsFragment extends Fragment {

    EditText passwordEditText;
    Activity activity;
    boolean goBack = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.create_chat_forum, null);

        passwordEditText = dialogView.findViewById(R.id.forum_name);

        new AlertDialog.Builder(getActivity())
                .setTitle("Enter current password")
                .setView(dialogView)
                .setNegativeButton("Cancel", ((dialog, which) -> {
                    goBack = true;
                    dialog.dismiss();
                }))
                .setPositiveButton("Submit", ((dialog, which) -> {
                    if (passwordEditText.getText().toString().equals(new SharedPreferencesManager(getActivity()).getStoredUserPassword())) {                goBack = false;
                        getActivity().getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, new SettingPreferences(), "settings")
                                .commit();
                    } else {
                        goBack = true;
                        Toast.makeText(getActivity(), "password invalid", Toast.LENGTH_SHORT).show();

                    }
                })).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (goBack)
                    activity.onBackPressed();
            }
        })
                .show();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (DashboardActivity)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity)getActivity()).setActionBarButton(true, "Settings");
        ((DashboardActivity)getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        ((DashboardActivity)getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity)getActivity()).bottomNavVisibility(true);
        super.onStop();
    }

}
