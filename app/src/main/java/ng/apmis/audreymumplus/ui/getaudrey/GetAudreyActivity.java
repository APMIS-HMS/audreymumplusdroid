package ng.apmis.audreymumplus.ui.getaudrey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class GetAudreyActivity extends AppCompatActivity implements GetAudreyPageTwo.OnFragmentInteractionListener{

    JSONObject registrationData;

    public String selectedState;
    public String selectedLga;
    RequestQueue queue;

    static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_audrey);

        registrationData = new JSONObject();
        queue = Volley.newRequestQueue(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.get_audrey_container, new GetAudreyPageOne())
                .addToBackStack(null)
                .commit();
    }

    void getStateAndLga (final String urlPath, Spinner spinner) {
        StringRequest any = new StringRequest(Request.Method.GET, BASE_URL + urlPath, response -> {
            try {
                JSONObject responseObj = new JSONObject(response.toString());
                JSONArray jar = responseObj.getJSONArray("data");
                if (urlPath.equals(getString(R.string.state))) {
                    final String[] gendersArray = new String[jar.length()];
                    for (int i = 0; i < jar.length(); i++) {
                        gendersArray[i] = jar.getJSONObject(i).getString("name");
                    }
                    runOnUiThread(() -> setupSpinnerAdapters(gendersArray, spinner));
                }
                if (urlPath.equals(getString(R.string.lga))) {
                    final String[] securityQuestionsArray = new String[jar.length()];
                    for (int i = 0; i < jar.length(); i++) {
                        securityQuestionsArray[i] = jar.getJSONObject(i).getString("name");
                    }
                    runOnUiThread(() -> setupSpinnerAdapters(securityQuestionsArray, spinner));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("Volley Error gender sec", String.valueOf(error));
            getStateAndLga(urlPath, spinner);
        });

        queue.add(any);
    }

    private void setupSpinnerAdapters(String[] dataList, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

    void onSpinnerOptionsSelection(Spinner any, final String spinnerType) {
        any.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerType.equals(getString(R.string.state))) {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString) && !selectedString.equals(getString(R.string.loading))) {
                        selectedState = selectedString;
                    }
                } else {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString) && !selectedString.equals(getString(R.string.loading))) {
                        selectedLga = selectedString;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void getAudrey (JSONObject uniquePerson) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Getting Audrey");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "save-person", uniquePerson, response -> {
            progressDialog.dismiss();
            Log.v("Sign up response", String.valueOf(response));
            finish();
        }, error -> {
            progressDialog.dismiss();
            Log.v("Sign up error", String.valueOf(error.getMessage()));
            Toast.makeText(this, "There was an error try again", Toast.LENGTH_SHORT).show();
        });
        queue.add(strRequest);
    }


    @Override
    public void clickSignup(View view) {
        getAudrey(registrationData);
    }
}
