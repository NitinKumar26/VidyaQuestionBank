package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.SplashActivity
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.helper.PrefManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.ads.mediation.unity.UnityMediationAdapter
import com.google.android.gms.ads.AdFormat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.mediation.InitializationCompleteCallback
import com.google.android.gms.ads.mediation.MediationConfiguration
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.unity3d.ads.metadata.MetaData
import java.util.*

//import com.adcolony.sdk.AdColony;
//import com.adcolony.sdk.AdColonyAppOptions;
//import com.google.ads.mediation.adcolony.AdColonyAdapterUtils;
//import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        changeStatusBarColor()
        setContentView(R.layout.activity_splash)


        //GDPR consent for Unity Personalized Ads
        val metaData = MetaData(this)
        metaData["gdpr.consent"] = true
        metaData.commit()
        MobileAds.initialize(this@SplashActivity, getString(R.string.admob_app_id))
        val unityInterstitial = Bundle()
        unityInterstitial.putString("gameId", getString(R.string.unity_game_id))
        unityInterstitial.putString("zoneId", getString(R.string.unity_interstitial_placement_id))
        val unityBanner = Bundle()
        unityBanner.putString("gameId", getString(R.string.unity_game_id))
        unityBanner.putString("zoneId", getString(R.string.unity_banner))
        val unityConfig: MutableList<MediationConfiguration> = ArrayList()
        unityConfig.add(MediationConfiguration(AdFormat.INTERSTITIAL, unityInterstitial))
        unityConfig.add(MediationConfiguration(AdFormat.BANNER, unityBanner))
        val adapter = UnityMediationAdapter()
        adapter.initialize(this, object : InitializationCompleteCallback {
            override fun onInitializationSucceeded() {}
            override fun onInitializationFailed(s: String) {
                Log.e("unityInit", s)
            }
        }, unityConfig)
        val versionCode = BuildConfig.VERSION_CODE
        versionCodeApp = versionCode.toString()
        if (HelperMethods.isNetworkAvailable(this@SplashActivity)) checkVersionCode()
        else Toast.makeText(this@SplashActivity, "Please Check your Internet Connection", Toast.LENGTH_LONG).show()

        if (BuildConfig.DEBUG) {
            val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf("808EBC3F3CDB7990C5E47717B824C7AC")).build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    companion object {
        private var versionCodeApp: String? = null
        private var versionCode: String? = null

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private fun checkVersionCode() {
        FirebaseFirestore.getInstance().collection("flags").document("version_code").get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    versionCode = documentSnapshot["version_code_qb"].toString()
                    if (versionCode.equals(versionCodeApp, ignoreCase = true)) {
                        val SPLASH_TIME_OUT = 2000
                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */Handler().postDelayed({

                            // This method will be executed once the timer is over
                            // Start your app main activity
                            // Session manager
                            val session = PrefManager(applicationContext)
                            if (session.isFirstTimeLaunch) {
                                //First time user (WelcomeActivity)
                                val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (session.isLoggedIn()) {
                                //user already logged in (MainActivity)
                                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                //user is required to login (SignupActivity)
                                val intent = Intent(this@SplashActivity, SignupActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }, SPLASH_TIME_OUT.toLong())
                    } else {
                        val intent = Intent(this@SplashActivity, UpdateVersionActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { e: Exception -> Toast.makeText(this@SplashActivity, e.message, Toast.LENGTH_SHORT).show() }
    }
}