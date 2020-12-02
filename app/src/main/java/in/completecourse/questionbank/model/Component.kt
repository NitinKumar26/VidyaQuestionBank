package `in`.completecourse.questionbank.model

import android.graphics.drawable.Drawable

class Component {
    private var mComponentName: String? = null
    private var mComponentKiId: String? = null
    private var mComponentURL: String? = null
    var cardBackground: Drawable? = null
    fun getmComponentName(): String? {
        return mComponentName
    }

    fun setmComponentName(mComponentName: String?) {
        this.mComponentName = mComponentName
    }

    // --Commented out by Inspection START (2/11/19 12:17 AM):
    //    public String getmComponentKiId() {
    //        return mComponentKiId;
    //    }
    // --Commented out by Inspection STOP (2/11/19 12:17 AM)
    fun setmComponentKiId(mComponentKiId: String?) {
        this.mComponentKiId = mComponentKiId
    }

    fun getmComponentURL(): String? {
        return mComponentURL
    }

    fun setmComponentURL(mComponentURL: String?) {
        this.mComponentURL = mComponentURL
    }
}