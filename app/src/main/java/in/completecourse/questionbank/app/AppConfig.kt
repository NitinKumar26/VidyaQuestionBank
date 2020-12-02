package `in`.completecourse.questionbank.app

object AppConfig {
    //Login URL
    const val URL_LOGIN = "http://qb.vidyastore.in/api/UserAuth"

    //SignUp URL
    const val URL_SIGNUP = "http://qb.vidyastore.in/api/UserRegistration"

    //User success URL
    const val URL_USER_SUCCESS = "http://qb.vidyastore.in/api/UserSuccess"

    //Reset Password URL
    const val URL_RESET_PASSWORD = "http://qb.vidyastore.in/api/updatePassword"

    //User details URL
    const val URL_USER_DETAILS = "http://qb.vidyastore.in/api/UserDetails"

    //Latest Books URL
    const val URL_LATEST_BOOKS = "http://completecourse.in/api/newarrivals.php"

    //Request Book
    const val URL_REQUEST_BOOK = "http://qb.vidyastore.in/api/submitRequest"

    //Class Chapters URL
    const val URL_ACTIVITIES = "http://qb.vidyastore.in/api/GetActivities"

    //Class Chapters URL
    const val URL_COMPONENTS = "http://qb.vidyastore.in/api/GetComponents"

    //Class Updates
    const val URL_COMPETITION_UPDATES = "http://completecourse.in/api/competition.php"

    //Notification URL
    const val URL_NOTIFICATION = "http://completecourse.in/api/GetNotifications"

    //URL Send OTP for password reset
    const val URL_SEND_OTP = "http://qb.vidyastore.in/api/sendOTP"
}