package ng.apmis.audreymumplus.ui.Journal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.data.AudreyRepository;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Thadeus-APMIS on 5/21/2018.
 */

public class AddJournalFragment extends Fragment {

    @BindView(R.id.crave)
    TextInputEditText cravings;
    @BindView(R.id.weight)
    TextInputEditText weight;
    @BindView(R.id.symptoms)
    TextInputEditText symtoms;
    @BindView(R.id.mood)
    TextInputEditText moodEdittext;

    @BindView(R.id.user_babyscan)
    ImageView babyscan;
    @BindView(R.id.baby_scan_fab)
    FloatingActionButton babyScanFab;

    @BindView(R.id.pregBel)
    ImageView pregbel;
    @BindView(R.id.preg_belly_fab)
    FloatingActionButton pregBellyFab;

    @BindView(R.id.baby_movement)
    TextInputEditText babyMovement;

    @BindView(R.id.save_btn)
    Button saveJournal;

    Intent CamIntent, GalIntent, CropIntent;
    Uri uri;
    File file;

    ImageView imageViewToUpdate;
    String uriToSet, pregScan, pregBelly = "";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_journal, container, false);
        ButterKnife.bind(this, rootView);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        saveJournal.setOnClickListener((view) -> {
            if (checkFields()) {

                long date = new Date().getTime();
                String mood = moodEdittext.getText().toString();
                String crav = cravings.getText().toString();
                String heavy = weight.getText().toString();
                String symtom = symtoms.getText().toString();
                String baby = babyMovement.getText().toString();

                JournalModel newJournal = new JournalModel(mood, crav, heavy, symtom, pregScan, pregBelly, baby, date);

                AudreyMumplus.getInstance().diskIO().execute(() ->
                        InjectorUtils.provideRepository(getActivity()).saveJournal(newJournal));

                Toast.makeText(getActivity(), "Journal Saved successfully", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        babyScanFab.setOnClickListener((view) -> {
            imageViewToUpdate = babyscan;
            uriToSet = "baby-scan";
            selectImageOption();
        });

        pregBellyFab.setOnClickListener((view) -> {
            imageViewToUpdate = pregbel;
            uriToSet = "preg-scan";
            selectImageOption();
        });



        return rootView;
    }

    boolean checkFields () {

        if (moodEdittext.getText().toString().equals("")) {
            moodEdittext.setError("required");
            return false;
        }

        if (cravings.getText().toString().equals("")) {
            cravings.setError("required");
            return false;
        }
        if(symtoms.getText().toString().equals("")){
            symtoms.setError("required");
            return false;
        }
        if(babyMovement.getText().toString().equals("")){
            babyMovement.setError("required");
            return false;
        }


        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Add Journal");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, "My Pregnancy");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
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

        CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

       // CamIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        CamIntent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

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

                } else {

                }

                imageViewToUpdate.setImageBitmap(bitmap);
                try {
                   // upload(bitmap);
                } catch (Exception ignored) {

                }

            }
        }

    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 440);
            CropIntent.putExtra("outputY", 440);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);


            startActivityForResult(CropIntent, 1);


        } catch (ActivityNotFoundException ignored) {

        }
    }

}
