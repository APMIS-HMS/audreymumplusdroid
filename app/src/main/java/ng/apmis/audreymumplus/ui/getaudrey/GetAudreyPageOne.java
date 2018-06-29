package ng.apmis.audreymumplus.ui.getaudrey;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.R;

/**
 * Created by Thadeus-APMIS on 6/29/2018.
 */

public class GetAudreyPageOne extends Fragment {

    @BindView(R.id.continue_btn)
    Button continueBtn;
    @BindView(R.id.surname_et)
    TextInputEditText surname;
    @BindView(R.id.firstname_et)
    TextInputEditText firstname;
    @BindView(R.id.other_name_et)
    TextInputEditText otherName;
    @BindView(R.id.state_origin_spinner)
    Spinner stateOrigin;
    @BindView(R.id.lga_spinner)
    Spinner lga;
    @BindView(R.id.tel_et)
    TextInputEditText phone;
    @BindView(R.id.age_et)
    TextInputEditText age;
    @BindView(R.id.email_et)
    TextInputEditText email;
    @BindView(R.id.address_et)
    TextInputEditText address;
    @BindView(R.id.occupation_et)
    TextInputEditText occupation;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one_get_audrey, container, false);
        ButterKnife.bind(this, rootView);

        ((GetAudreyActivity)getActivity()).getStateAndLga(getString(R.string.state), stateOrigin);
        ((GetAudreyActivity)getActivity()).getStateAndLga(getString(R.string.lga), lga);

        ((GetAudreyActivity)getActivity()).onSpinnerOptionsSelection(stateOrigin, getString(R.string.state));
        ((GetAudreyActivity)getActivity()).onSpinnerOptionsSelection(stateOrigin, getString(R.string.lga));


        continueBtn.setOnClickListener((view -> {
            if (checkFields()) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .add(R.id.get_audrey_container, new GetAudreyPageTwo())
                        .commit();
            }
        }));


        return rootView;
    }

    boolean checkFields () {
        //TODO Remove this line when functional
        return true;
        /*if (surname.getText().toString().equals("")) {
            surname.setError("This field cannot be empty");
            return false;
        }
        if (firstname.getText().toString().equals("")) {
            firstname.setError("This field cannot be empty");
            return false;
        }
        if (otherName.getText().toString().equals("")) {
            otherName.setError("This field cannot be empty");
            return false;
        }
        if (((GetAudreyActivity) getActivity()).selectedState.equals("") || ((GetAudreyActivity) getActivity()).selectedLga.equals(getString(R.string.loading))) {
            Toast.makeText(getActivity(), "Please select State", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (((GetAudreyActivity) getActivity()).selectedLga.equals("") || ((GetAudreyActivity) getActivity()).selectedLga.equals(getString(R.string.loading))) {
            Toast.makeText(getActivity(), "Please select Lga", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.getText().toString().equals("") || phone.getText().toString().length() < 11) {
            phone.setError("Please fill mobile number");
            return false;
        }
        if (age.getText().toString().equals("")) {
            age.setError("This field cannot be empty");
            return false;
        }
        if (email.getText().toString().equals("") || !email.getText().toString().contains("@")) {
            email.setError("Check Email");
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

        try {
            ((GetAudreyActivity)getActivity()).registrationData.put("surname", surname.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("firstname", firstname.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("othername", otherName.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("stateoforigin", ((GetAudreyActivity) getActivity()).selectedState);
            ((GetAudreyActivity)getActivity()).registrationData.put("lga", ((GetAudreyActivity) getActivity()).selectedLga);
            ((GetAudreyActivity)getActivity()).registrationData.put("telephone", phone.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("age", age.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("email", email.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("address", address.getText().toString());
            ((GetAudreyActivity)getActivity()).registrationData.put("occupation", occupation.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
        */
    }

}
