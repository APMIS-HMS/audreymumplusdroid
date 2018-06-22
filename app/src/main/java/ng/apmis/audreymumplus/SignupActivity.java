package ng.apmis.audreymumplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

/**
 * Created by Thadeus-APMIS on 5/31/2018.
 */

public class SignupActivity extends AppCompatActivity implements SignupFragmentB.OnFragmentInteractionListener{

    static JSONObject audreyMum;
    ProgressDialog progressDialog;
    RequestQueue queue;
    private static final String BASE_URL = "https://audrey-mum.herokuapp.com/";

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.storeUserPassword("");
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
        try {
            uniquePerson.put("person", audreyMum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        signUp(uniquePerson);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void signUp(JSONObject uniquePerson) {

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "save-person", uniquePerson, response -> {

            Log.v("Sign up response", String.valueOf(response));
            try {
                JSONObject signUpJob = new JSONObject(response.toString());
                if (signUpJob.getString("status").equals("error")) {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "There was an error try again", Toast.LENGTH_SHORT).show();
                } else {
                    attemptLogin(uniquePerson.getJSONObject("person").getString("email"), uniquePerson.getJSONObject("person").getString("password"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this, "There was an error try again", Toast.LENGTH_SHORT).show();
        });
        queue.add(strRequest);
    }

    public void setSupportActionTitle (String title, boolean backButton) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(backButton);
        getSupportActionBar().setHomeButtonEnabled(backButton);
        getSupportActionBar().setHomeAsUpIndicator(!backButton ? R.mipmap.audrey_icon : R.drawable.ic_arrow_back_black_24dp);
    }

    private void attemptLogin(String email, String password) {
        progressDialog.setTitle("Signing In");

        sharedPreferencesManager.storeUserEmail(email);

        AudreyMumplus.getInstance().networkIO().execute(() -> {

            JSONObject job = new JSONObject();
            try {
                job.put("email", email);
                job.put("password", password);
                job.put("strategy", "local");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {

                try {
                    Log.v("accessToken", response.getString("accessToken"));
                    String token = response.getString("accessToken");

                    sharedPreferencesManager.storeUserToken(token);

                    Log.v("sharedPRef", String.valueOf(sharedPreferencesManager.getUserToken()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                finish();
                startActivity(new Intent(this, DashboardActivity.class));
            }, error -> {
                Log.d("error", String.valueOf(error.getMessage()) + "Error");
                progressDialog.dismiss();
                new AlertDialog.Builder(this)
                        .setTitle("Login Failed")
                        .setMessage("Please try again !!!")
                        .setPositiveButton("Login", (dialog, which) -> startActivity(new Intent(this, LoginActivity.class)))
                        .show();
            });

            queue.add(loginRequest);

        });


    }

}
