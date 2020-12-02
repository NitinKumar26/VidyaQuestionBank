package `in`.completecourse.questionbank.model

class NotificationModel {
    var mHeading: String? = null
    var mSubHeading: String? = null
    var url: String? = null

    // --Commented out by Inspection START (2/11/19 12:17 AM):
    var serial: String? = null
    fun getmHeading(): String? {
        return mHeading
    }

    fun getmSubHeading(): String? {
        return mSubHeading
    }

    fun setmHeading(mHeading: String?) {
        this.mHeading = mHeading
    }

    fun setmSubHeading(mSubHeading: String?) {
        this.mSubHeading = mSubHeading
    }
    //    public NotificationModel(String heading, String subheading, String url){
    //        this.mHeading = heading;
    //        this.mSubHeading = subheading;
    //        this.url = url;
    //    }
    // --Commented out by Inspection STOP (2/11/19 12:17 AM)
}