package `in`.completecourse.questionbank.model

class Subject {
    private var mSubjectName: String? = null
    private var mIconResourceId = 0
    private var mClass: String? = null
    fun getmSubjectName(): String? {
        return mSubjectName
    }

    fun setmSubjectName(mSubjectName: String?) {
        this.mSubjectName = mSubjectName
    }

    fun getmIconResourceId(): Int {
        return mIconResourceId
    }

    fun setmIconResourceId(mIconResourceId: Int) {
        this.mIconResourceId = mIconResourceId
    }

    fun getmClass(): String? {
        return mClass
    }

    fun setmClass(mClass: String?) {
        this.mClass = mClass
    }
}