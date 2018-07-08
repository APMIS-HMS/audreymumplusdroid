package ng.apmis.audreymumplus.ui.getaudrey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class GetAudreyFragment extends Fragment{

    JSONObject registrationData;

    public String selectedState;
    public String selectedLga;
    RequestQueue queue;

    @BindView(R.id.submit_btn)
    Button getAudreyButton;
    @BindView(R.id.spouse_fullname)
    TextInputEditText spouseFullname;
    @BindView(R.id.spouse_phone)
    TextInputEditText spousePhone;
    @BindView(R.id.hospital_name)
    TextInputEditText hostpitalName;
    @BindView(R.id.hostpital_state_et)
    TextInputEditText hospitalState;
    @BindView(R.id.expected_date_of_delivery_et)
    TextInputEditText expectedDateDelivery;
    @BindView(R.id.previous_children)
    TextInputEditText previousChildren ;

    @BindView(R.id.address_et)
    TextInputEditText address;
    @BindView(R.id.occupation_et)
    TextInputEditText occupation;
    @BindView(R.id.state_origin_spinner)
    Spinner stateOrigin;
    @BindView(R.id.lga_spinner)
    Spinner lga;


    static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_audrey, container, false);
        ButterKnife.bind(this, rootView);
        registrationData = new JSONObject();
        queue = Volley.newRequestQueue(getContext());

        getAudreyButton.setOnClickListener((view -> {
            if (checkFields()) {
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(getActivity()).getPerson().observe(this, person -> {
                        InjectorUtils.provideJournalNetworkDataSource(getActivity()).updateProfileGetAudrey(person.getPersonId(), registrationData, getActivity(), true);
                    });
                });
            }
        }));

        return rootView;
    }

    boolean checkFields () {
        if (selectedState.equals("") || selectedLga.equals(getString(R.string.loading))) {
            Toast.makeText(getActivity(), "Please select State", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedLga.equals("") || selectedLga.equals(getString(R.string.loading))) {
            Toast.makeText(getActivity(), "Please select Lga", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address.getText().toString().equals("")) {
            address.setError("Address field cannot be empty");
            return false;
        }
        if (occupation.getText().toString().equals("")) {
            occupation.setError("Input occupation");
            return false;
        }

        if (spouseFullname.getText().toString().equals("")) {
            spouseFullname.setError("Please fill spouse name");
            return false;
        }
        if (spousePhone.getText().toString().equals("")) {
            spousePhone.setError("Please fill phone number");
            return false;
        }
        if (hostpitalName.getText().toString().equals("")) {
            hostpitalName.setError("Please fill hospital name");
            return false;
        }
        if (hospitalState.getText().toString().equals("")) {
            hospitalState.setError("Please hospital state");
            return false;
        }
        if (expectedDateDelivery.getText().toString().equals("")) {
            expectedDateDelivery.setError("Please fill Expected Date of Delivery");
            return false;
        }
        if (previousChildren.getText().toString().equals("")) {
            previousChildren.setError("Please enter number of previous children");
            return false;
        }

        try {
            registrationData.put("stateoforigin", selectedState);
            registrationData.put("lga", selectedLga);
            registrationData.put("address", address.getText().toString());
            registrationData.put("occupation", occupation.getText().toString());
            registrationData.put("spousefullname", spouseFullname.getText().toString());
            registrationData.put("spousephone", spousePhone.getText().toString());
            registrationData.put("hospitalname", hostpitalName.getText().toString());
            registrationData.put("hospitalstate", hospitalState.getText().toString());
            registrationData.put("edd", expectedDateDelivery.getText().toString());
            registrationData.put("noOfPreviousChildren", previousChildren.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
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
                    getActivity().runOnUiThread(() -> setupSpinnerAdapters(gendersArray, spinner));
                }
                if (urlPath.equals(getString(R.string.lga))) {
                    final String[] securityQuestionsArray = new String[jar.length()];
                    for (int i = 0; i < jar.length(); i++) {
                        securityQuestionsArray[i] = jar.getJSONObject(i).getString("name");
                    }
                    getActivity().runOnUiThread(() -> setupSpinnerAdapters(securityQuestionsArray, spinner));
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
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, dataList);
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


   /* @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll(new RequestQueue.RequestFilter() {
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }*/
}
