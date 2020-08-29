package in.completecourse.questionbank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class SplashActivity extends AppCompatActivity {
    private static String versionCodeApp;
    private static String versionCode;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_splash);

        db = FirebaseFirestore.getInstance();
        MobileAds.initialize(SplashActivity.this, SplashActivity.this.getResources().getString(R.string.admob_app_id));

        int versionCode = BuildConfig.VERSION_CODE;
        versionCodeApp = String.valueOf(versionCode);

        if (HelperMethods.INSTANCE.isNetworkAvailable(SplashActivity.this))
            checkVersionCode();
        else
            Toast.makeText(SplashActivity.this, "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
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
        db.collection("flags").document("version_code").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}