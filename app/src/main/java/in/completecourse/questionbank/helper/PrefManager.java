package in.completecourse.questionbank.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.completecourse.questionbank.SignupActivity;

public class PrefManager {

    //Shared preferences
    private final SharedPreferences pref;
    //Editor for shared preferences
    private SharedPreferences.Editor editor;
    //Context
    private Context mContext;

    // Shared preferences file name
    private static final String PREF_NAME = "vidya_question_bank";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_CLASS = "user_class";
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";

    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        this.mContext = context;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        // apply changes
        editor.apply();
    }

    public void setUserDetails(String userId, String userEmail){
        editor = pref.edit();
        editor.putString(USER_ID, userId);
        editor.putString(USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getUserId(){
        return pref.getString(USER_ID, "");
    }

    public String getUserEmail(){
        return  pref.getString(USER_EMAIL, "");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUserClass(String userClass){
        editor.putString(USER_CLASS, userClass);
        editor.commit();
    }

    public String getUserClass(){
        return pref.getString(USER_CLASS, "");
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor = pref.edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, SignupActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
    }
}
