package ng.apmis.audreymumplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;

/**
 * Created by Thadeus on 6/16/2018.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.user_email)
    TextInputEditText userEmail;
    @BindView(R.id.reset_password)
    Button resetPassword;
    @BindView(R.id.sign_in_tv)
    TextView signInTextView;


    boolean fieldsOk = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        ButterKnife.bind(this);

        resetPassword.setOnClickListener((view) ->
                checkFields()
        );

        signInTextView.setOnClickListener((view) ->
                finish()
        );


    }


    private void checkFields() {
        String email = userEmail.getText().toString();

        if (checkEmail(email)) {
            userEmail.setError("Check email");
        } else {
            fieldsOk = true;
        }

        if (fieldsOk) {
            InjectorUtils.provideJournalNetworkDataSource(this)
                    .resetPassword(this, userEmail.getText().toString());
            userEmail.setText("");
        }
    }

    private boolean checkEmail(String email) {
        return !email.contains("@") || email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
