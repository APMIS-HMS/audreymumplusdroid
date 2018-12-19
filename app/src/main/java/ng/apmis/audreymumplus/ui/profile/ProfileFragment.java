package ng.apmis.audreymumplus.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.TextUtils;
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
import com.theartofdev.edmodo.cropper.CropImage;

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
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.InputUtils;

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
        InputUtils.showKeyboard(activity, firstNameEdittext);

        AudreyMumplus.getInstance().diskIO().execute(() -> {

            ((DashboardActivity) getActivity()).getPersonLive().observe(this, new Observer<Person>() {
                @Override
                public void onChanged(@Nullable Person userDetails) {
                    if (userDetails != null) {
                        firstNameEdittext.setText(ProfileFragment.this.getContext().getString(R.string.user_firstname, userDetails.getFirstName()));
                        lastNameEdittext.setText(ProfileFragment.this.getContext().getString(R.string.user_lastname, userDetails.getLastName()));
                        userEmail.setText(ProfileFragment.this.getContext().getString(R.string.user_email, userDetails.getEmail()));
                        phoneEdittext.setText(ProfileFragment.this.getContext().getString(R.string.user_phone, userDetails.getPrimaryContactPhoneNo()));
                        updateBiodataPersonId = userDetails.getPersonId();
                        updateProfileDbId = userDetails.get_id();
                        Log.e("TAGGED", "Called frag as " + userDetails.getFirstName() +" "+userDetails.getId());


                        loadProfileImage(userDetails);
                        /*Glide.with(ProfileFragment.this.getContext())
                                .load(userDetails.getProfileImage() != null ? userDetails.getProfileImage() : R.drawable.ic_profile_place_holder)
                                .into(userImage);*/
                    }
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
                InjectorUtils.provideJournalNetworkDataSource(activity).updateProfileGetAudrey(updateBiodataPersonId, changeFields, getActivity());
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
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
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
    public void onPause() {
        InputUtils.hideKeyboard();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            cropImage();

        } else if (requestCode == GALLERY_REQUEST_CODE) {

            if (data != null) {
                uri = data.getData();
                cropImage();
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();

                File auxFile = new File(resultUri.getPath());

                Bitmap bitmap = BitmapFactory.decodeFile(auxFile.getAbsolutePath());
                userImage.setImageBitmap(bitmap);

                //compressed bitmap quality to 25%
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);

                byte[] byteArray = stream.toByteArray();

                size = byteArray.length / 1024;

                // Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                String imageBase64String = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                Log.v("TAGGED", imageBase64String);

                uploadImage(imageBase64String);

            }

        }

    }

    public void cropImage(){
        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity(uri).setFixAspectRatio(true)
                .start(getContext(), this);
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


    public void loadProfileImage (Person person) {
        File profilePhotoDir = new File(getContext().getFilesDir(), "profilePhotos");
        profilePhotoDir.mkdir();

        File localFile =  null;

        if (!TextUtils.isEmpty(person.getProfileImageLocalFileName()))
            localFile = new File(profilePhotoDir, person.getProfileImageLocalFileName());

        if (localFile != null && localFile.exists()){
            //imageProgress.setVisibility(View.GONE);
            try {
                Glide.with(getContext()).load(localFile).into(userImage);
            } catch (Exception e){

            }
            return;
        }

        AudreyMumplus.getInstance().networkIO().execute(() -> {
            InjectorUtils.provideJournalNetworkDataSource(getContext()).getProfileImageFromUrl(person, profilePhotoDir);
        });

    }

}
