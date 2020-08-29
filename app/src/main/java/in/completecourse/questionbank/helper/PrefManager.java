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
    private final Context mContext;

    // Shared preferences file name
    private static final String PREF_NAME = "vidya_question_bank";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_CLASS = "user_class";
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NAME = "user_name";
    private static final String USER_CONTACT = "user_contact";

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
        editor = pref.edit();
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

// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public void setUserData(String username, String userclass, String userContact, String userEmail){
//        editor = pref.edit();
//        editor.putString(USER_NAME, username);
//        editor.putString(USER_CLASS, userclass);
//        editor.putString(USER_CONTACT, userContact);
//        editor.putString(USER_EMAIL, userEmail);
//        editor.apply();
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public String getUserName(){
//        return pref.getString(USER_NAME, "");
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public String getUserId(){
        return pref.getString(USER_ID, "");
    }

// --Commented out by Inspection START (2/11/19 12:17 AM):
// --Commented out by Inspection START (2/11/19 12:17 AM):
////    public String getUserEmail(){
////        return  pref.getString(USER_EMAIL, "");
////    }
//// --Commented out by Inspection STOP (2/11/19 12:17 AM)
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUserClass(String userClass){
        editor.putString(USER_CLASS, userClass);
        editor.commit();
    }

// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public String getUserClass(){
//        return pref.getString(USER_CLASS, "");
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor = pref.edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to Login Activity
        Intent i = new Intent(mContext, SignupActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
    }
}
