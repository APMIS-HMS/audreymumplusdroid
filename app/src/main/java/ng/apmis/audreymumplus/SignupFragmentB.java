package ng.apmis.audreymumplus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    @BindView(R.id.email_edittext)
    TextInputEditText emailEditText;
    @BindView(R.id.password_edittext)
    TextInputEditText passwordEditText;
    @BindView(R.id.confirm_password_edittext)
    TextInputEditText confirmPassword;
    @BindView(R.id.sign_up_btn)
    Button signupBtn;

    boolean isPasswordMatch = false;
    OnFragmentInteractionListener onFragmentInteractionListener;

    public SignupFragmentB () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_b, container, false);
        ButterKnife.bind(this, rootView);

        signupBtn.setOnClickListener((view) -> {
            try {
                if (checkFields()) {
                    onFragmentInteractionListener.onClickView(view);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isPasswordMatch = editable.toString().equals(passwordEditText.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SignupActivity)getActivity()).setSupportActionTitle("Sign up", true);
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

        if (maidenNameEditText.getText().toString().length() < 2) {
            maidenNameEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (phoneEditText.getText().toString().length() < 11) {
            phoneEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (emailEditText.getText().toString().length() < 11 || !emailEditText.getText().toString().contains("@")) {
            emailEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError(getString(R.string.input_error));
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
            confirmPassword.setError(getString(R.string.input_error));
            return false;
        }
        if (!isPasswordMatch) {
            confirmPassword.setError("Password do no match");
            return false;
        }

        SignupActivity.audreyMum.put("motherMaidenName", maidenNameEditText.getText().toString());
        SignupActivity.audreyMum.put("primaryContactPhoneNo", phoneEditText.getText().toString());
        SignupActivity.audreyMum.put("email", emailEditText.getText().toString());
        SignupActivity.audreyMum.put("password", confirmPassword.getText().toString());

        return true;
    }

    public interface OnFragmentInteractionListener {
        void onClickView (View view);
    }
}
