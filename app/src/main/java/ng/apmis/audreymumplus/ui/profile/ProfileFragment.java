package ng.apmis.audreymumplus.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
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
    private String updateBiodataPersonId;
    private String updateProfileDbId;

    Uri uri;
    String imageFilePath;
    public static final int GALLERY_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int CROP_REQUEST_CODE = 3;

    AppCompatActivity activity;

    Intent camIntent, GalIntent, cropIntent;

    int size;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);


        AudreyMumplus.getInstance().diskIO().execute(() -> {

            ((DashboardActivity) getActivity()).getPersonLive().observe(this, userDetails -> {
                if (userDetails != null) {
                    Log.v("User Details", userDetails.toString());
                    firstNameEdittext.setText(getContext().getString(R.string.user_firstname, userDetails.getFirstName()));
                    lastNameEdittext.setText(getContext().getString(R.string.user_lastname, userDetails.getLastName()));
                    userEmail.setText(getContext().getString(R.string.user_email, userDetails.getEmail()));
                    phoneEdittext.setText(getContext().getString(R.string.user_phone, userDetails.getPrimaryContactPhoneNo()));
                    updateBiodataPersonId = userDetails.getPersonId();
                    updateProfileDbId = userDetails.get_id();

                    Glide.with(getContext())
                            .load(userDetails.getProfileImage() != null ? userDetails.getProfileImage() : R.drawable.ic_profile_place_holder)
                            .into(userImage);
                }
            });
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
                InjectorUtils.provideJournalNetworkDataSource(activity).updateProfileGetAudrey(updateBiodataPersonId, changeFields, activity, false);
                setFocusableEditable(false);
            }
        });


        return rootView;
    }

    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        final BottomSheetDialog builder = new BottomSheetDialog(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
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

    public void ClickImageFromCamera() {

        File file;

        camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
                } else {
                    startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
                }
            }

        } else {
            Toast.makeText(getActivity(), "There's a problem with camera", Toast.LENGTH_SHORT).show();
        }






    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void GetImageFromGallery() {

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), GALLERY_REQUEST_CODE);

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

    public void ImageCropFunction() {


        // Image Crop Code
        try {
            cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(uri, "image/*");

            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("outputX", 440);
            cropIntent.putExtra("outputY", 440);
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 4);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            cropIntent.putExtra("return-data", true);


            cropIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);


            startActivityForResult(cropIntent, CROP_REQUEST_CODE);


        } catch (ActivityNotFoundException ignored) {

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }



    @SuppressLint("StaticFieldLeak")
    public void uploadImage(String image) {
        Log.v("currentperson", updateProfileDbId);
        JSONObject changeFields = new JSONObject();
        try {
            changeFields.put("uri", "data:image/jpeg;base64," + image);
            changeFields.put("email", userEmail.getText().toString());
            changeFields.put("peopleId", updateProfileDbId);
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
            startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity(), "You need permission to use camera", Toast.LENGTH_SHORT).show();
        }
    }



}
