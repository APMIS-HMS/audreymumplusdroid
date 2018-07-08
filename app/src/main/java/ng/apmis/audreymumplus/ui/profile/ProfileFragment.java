package ng.apmis.audreymumplus.ui.profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);

        ((DashboardActivity) getActivity()).person.observe(getActivity(), userDetails -> {
            firstNameEdittext.setText(getContext().getString(R.string.user_firstname, userDetails.getFirstName()));
            lastNameEdittext.setText(getContext().getString(R.string.user_lastname, userDetails.getLastName()));
            userEmail.setText(getContext().getString(R.string.user_email, userDetails.getEmail()));
            phoneEdittext.setText(getContext().getString(R.string.user_phone, userDetails.getPrimaryContactPhoneNo()));
            currentPersonId = userDetails.getPersonId();
            //TODO Check user image in ${userDetails}
        });

        addImage.setOnClickListener((view) -> {
            selectImageOption();
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

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        final BottomSheetDialog builder = new BottomSheetDialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_cell, null);
        builder.setContentView(dialogView);
        ImageButton btnPix = (ImageButton) dialogView.findViewById(R.id.btnSelectPicture);
        btnPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImageFromGallery();
                builder.dismiss();
            }
        });

        ImageButton btnCamera = (ImageButton) dialogView.findViewById(R.id.btnSelectCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickImageFromCamera();
                builder.dismiss();
            }
        });

        ImageButton btnX = (ImageButton) dialogView.findViewById(R.id.btnCancel);
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageCropFunction();

        } else if (requestCode == GALLERY_REQUEST_CODE) {

            if (data != null) {
                uri = data.getData();
                ImageCropFunction();

            }
        } else if (requestCode == CROP_REQUEST_CODE) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                userImage.setImageBitmap(bitmap);

            }
        }

    }

    public void GetImageFromGallery() {

        Intent GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), GALLERY_REQUEST_CODE);

    }

    public void ClickImageFromCamera() {

        File file;

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            file = new File(Environment.getExternalStorageDirectory(),
                    "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");

            uri = Uri.fromFile(file);

        } else {

            try {
                file = createImageFile();
                if (file != null) {
                    uri = FileProvider.getUriForFile(getContext(),
                            getContext().getApplicationContext().getPackageName() + ".fileprovider",
                            file);
                    galleryAddPic();

                    getActivity().getApplicationContext().grantUriPermission("com.android.camera",
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    camIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                    camIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        camIntent.putExtra("return-data", true);

        if (camIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity(), "There's a problem with camera", Toast.LENGTH_SHORT).show();
        }


    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            CropIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 440);
            CropIntent.putExtra("outputY", 440);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);


            startActivityForResult(CropIntent, CROP_REQUEST_CODE);


        } catch (ActivityNotFoundException ignored) {

        }
    }
}
