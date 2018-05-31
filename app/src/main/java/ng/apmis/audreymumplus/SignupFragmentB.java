package ng.apmis.audreymumplus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thadeus-APMIS on 5/31/2018.
 */

public class SignupFragmentB extends Fragment {

    @BindView(R.id.maiden_name)
    TextInputEditText maidenNameEditText;
    @BindView(R.id.phone)
    TextInputEditText phoneEditText;
    @BindView(R.id.security_questions)
    Spinner securityQuestionSpinner;
    @BindView(R.id.security_answer)
    TextInputEditText securityAnswer;
    @BindView(R.id.sign_up_btn)
    Button signupBtn;

    String securityQuestionText;

    OnFragmentInteractionListener onFragmentInteractionListener;

    public SignupFragmentB () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_b, container, false);
        ButterKnife.bind(this, rootView);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, getActivity().getResources().getStringArray(R.array.Security));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        securityQuestionSpinner.setAdapter(spinnerAdapter);

        securityQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = adapterView.getItemAtPosition(i).toString();
                if (!TextUtils.isEmpty(selectedString)) {
                    securityQuestionText = selectedString;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        signupBtn.setOnClickListener((view) -> {
            try {
                if (checkFields()) {
                    onFragmentInteractionListener.onClickView(view);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    boolean checkFields() throws JSONException {

        if (true) return  true;

        if (maidenNameEditText.getText().toString().length() < 2) {
            maidenNameEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (phoneEditText.getText().toString().length() < 11) {
            phoneEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (TextUtils.isEmpty(securityQuestionText)) {
            Toast.makeText(this.getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(securityAnswer.getText().toString())) {
            securityAnswer.setError(getString(R.string.input_error));
            return false;
        }

        SignupActivity.audreyMum.put("motherMaidenName", maidenNameEditText.getText().toString());
        SignupActivity.audreyMum.put("primaryContactPhoneNo", phoneEditText.getText().toString());
        SignupActivity.audreyMum.put("securityQuestion", securityQuestionText);
        SignupActivity.audreyMum.put("securityAnswer", securityAnswer.getText().toString());

        return true;
    }

    public interface OnFragmentInteractionListener {
        void onClickView (View view);
    }
}
