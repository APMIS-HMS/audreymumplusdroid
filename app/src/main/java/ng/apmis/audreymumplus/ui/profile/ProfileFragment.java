package ng.apmis.audreymumplus.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.CameraUtils;
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Thadeus on 6/13/2018.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.firstname_et)
    TextInputEditText firstNameEdittext;
    @BindView(R.id.lastname_et)
    TextInputEditText lastNameEdittext;
    @BindView(R.id.phone_edittext)
    TextInputEditText phoneEdittext;

    @BindView(R.id.user_email)
    TextInputEditText userEmail;

    @BindView(R.id.add_image)
    FloatingActionButton addImage;
    @BindView(R.id.user_image)
    CircularImageView userImage;
    @BindView(R.id.edit_save_btn)
    Button editSaveButton;
    private String currentPersonId;

    Uri uri;
    String mCurrentPhotoPath;
    public static final int GALLERY_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int CROP_REQUEST_CODE = 3;

    CameraUtils cameraUtils;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);
        cameraUtils = new CameraUtils(this);

        ((DashboardActivity) getActivity()).person.observe(getActivity(), userDetails -> {
            firstNameEdittext.setText(getContext().getString(R.string.user_firstname, userDetails.getFirstName()));
            lastNameEdittext.setText(getContext().getString(R.string.user_lastname, userDetails.getLastName()));
            userEmail.setText(getContext().getString(R.string.user_email, userDetails.getEmail()));
            phoneEdittext.setText(getContext().getString(R.string.user_phone, userDetails.getPrimaryContactPhoneNo()));
            currentPersonId = userDetails.getPersonId();
            //TODO Check user image in ${userDetails}
        });

        addImage.setOnClickListener((view) -> {
            cameraUtils.selectImageOption();
        });

        editSaveButton.setOnClickListener((view) -> {
            Button b = (Button) view;
            if (b.getText().toString().equals("EDIT")) {
                setFocusableEditable(true);
            } else {

                JSONObject changeFields = new JSONObject();
                try {
                    changeFields.put("firstName", firstNameEdittext.getText().toString());
                    changeFields.put("lastName", lastNameEdittext.getText().toString());
                    changeFields.put("email", userEmail.getText().toString());
                    changeFields.put("primaryContactPhoneNo", phoneEdittext.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                InjectorUtils.provideJournalNetworkDataSource(getActivity()).updateProfile(currentPersonId, changeFields, getActivity());
                setFocusableEditable(false);
            }
        });


        return rootView;
    }

    void setFocusableEditable(boolean b) {
        firstNameEdittext.setEnabled(b);
        lastNameEdittext.setEnabled(b);
        userEmail.setEnabled(b);
        phoneEdittext.setEnabled(b);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            cameraUtils.ImageCropFunction(null);

        } else if (requestCode == GALLERY_REQUEST_CODE) {

            if (data != null) {
                uri = data.getData();
                cameraUtils.ImageCropFunction(uri);

            }
        } else if (requestCode == CROP_REQUEST_CODE) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                userImage.setImageBitmap(bitmap);

            }
        }

    }

}
