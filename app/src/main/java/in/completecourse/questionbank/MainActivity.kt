package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.fragment.HomeFragment
import `in`.completecourse.questionbank.fragment.NewArrivalFragment
import `in`.completecourse.questionbank.fragment.ProfileFragment
import `in`.completecourse.questionbank.helper.HelperMethods
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // --Commented out by Inspection (2/11/19 12:17 AM):private TextView titleText;
    private var doubleBackToExitPressedOnce = false
    private var homeFragment: HomeFragment? = null
    private var newArrivalFragment: NewArrivalFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.itemIconTintList = null
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = HideBottomViewOnScrollBehavior<View?>()
        homeFragment = HomeFragment()
        newArrivalFragment = NewArrivalFragment()
        profileFragment = ProfileFragment()
        navigation.setOnNavigationItemSelectedListener(listener)
        HelperMethods.loadFragment(homeFragment, this@MainActivity)

    }

    val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                HelperMethods.showFragment(homeFragment, this@MainActivity)
                HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                HelperMethods.hideFragment(profileFragment, this@MainActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_new_arrival -> {
                HelperMethods.showFragment(newArrivalFragment, this@MainActivity)
                HelperMethods.hideFragment(homeFragment, this@MainActivity)
                HelperMethods.hideFragment(profileFragment, this@MainActivity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                HelperMethods.showFragment(profileFragment, this@MainActivity)
                HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                HelperMethods.hideFragment(homeFragment, this@MainActivity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}