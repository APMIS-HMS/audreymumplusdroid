package ng.apmis.audreymumplus.ui.pregnancymodule.journal;

import android.Manifest;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import ng.apmis.audreymumplus.utils.InputUtils;
import ng.apmis.audreymumplus.utils.Week;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity.globalPerson;

/**
 * Created by Thadeus-APMIS on 5/21/2018.
 */

public class JournalAddFragment extends Fragment {

    @BindView(R.id.crave)
    TextInputEditText cravings;
    @BindView(R.id.weight)
    TextInputEditText weight;
    @BindView(R.id.symptoms)
    TextInputEditText symtoms;
    @BindView(R.id.mood)
    TextInputEditText moodEdittext;

   /* @BindView(R.id.user_babyscan)
    ImageView babyscan;
    @BindView(R.id.baby_scan_fab)
    FloatingActionButton babyScanFab;*/

    @BindView(R.id.baby_scan)
    ViewGroup babyScanViewGroup;
    @BindView(R.id.baby_scan_tv)
    TextView babyScanTv;
/*
    @BindView(R.id.pregBel)
    ImageView pregbel;
    @BindView(R.id.preg_belly_fab)
    FloatingActionButton pregBellyFab;*/

    @BindView(R.id.baby_bump)
    ViewGroup babyBumpViewGroup;
    @BindView(R.id.baby_bump_tv)
    TextView babyBumpTv;

    @BindView(R.id.baby_movement)
    TextInputEditText babyMovement;

    @BindView(R.id.save_btn)
    Button saveJournal;

    Intent camIntent, GalIntent, cropIntent;
    Uri uri;

    String uriToSet, pregScan, babyBump = "";
    String imageFilePath;
    TextView setImageUri;

    AppCompatActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_journal, container, false);
        ButterKnife.bind(this, rootView);

        InputUtils.showKeyboard(activity, moodEdittext);

        saveJournal.setOnClickListener((view) -> {
            if (checkFields()) {

                long date = new Date().getTime();
                String mood = moodEdittext.getText().toString();
                String crav = cravings.getText().toString();
                String heavy = weight.getText().toString();
                String symtom = symtoms.getText().toString();
                String baby = babyMovement.getText().toString();

                JournalModel newJournal = new JournalModel(mood, crav, heavy, symtom, pregScan, babyBump, baby, date, String.valueOf(globalPerson.getDay()), Week.valueOf(globalPerson.getWeek().replace(" ", "")).getWeek());

                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    try {
                        InjectorUtils.provideRepository(getActivity()).saveJournal(newJournal);
                        activity.runOnUiThread(() ->
                                new AlertDialog.Builder(activity, R.style.DialogTheme)
                                        .setTitle("Alert")
                                        .setMessage("Journal saved successfully")
                                        .setPositiveButton("OK", ((dialogInterface, i) -> {
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            dialogInterface.dismiss();
                                        }
                                        ))
                                        .show());
                    } catch (SQLiteCantOpenDatabaseException ex) {
                        activity.runOnUiThread(() ->
                                new AlertDialog.Builder(activity, R.style.DialogTheme)
                                        .setTitle("Alert")
                                        .setMessage("Journal failed to save")
                                        .setPositiveButton("BACK", ((dialogInterface, i) -> {
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            dialogInterface.dismiss();
                                        }
                                        ))
                                        .show());
                    }
                });


            }
        });

        babyScanViewGroup.setOnClickListener((view) -> {
            setImageUri = babyScanTv;
            uriToSet = "baby-scan";
            selectImageOption();
        });

        babyBumpViewGroup.setOnClickListener((view) -> {
            setImageUri = babyBumpTv;
            uriToSet = "preg-scan";
            selectImageOption();
        });
        return rootView;
    }

    boolean checkFields() {

        if (moodEdittext.getText().toString().equals("")) {
            moodEdittext.setError("required");
            return false;
        }

        if (cravings.getText().toString().equals("")) {
            cravings.setError("required");
            return false;
        }
        if (symtoms.getText().toString().equals("")) {
            symtoms.setError("required");
            return false;
        }
        if (babyMovement.getText().toString().equals("")) {
            babyMovement.setError("required");
            return false;
        }


        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "New Journal");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
        ((DashboardActivity) getActivity()).fabVisibility(false);
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
                startActivityForResult(camIntent, 0);
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9000);
                } else {
                    startActivityForResult(camIntent, 0);
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

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();

        } else if (requestCode == 2) {

            if (data != null) {
                uri = data.getData();
                ImageCropFunction();

            }
        } else if (requestCode == 1) {


            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                if (uriToSet.equals("baby-scan")) {
                    Toast.makeText(getActivity(), "Baby scan " + uri.toString(), Toast.LENGTH_SHORT).show();
                    pregScan = uri.toString();
                } else {
                    Toast.makeText(getActivity(), "Belly scan " + uri.toString(), Toast.LENGTH_SHORT).show();
                    babyBump = uri.toString();
                }
                setImageUri.setText("image/" + uri.getLastPathSegment());
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


            startActivityForResult(cropIntent, 1);


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onPause() {
        InputUtils.hideKeyboard();
        super.onPause();
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
