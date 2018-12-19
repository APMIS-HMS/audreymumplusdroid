package ng.apmis.audreymumplus.data.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 12/17/2018.
 */

public class NetworkCalls {

    private Context context;
    private Gson gson;

    NetworkCalls(Context context) {
        this.context = context;
        gson = new GsonBuilder().create();
    }

    public void getProfileImage (Person person, File downloadFile) {

        ImageLoader.ImageListener imageListener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                try {
                    FileOutputStream out = new FileOutputStream(downloadFile);

                    //compress bitmap and save to file
                    response.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    //notify that image has been saved
                    person.setProfileImageLocalFileName(downloadFile.getName());
                    InjectorUtils.provideJournalNetworkDataSource(context).updatePerson(person);


                    Log.v("Image reponse", "Download image completed to "+downloadFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.e("TAG", error.getLocalizedMessage());
                    //Notify an error occurred
                    InjectorUtils.provideJournalNetworkDataSource(context).updatePerson(person);
                } catch (Exception e){

                }
            }
        };

        try {
            Log.e("Profile image url", person.getProfileImage());

            AudreyMumplus.getInstance().getImageLoader().get(person.getProfileImage(), imageListener);
        } catch (NullPointerException ex) {
            Log.e("upload image exception", ex.getMessage());
        }

    }

}
