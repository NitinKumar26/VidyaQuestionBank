package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.fragment.EasyLoginFragment
import `in`.completecourse.questionbank.helper.HelperMethods
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Load EasyLoginFragment
        HelperMethods.loadFragment(EasyLoginFragment(), this)
    }
}