package com.crossover.crosschat.android.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Objects;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/10/18.
 */
public class InputUtils {

    public static final String TAG = InputUtils.class.getSimpleName();

    public static Context context;

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity == null || editText == null)
            return;

        context = activity;

        try {
            InputMethodManager inputManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputManager).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void hideKeyboard() {
        if (context == null || !(context instanceof Activity))
            return;

        try {
            View currentFocus = ((Activity)context).getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(inputManager).hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
