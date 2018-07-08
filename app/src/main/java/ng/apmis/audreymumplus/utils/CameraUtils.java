package ng.apmis.audreymumplus.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ng.apmis.audreymumplus.R;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by Thadeus-APMIS on 7/7/2018.
 */

public class CameraUtils {

    public static final int GALLERY_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int CROP_REQUEST_CODE = 3;
    Fragment mContext;


    private String mCurrentPhotoPath;
    Uri uri;

    public CameraUtils(Fragment context) {
        mContext = context;
    }

    public void GetImageFromGallery() {

        Intent GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        mContext.startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), GALLERY_REQUEST_CODE);

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
                    uri = FileProvider.getUriForFile(mContext.getActivity(),
                            mContext.getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                            file);
                    galleryAddPic(mContext.getActivity());

                    mContext.getActivity().getApplicationContext().grantUriPermission("com.android.camera",
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

        if (camIntent.resolveActivity(mContext.getActivity().getPackageManager()) != null) {
            mContext.startActivityForResult(camIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(mContext.getContext(), "There's a problem with camera", Toast.LENGTH_SHORT).show();
        }


    }

    private void galleryAddPic(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
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

    public void ImageCropFunction(Uri uri) {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            CropIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);

            CropIntent.setDataAndType(uri == null ? this.uri : uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 440);
            CropIntent.putExtra("outputY", 440);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);


            mContext.startActivityForResult(CropIntent, CROP_REQUEST_CODE);


        } catch (ActivityNotFoundException ignored) {

        }
    }


    public void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        final BottomSheetDialog builder = new BottomSheetDialog(mContext.getActivity());
        LayoutInflater inflater = mContext.getActivity().getLayoutInflater();
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


}
