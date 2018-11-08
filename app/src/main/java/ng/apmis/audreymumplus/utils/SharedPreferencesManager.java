package ng.apmis.audreymumplus.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ng.apmis.audreymumplus.ui.Chat.ChatContextFragment;
import ng.apmis.audreymumplus.ui.Chat.ChatContextModel;

public class SharedPreferencesManager {

    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    private static final String IS_LOGGED_IN = "isLoggedIn";

    private static final String ACCESS_TOKEN = "token";

    private static final String PERSON_ID = "pid";

    private static final String EMAIL = "email";

    private static final String DB_ID = "dbib";

    private static final String APMIS_ID = "apmisid";

    public static final String USER_PASSWORD = "password";

    private static final String SERVER_ROOM_COUNT = "forumjoin";

    private static final String LAST_CHAT_TIME_FORUM_LIST_STRING = "lastchat";

    private static final String USER_ID = "userid";


    public SharedPreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, _context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public ChatContextFragment.ForumNameAndLastDate getLastChatForumAndCreatedAt (String forumName) {
        String item = pref.getString(forumName, "");
        return new Gson().fromJson(item, ChatContextFragment.ForumNameAndLastDate.class);
    }

    public void addForumNameAndLastCreatedAtAsStringInPrefs(String forumName, String createdAt, int lastChatPosition) {
        editor.putString(forumName, new Gson().toJson(new ChatContextFragment.ForumNameAndLastDate(forumName,createdAt, lastChatPosition)));
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setJustLoggedIn (boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean justLoggedIn () {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public int getTotalRoomCountOnserver () {
        return pref.getInt(SERVER_ROOM_COUNT, 0);
    }

    public void setTotalRoomCountOnserver (int totalRoomCount) {
        editor.putInt(SERVER_ROOM_COUNT, totalRoomCount);
        editor.commit();
    }

    public void storeUserEmail(String email) {
        editor.putString(EMAIL, EncryptionUtils.encrypt(email));
        editor.commit();
    }

    public String getStoredEmail () {
        return EncryptionUtils.decrypt(pref.getString(EMAIL, ""));
    }

    public void storeUserToken (String token) {
        editor.putString(ACCESS_TOKEN, EncryptionUtils.encrypt(token));
        editor.commit();
    }

    public String getUserToken () {
        return EncryptionUtils.decrypt(pref.getString(ACCESS_TOKEN, ""));
    }

    public void storeUserPassword (String password) {
        editor.putString(USER_PASSWORD, EncryptionUtils.encrypt(password));
        editor.commit();
    }

    public String getStoredUserPassword () {
        return EncryptionUtils.decrypt(pref.getString(USER_PASSWORD, ""));
    }


    public void storeUser_id(String _id) {
        editor.putString(USER_ID, _id);
        editor.commit();
    }

    public String getUser_id () {
        return pref.getString(USER_ID, "");
    }
}
