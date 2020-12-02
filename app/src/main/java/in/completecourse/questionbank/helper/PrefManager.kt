package `in`.completecourse.questionbank.helper

import `in`.completecourse.questionbank.SignupActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class PrefManager(context: Context) {
    //Shared preferences
    private val pref: SharedPreferences

    //Editor for shared preferences
    private var editor: SharedPreferences.Editor? = null

    //Context
    private val mContext: Context
    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor = pref.edit()
            editor?.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor?.apply()
        }

    fun setLogin(isLoggedIn: Boolean) {
        editor = pref.edit()
        editor?.putBoolean(IS_LOGGED_IN, isLoggedIn)
        // apply changes
        editor?.apply()
    }

    fun setUserDetails(userId: String?, userEmail: String?) {
        editor = pref.edit()
        editor?.putString(USER_ID, userId)
        editor?.putString(USER_EMAIL, userEmail)
        editor?.apply()
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
    val userId: String?
        get() = pref.getString(USER_ID, "")

    // --Commented out by Inspection START (2/11/19 12:17 AM):
    // --Commented out by Inspection START (2/11/19 12:17 AM):
    ////    public String getUserEmail(){
    ////        return  pref.getString(USER_EMAIL, "");
    ////    }
    //// --Commented out by Inspection STOP (2/11/19 12:17 AM)
    // --Commented out by Inspection STOP (2/11/19 12:17 AM)
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGGED_IN, false)
    }

    fun setUserClass(userClass: String?) {
        editor?.putString(USER_CLASS, userClass)
        editor?.commit()
    }
    // --Commented out by Inspection START (2/11/19 12:17 AM):
    //    public String getUserClass(){
    //        return pref.getString(USER_CLASS, "");
    //    }
    // --Commented out by Inspection STOP (2/11/19 12:17 AM)
    /**
     * Clear session details
     */
    fun logoutUser() {
        editor = pref.edit()
        // Clearing all data from Shared Preferences
        editor?.clear()
        editor?.apply()

        // After logout redirect user to Login Activity
        val i = Intent(mContext, SignupActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        // Staring Login Activity
        mContext.startActivity(i)
    }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "vidya_question_bank"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USER_CLASS = "user_class"
        private const val USER_ID = "user_id"
        private const val USER_EMAIL = "user_email"
        private const val USER_NAME = "user_name"
        private const val USER_CONTACT = "user_contact"
    }

    init {
        // shared pref mode
        val PRIVATE_MODE = 0
        mContext = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }
}