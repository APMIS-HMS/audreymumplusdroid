package ng.apmis.audreymumplus.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus on 6/13/2018.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.user_full_name)
    TextInputEditText userFullname;
    @BindView(R.id.add_image)
    FloatingActionButton addImage;
    @BindView(R.id.user_image)
    CircularImageView userImage;
    @BindView(R.id.user_email)
    TextInputEditText userEmail;
    @BindView(R.id.edit_save_btn)
    Button editSaveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);

        ((DashboardActivity) getActivity()).person.observe(getActivity(), userDetails -> {
            userFullname.setText(getContext().getString(R.string.user_fullname, userDetails.getFirstName(), userDetails.getLastName()));
            userEmail.setText(getContext().getString(R.string.user_email, userDetails.getEmail()));
            //TODO Check user image in ${userDetails}
        });

        addImage.setOnClickListener((view) -> {
            //TODO Start intent to pick image from camera or from gallery
            Toast.makeText(getContext(), "I will add image to the imageview that contains me", Toast.LENGTH_SHORT).show();
        });

        editSaveButton.setOnClickListener((view) -> {
            Button b = (Button) view;
            if (b.getText().toString().equals("EDIT")) {
                setFocusableEditable(true);
            } else {
                //TODO start save transaction in a loading whether local or to server
                //On completion disable fields
                setFocusableEditable(false);
            }
        });


        return rootView;
    }

    void setFocusableEditable(boolean b) {
        userFullname.setEnabled(b);
        userEmail.setEnabled(b);
        addImage.setVisibility(b ? View.VISIBLE : View.GONE);

        editSaveButton.setText(b ? "SAVE" : "EDIT");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "My Profile");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
    }
}
