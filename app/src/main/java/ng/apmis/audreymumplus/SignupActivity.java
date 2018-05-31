package ng.apmis.audreymumplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;

/**
 * Created by Thadeus-APMIS on 5/31/2018.
 */

public class SignupActivity extends AppCompatActivity implements SignupFragmentB.OnFragmentInteractionListener{

    static JSONObject audreyMum;
    ProgressDialog progressDialog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        audreyMum = new JSONObject();
        queue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);

        placeFragment(new SignupFragmentA(), getSupportFragmentManager(), null);

    }

    static void placeFragment (Fragment fragment, FragmentManager fm, String tag) {
        if (tag == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
            return;
        }
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickView (View view) {
        JSONObject uniquePerson = new JSONObject();
        //Assign all values to an Object with a person key
        try {
            uniquePerson.put("person", audreyMum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Person Obj", uniquePerson.toString());
        Log.v("Person Starts", "Here");
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Sign Up");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        signUp(uniquePerson);
    }


    void signUp(JSONObject uniquePerson) {

        if (true) startActivity(new Intent(this, DashboardActivity.class)); finish();

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, "save-person", uniquePerson, response -> {
            progressDialog.dismiss();
            Log.v("Sign up response", String.valueOf(response));
            startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
            finish();
        }, error -> {
            progressDialog.dismiss();
            Log.v("Sign up error", String.valueOf(error.getMessage()));
            Toast.makeText(SignupActivity.this, "There was an error try again", Toast.LENGTH_SHORT).show();
        });
        queue.add(strRequest);
    }
}
