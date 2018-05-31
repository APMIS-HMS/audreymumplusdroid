package ng.apmis.audreymumplus;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thadeus-APMIS on 5/31/2018.
 */

public class SignupFragmentA extends Fragment {

    @BindView(R.id.title_spinner)
    Spinner titleSpinner;
    @BindView(R.id.firstname_et)
    TextInputEditText firstNameEditText;
    @BindView(R.id.lastname_et)
    TextInputEditText lastNameEditText;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.dob_et)
    TextInputEditText dobEditText;
    @BindView(R.id.continue_btn)
    Button continueBtn;

    String titleText;
    String genderText;


    public SignupFragmentA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_a, container, false);

        ButterKnife.bind(this, rootView);


        setupSpinnerAdapters(getActivity().getResources().getStringArray(R.array.Gender), genderSpinner);
        setupSpinnerAdapters(getActivity().getResources().getStringArray(R.array.title), titleSpinner);
        onSpinnerOptionsSelection(titleSpinner, getResources().getString(R.string.title));
        onSpinnerOptionsSelection(genderSpinner, "other");

        continueBtn.setOnClickListener((view) -> {
            try {
                if (checkFields()) {
                    SignupActivity.placeFragment(new SignupFragmentB(), getFragmentManager(), "B-SIGN-UP");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return rootView;
    }

    private void setupSpinnerAdapters(String[] dataList, Spinner spinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

    void onSpinnerOptionsSelection(Spinner any, final String spinnerType) {
        any.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerType.equals(getString(R.string.title))) {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString)) {
                        genderText = selectedString;
                    }
                } else {
                    String selectedString = adapterView.getItemAtPosition(i).toString();
                    if (!TextUtils.isEmpty(selectedString)) {
                        titleText = selectedString;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    boolean checkFields() throws JSONException {
        //TODO remove after test
        if (true) return true;

        Pattern p = Pattern.compile("([0-9])");
        Matcher m = p.matcher(firstNameEditText.getText().toString());

        if (TextUtils.isEmpty(titleText)) {
            Toast.makeText(this.getActivity(), "Select Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (firstNameEditText.getText().toString().length() < 1 || m.find()) {
            firstNameEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (lastNameEditText.getText().toString().length() < 1 || m.find()) {
            lastNameEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (TextUtils.isEmpty(genderText)) {
            Toast.makeText(this.getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(dobEditText.getText().toString())) {
            dobEditText.setError(getString(R.string.input_error));
            return false;
        }

        SignupActivity.audreyMum.put("title", titleText);
        SignupActivity.audreyMum.put("firstName", firstNameEditText.getText().toString());
        SignupActivity.audreyMum.put("lastName", lastNameEditText.getText().toString());
        SignupActivity.audreyMum.put("gender", genderText);
        SignupActivity.audreyMum.put("dateOfBirth", dobEditText.getText().toString());

        return true;
    }


}
