package ng.apmis.audreymumplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Thadeus on 6/16/2018.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.user_email)
    EditText userEmail;
    @BindView(R.id.phone_edittext)
    EditText phoneNumber;
    @BindView(R.id.reset_password)
    Button resetPassword;
    @BindView(R.id.sign_in_tv)
    TextView signInTextView;

    ProgressDialog progressDialog;

    boolean fieldsOk = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        ButterKnife.bind(this);

        resetPassword.setOnClickListener((view) ->
                Toast.makeText(this, "yeah", Toast.LENGTH_SHORT).show()
        );

        signInTextView.setOnClickListener((view) ->
                startActivity(new Intent(this, LoginActivity.class))
        );


    }


    private void checkFields() {
        String email = userEmail.getText().toString();
        String password = phoneNumber.getText().toString();

        if (!checkEmail(email)) {
            userEmail.setError("Check Apmis ID");
        } else {
            fieldsOk = true;
        }

        if (!checkPassword(password)) {
            phoneNumber.setError("Check password !!!");
            fieldsOk = false;
        } else {
            fieldsOk = true;
        }

        if (fieldsOk) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Signing In");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            AudreyMumplus.getInstance().networkIO().execute(() -> {

                JSONObject job = new JSONObject();
                try {
                    job.put("email", email);
                    job.put("password", password);
                    job.put("strategy", "local");
                    Log.v("Person to Json", String.valueOf(job));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, "" + "authentication", job, response -> {
                    Log.v("Login response", String.valueOf(response));

                    try {
                        Log.v("accessToken", response.getString("accessToken"));
                        Log.v("user", response.getString("user"));
                        String token = response.getString("accessToken");

                        JSONObject userObj = response.getJSONObject("user");

                        String personId = userObj.getString("personId");
                        String mEmail = userObj.getString("email");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    finish();
                }, error -> {
                    Log.d("error", String.valueOf(error.getMessage()) + "Error");
                    progressDialog.dismiss();
                    new AlertDialog.Builder(ForgotPasswordActivity.this)
                            .setTitle("Login Failed")
                            .setMessage("Please try again !!!")
                            .setPositiveButton("Dismiss", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                });

                //queue.add(loginRequest);
        });

    }
    }

    private boolean checkEmail (String email) {
        return !email.contains("@") || email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkPassword (String password) {
        return !TextUtils.isEmpty(password);
    }

    public void forgotPassword(View view) {
        // startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }
}
