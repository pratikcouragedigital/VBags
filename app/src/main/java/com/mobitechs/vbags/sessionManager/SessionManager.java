package com.mobitechs.vbags.sessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.Account;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;      // Shared pref mode
    SessionManager sessionManager;

    private static final String IS_LOGIN = "IsLoggedIn";
    // Sharedpref file name
    private static final String PREF_NAME = "Preference";
    // Email address (make variable public to access from outside)
    public static final String KEY_USERID = "userId";
    public static final String KEY_USERTYPE = "userType";
    public static final String KEY_FIRST_NAME = "userFirstName";
    public static final String KEY_LAST_NAME = "userLastName";
    public static final String KEY_CONTACTNO = "contactNo";
    public static final String KEY_USEREMAIL = "userEmail";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_DEVICEID = "deviceId";
    public static final String KEY_MANUFACTURER = "manufacturer";
    public static final String KEY_MODEL = "model";
    public static final String KEY_FINGERPRINT = "fingerPrint";
    public static final String KEY_LUCKYDRAW_AMOUNT = "luckyDrawAmount";
    public static final String KEY_IS_LUCKYDRAW_DONE = "isLuckyDrawDone";
    public static final String KEY_CART_BADGE = "cartBadge";

    public SessionManager(Context c) {
        this.context = c;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String saveUserId, String userFirstName,String userLastName, String userEmail, String userContactNo, String userType) {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

        editor.putString(KEY_USERID, saveUserId);
        editor.putString(KEY_USERTYPE, userType);
        editor.putString(KEY_FIRST_NAME, userFirstName);
        editor.putString(KEY_LAST_NAME, userLastName);
        editor.putString(KEY_CONTACTNO, userContactNo);
        editor.putString(KEY_USEREMAIL, userEmail);

//         commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_CONTACTNO, pref.getString(KEY_CONTACTNO, null));
        user.put(KEY_USEREMAIL, pref.getString(KEY_USEREMAIL, null));
        user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
        // return user
        return user;
    }


    public void SetLuckyDrawAmount(String amount,String isDone) {

        editor.putString(KEY_LUCKYDRAW_AMOUNT, amount);
        editor.putString(KEY_IS_LUCKYDRAW_DONE, isDone);
        editor.commit();
    }

    public HashMap<String, String> getLuckyDrawAmount() {
        HashMap<String, String> luckyAmount = new HashMap<String, String>();

        luckyAmount.put(KEY_LUCKYDRAW_AMOUNT, pref.getString(KEY_LUCKYDRAW_AMOUNT, null));
        luckyAmount.put(KEY_IS_LUCKYDRAW_DONE, pref.getString(KEY_IS_LUCKYDRAW_DONE, null));
        // return user
        return luckyAmount;
    }


    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Account.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        } else {

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        }
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, MainActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setCartBadge(String cartBadge) {
        editor.putString(KEY_CART_BADGE, cartBadge);
        editor.commit();
    }

    public String getCartBadge() {
        return pref.getString(KEY_CART_BADGE, "0");
    }

    public void createUserFirebaseNotificationToken(String token, String deviceId,String manufacturer, String model,String fingerPrint) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_DEVICEID, deviceId);
        editor.putString(KEY_MANUFACTURER, manufacturer);
        editor.putString(KEY_MODEL, model);
        editor.putString(KEY_FINGERPRINT, fingerPrint);
        editor.commit();
    }

    public HashMap<String, String> getUserFirebaseNotificationToken() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_DEVICEID, pref.getString(KEY_DEVICEID, null));
        user.put(KEY_MANUFACTURER, pref.getString(KEY_MANUFACTURER, null));
        user.put(KEY_MODEL, pref.getString(KEY_MODEL, null));
        user.put(KEY_FINGERPRINT, pref.getString(KEY_FINGERPRINT, null));

        return user;
    }
}
