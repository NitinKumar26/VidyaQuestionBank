package in.completecourse.questionbank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

//import com.adcolony.sdk.AdColony;
//import com.adcolony.sdk.AdColonyAppOptions;
//import com.google.ads.mediation.adcolony.AdColonyAdapterUtils;
//import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.ads.mediation.unity.UnityMediationAdapter;
import com.google.android.gms.ads.AdFormat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.metadata.MetaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class SplashActivity extends AppCompatActivity {
    private static String versionCodeApp;
    private static String versionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_splash);


        //GDPR consent for Unity Personalized Ads
        MetaData metaData = new MetaData(this);
        metaData.set("gdpr.consent", true);
        metaData.commit();

        MobileAds.initialize(SplashActivity.this, getString(R.string.admob_app_id));

        Bundle unityInterstitial = new Bundle();
        unityInterstitial.putString("gameId", getString(R.string.unity_game_id));
        unityInterstitial.putString("zoneId", getString(R.string.unity_interstitial_placement_id));

        Bundle unityBanner = new Bundle();
        unityBanner.putString("gameId", getString(R.string.unity_game_id));
        unityBanner.putString("zoneId", getString(R.string.unity_banner));

        List<MediationConfiguration> unityConfig = new ArrayList<>();
        unityConfig.add(new MediationConfiguration(AdFormat.INTERSTITIAL, unityInterstitial));
        unityConfig.add(new MediationConfiguration(AdFormat.BANNER, unityBanner));

        UnityMediationAdapter adapter  = new UnityMediationAdapter();
        adapter.initialize(this, new InitializationCompleteCallback() {
            @Override
            public void onInitializationSucceeded() {}
            @Override
            public void onInitializationFailed(String s) { Log.e("unityInit", s); }
        }, unityConfig);


        int versionCode = BuildConfig.VERSION_CODE;
        versionCodeApp = String.valueOf(versionCode);

        if (HelperMethods.INSTANCE.isNetworkAvailable(SplashActivity.this)) checkVersionCode();
        else Toast.makeText(SplashActivity.this, "Please Check your Internet Connection", Toast.LENGTH_LONG).show();

        if (BuildConfig.DEBUG){
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("808EBC3F3CDB7990C5E47717B824C7AC")).build();
            MobileAds.setRequestConfiguration(configuration);
        }
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private void checkVersionCode(){
        FirebaseFirestore.getInstance().collection("flags").document("version_code").get()
                .addOnSuccessListener(documentSnapshot -> {
                    versionCode = String.valueOf(documentSnapshot.get("version_code_qb"));
                    if (versionCode.equalsIgnoreCase(versionCodeApp)){
                        int SPLASH_TIME_OUT = 2000;
                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */
                        new Handler().postDelayed(() -> {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            // Session manager
                            PrefManager session = new PrefManager(getApplicationContext());

                            if (session.isFirstTimeLaunch()){
                                //First time user (WelcomeActivity)
                                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (session.isLoggedIn()){
                                //user already logged in (MainActivity)
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                //user is required to login (SignupActivity)
                                Intent intent = new Intent(SplashActivity.this,SignupActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },SPLASH_TIME_OUT);
                    }else{
                        Intent intent = new Intent(SplashActivity.this, UpdateVersionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(e -> Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}