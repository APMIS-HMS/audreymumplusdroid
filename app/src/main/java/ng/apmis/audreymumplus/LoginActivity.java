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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

/**
 * Created by Thadeus on 6/15/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://audrey-mum.herokuapp.com/";
    RequestQueue queue;

    boolean fieldsOk = false;

    SharedPreferencesManager sharedPreferencesManager;
    ProgressDialog progressDialog;

    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passswordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ButterKnife.bind(this);

        queue = Volley.newRequestQueue(this.getApplicationContext());

        sharedPreferencesManager = new SharedPreferencesManager(this.getApplicationContext());

        signInBtn.setOnClickListener((view) -> checkFields());

     /*   rememberMe.setOnClickListener((view) -> {
            if (((CheckBox) view).isChecked()) {
                sharedPreferencesManager.storeUserPassword(passwordEditText.getText().toString());
            } else {
                sharedPreferencesManager.storeUserPassword("");
            }
        });*/

     /*   createAccount.setOnClickListener( (view) -> startActivity (new Intent(this, SignupActivity.class)) );

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkFields();
                return true;
            }
            return false;
        });*/



    }

    private void attemptLogin(String email, String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        sharedPreferencesManager.storeUserEmail(email);

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
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {
                Log.v("Login response", String.valueOf(response));

                try {
                    Log.v("accessToken", response.getString("accessToken"));
                    Log.v("user", response.getString("user"));
                    String token = response.getString("accessToken");

                    JSONObject userObj = response.getJSONObject("user");

                    String personId = userObj.getString("personId");
                    String mEmail = userObj.getString("email");
                    String dbId = userObj.getString("_id");


                    sharedPreferencesManager.storeLoggedInUserKeys(token, personId, mEmail, dbId);

                    Log.v("sharedPRef", String.valueOf(sharedPreferencesManager.storedLoggedInUser()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }, error -> {
                Log.d("error", String.valueOf(error.getMessage()) + "Error");
                progressDialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login Failed")
                        .setMessage("Please try again !!!")
                        .setPositiveButton("Dismiss", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });

            queue.add(loginRequest);

        });


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


    private void checkFields() {
        String email = emailEditText.getText().toString();
        String password = passswordEditText.getText().toString();

        if (!checkEmail(email)) {
            emailEditText.setError("Check Apmis ID");
        } else {
            fieldsOk = true;
        }

        if (!checkPassword(password)) {
            passswordEditText.setError("Check password !!!");
            fieldsOk = false;
        } else {
            fieldsOk = true;
        }

        if (fieldsOk) {
            attemptLogin(email, password);
        }

    }


}
