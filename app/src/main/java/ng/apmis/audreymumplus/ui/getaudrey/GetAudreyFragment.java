package ng.apmis.audreymumplus.ui.getaudrey;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.AudreyMumplus;
import ng.apmis.audreymumplus.R;
import ng.apmis.audreymumplus.SignupFragmentA;
import ng.apmis.audreymumplus.data.network.MumplusNetworkDataSource;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class GetAudreyFragment extends Fragment {

    JSONObject registrationData;

    public String selectedState;
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
    TextInputEditText previousChildren;
    @BindView(R.id.lga_edittext)
    TextInputEditText lgaEdittext;

    @BindView(R.id.address_et)
    TextInputEditText address;
    @BindView(R.id.occupation_et)
    TextInputEditText occupation;
    @BindView(R.id.state_origin_spinner)
    Spinner stateOrigin;
    DialogFragment dialogfragment;


    static final String BASE_URL = "https://apmisapitest.azurewebsites.net/";

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_audrey, container, false);
        ButterKnife.bind(this, rootView);
        registrationData = new JSONObject();
        queue = Volley.newRequestQueue(getContext());

        stateOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = adapterView.getItemAtPosition(i).toString();
                if (!TextUtils.isEmpty(selectedString) && !selectedString.equals(getString(R.string.loading))) {
                    selectedState = selectedString;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        expectedDateDelivery.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                dialogfragment = new DatePicker();
                dialogfragment.show(getActivity().getFragmentManager(), "Select Deliver Date");
                return true;
            }
            return false;
        });

        getAudreyButton.setOnClickListener((view -> {
            if (checkFields()) {
                AudreyMumplus.getInstance().diskIO().execute(() -> {
                    InjectorUtils.provideRepository(getActivity()).getPerson().observe(this, person -> {
                        MumplusNetworkDataSource dataSource = InjectorUtils.provideJournalNetworkDataSource(getActivity());
                        dataSource.updateProfileGetAudrey(person.getPersonId(), registrationData, getActivity(), true);
                        getActivity().getSupportFragmentManager().popBackStack();
                    });
                });
            }
        }));

        return rootView;
    }

    boolean checkFields() {
        if (selectedState.equals("")) {
            Toast.makeText(getActivity(), "Please select State", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lgaEdittext.getText().toString().equals("")) {
            lgaEdittext.setError("Local Government field cannot be empty");
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
            registrationData.put("lga", lgaEdittext.getText().toString());
            registrationData.put("address", address.getText().toString());
            registrationData.put("occupation", occupation.getText().toString());
            registrationData.put("spousefullname", spouseFullname.getText().toString());
            registrationData.put("spousephone", spousePhone.getText().toString());
            registrationData.put("hospitalname", hostpitalName.getText().toString());
            registrationData.put("hospitalstate", hospitalState.getText().toString());
            registrationData.put("ExpectedDateOfDelivery", expectedDateDelivery.getText().toString());
            registrationData.put("noOfPreviousChildren", previousChildren.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setActionBarButton(true, "Get Audrey");
        ((DashboardActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DashboardActivity) getActivity()).setActionBarButton(false, getString(R.string.app_name));
        ((DashboardActivity) getActivity()).bottomNavVisibility(true);
    }

    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        }

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            EditText et = (EditText) getActivity().findViewById(R.id.expected_date_of_delivery_et);
            et.setText(String.format("%d/%d/%d", month + 1, day, year));

        }

    }


}
