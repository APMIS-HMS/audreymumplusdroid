package ng.apmis.audreymumplus.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
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

    AppCompatActivity activity;

    int size;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);
        cameraUtils = new CameraUtils(this);


        AudreyMumplus.getInstance().diskIO().execute(() -> {

            ((DashboardActivity) getActivity()).getPersonLive().observe(this, userDetails -> {
                firstNameEdittext.setText(getContext().getString(R.string.user_firstname, userDetails.getFirstName()));
                lastNameEdittext.setText(getContext().getString(R.string.user_lastname, userDetails.getLastName()));
                userEmail.setText(getContext().getString(R.string.user_email, userDetails.getEmail()));
                phoneEdittext.setText(getContext().getString(R.string.user_phone, userDetails.getPrimaryContactPhoneNo()));
                currentPersonId = userDetails.getPersonId();

                    Glide.with(getContext())
                            .load(userDetails.getProfileImage() != null ? userDetails.getProfileImage() : R.drawable.ic_profile_place_holder)
                            .into(userImage);
            });
        });
        addImage.setOnClickListener((view) -> {
            //TODO check camera permission and storage permission
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
            } else {
                cameraUtils.selectImageOption();
            }
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
                InjectorUtils.provideJournalNetworkDataSource(activity).updateProfileGetAudrey(currentPersonId, changeFields, activity, false);
                setFocusableEditable(false);
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
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

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                size = byteArray.length / 1024;

               // Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                String imageBase64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

               // Log.v("Base 64 string", imageBase64String);

               uploadImage(imageBase64String);


            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void uploadImage(String image) {
        JSONObject changeFields = new JSONObject();
        try {
            changeFields.put("uri", image);
            changeFields.put("email", userEmail.getText().toString());
            changeFields.put("peopleId", currentPersonId);
            changeFields.put("size", size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("change fields", changeFields.toString());
        InjectorUtils.provideJournalNetworkDataSource(activity).updateProfileImage(changeFields, activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Click add camera button", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "You need permission to use camera", Toast.LENGTH_SHORT).show();
        }
    }

}
