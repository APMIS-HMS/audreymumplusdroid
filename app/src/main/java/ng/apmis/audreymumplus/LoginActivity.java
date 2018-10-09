package ng.apmis.audreymumplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.StringCallback;
import com.koushikdutta.async.http.socketio.transport.SocketIOTransport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.audreymumplus.data.database.Person;
import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.utils.InjectorUtils;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

/**
 * Created by Thadeus on 6/15/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BASE_URL = "https://audrey-mum.herokuapp.com/";
    RequestQueue queue;

    SharedPreferencesManager sharedPreferencesManager;
    ProgressDialog progressDialog;

    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.sign_up)
    TextView signupTv;
    @BindView(R.id.forgot_password_tv)
    TextView forgotPassword;
    @BindView(R.id.remember_me)
    CheckBox rememberMe;
    JSONArray jar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ButterKnife.bind(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", "YK-34680");
            jsonObject.put("password", "admin@01");
            jsonObject.put("strategy", "local");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jar = new JSONArray();

        jar.put(jsonObject);

        Uri uri = Uri.parse("https://audrey-mum.herokuapp.com");

        Log.v("URi string", uri.toString());

        SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), uri.toString(), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                if (ex != null) {
                    ex.printStackTrace();
                    Log.v("exception", ex.toString());
                    return;
                }

                client.setStringCallback(new StringCallback() {
                    @Override
                    public void onString(String string, Acknowledge acknowledge) {
                        Log.v("String callback", string);
                    }

                });
                client.on("someEvent", new EventCallback() {
                    @Override
                    public void onEvent(JSONArray argument, Acknowledge acknowledge) {
                        Log.v("someEvent", argument.toString());
                    }
                });
                client.setJSONCallback(new JSONCallback() {
                    @Override
                    public void onJSON(JSONObject json, Acknowledge acknowledge) {
                        Log.v("JSONCallback", json.toString());
                    }

                });
               /* client.emit("authenticate", jar, new Acknowledge() {
                    @Override
                    public void acknowledge(JSONArray arguments) {
                        Log.v("Authenticate", arguments.toString());
                    }
                });*/
            }
        });


        queue = Volley.newRequestQueue(this.getApplicationContext());

        sharedPreferencesManager = new SharedPreferencesManager(this.getApplicationContext());

        if (!TextUtils.isEmpty(sharedPreferencesManager.getStoredEmail())) {
            emailEditText.setText(sharedPreferencesManager.getStoredEmail());
        }

        if (!TextUtils.isEmpty(sharedPreferencesManager.getStoredUserPassword())) {
            passwordEditText.setText(sharedPreferencesManager.getStoredUserPassword());
            rememberMe.setChecked(true);
        }


        rememberMe.setOnClickListener(this);

        signInBtn.setOnClickListener((view) -> {
            if (checkFields()) {
                isRememberChecked();
                attemptLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        signupTv.setOnClickListener((view) ->
                startActivity(new Intent(this, SignupActivity.class))
        );

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (checkFields()) {
                    isRememberChecked();
                    attemptLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return true;
            }
            return false;
        });

        forgotPassword.setOnClickListener((view -> startActivity(new Intent(this, ForgotPasswordActivity.class))));

    }

    void isRememberChecked() {
        if (rememberMe.isChecked()) {
            sharedPreferencesManager.storeUserPassword(passwordEditText.getText().toString());
        } else {
            sharedPreferencesManager.storeUserPassword("");
        }
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

            //Put parameters in JSON Object
            JSONObject job = new JSONObject();
            try {
                job.put("email", email);
                job.put("password", password);
                job.put("strategy", "local");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + "authentication", job, response -> {

                try {
                    String token = response.getString("accessToken");
                    Person user = new Gson().fromJson(response.getJSONObject("user").toString(), Person.class);

                    sharedPreferencesManager.storeUserToken(token);

                    InjectorUtils.provideJournalNetworkDataSource(this).fetchPeopleAndSaveToDb(user.getPersonId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                sharedPreferencesManager.setJustLoggedIn(true);
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

    private boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkPassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    private boolean checkFields() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!checkEmail(email)) {
            emailEditText.setError("Check email");
            return false;
        }
        if (!checkPassword(password)) {
            passwordEditText.setError("Check password !!!");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        if (((CheckBox) view).isChecked()) {
            sharedPreferencesManager.storeUserPassword(passwordEditText.getText().toString());
        } else {
            sharedPreferencesManager.storeUserPassword("");
        }
    }


}
