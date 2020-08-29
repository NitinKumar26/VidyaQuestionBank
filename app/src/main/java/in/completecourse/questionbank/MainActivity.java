package in.completecourse.questionbank;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import in.completecourse.questionbank.fragment.HomeFragment;
import in.completecourse.questionbank.fragment.NewArrivalFragment;
import in.completecourse.questionbank.fragment.ProfileFragment;
import in.completecourse.questionbank.helper.HelperMethods;


public class MainActivity extends AppCompatActivity {
    // --Commented out by Inspection (2/11/19 12:17 AM):private TextView titleText;
    private boolean doubleBackToExitPressedOnce = false;
    private HomeFragment homeFragment;
    private NewArrivalFragment newArrivalFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //titleText = findViewById(R.id.toolbar_title);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new HideBottomViewOnScrollBehavior());
        homeFragment = new HomeFragment();
        newArrivalFragment = new NewArrivalFragment();
        profileFragment = new ProfileFragment();
        navigation.setOnNavigationItemSelectedListener(listener);
        HelperMethods.INSTANCE.loadFragment(homeFragment, MainActivity.this);
    }

    final BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    HelperMethods.INSTANCE.showFragment(homeFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(newArrivalFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(profileFragment, MainActivity.this);
                    return true;
                case R.id.navigation_new_arrival:
                    HelperMethods.INSTANCE.showFragment(newArrivalFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(homeFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(profileFragment, MainActivity.this);
                    return true;
                case R.id.navigation_profile:
                    HelperMethods.INSTANCE.showFragment(profileFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(newArrivalFragment, MainActivity.this);
                    HelperMethods.INSTANCE.hideFragment(homeFragment, MainActivity.this);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
