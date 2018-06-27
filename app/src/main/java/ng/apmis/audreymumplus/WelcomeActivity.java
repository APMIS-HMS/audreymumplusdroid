package ng.apmis.audreymumplus;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import ng.apmis.audreymumplus.ui.Dashboard.DashboardActivity;
import ng.apmis.audreymumplus.ui.onboarding.OnboardingActivity;
import ng.apmis.audreymumplus.utils.SharedPreferencesManager;

/**
 * Created by Thadeus-APMIS on 6/20/2018.
 */

public class WelcomeActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        new Handler().postDelayed(() -> {
            if (sharedPreferencesManager.isFirstTimeLaunch()) {
                startActivity(new Intent(this, OnboardingActivity.class));
                finish();
            } else {
                if (sharedPreferencesManager.getUserToken().equals("")) {
                    startActivity(new Intent(this, DashboardActivity.class));
                    //startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(this, DashboardActivity.class));
                    finish();
                }
            }
        }, 1000);


    }
}
