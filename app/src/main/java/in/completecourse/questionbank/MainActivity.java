package in.completecourse.questionbank;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.firebase.analytics.FirebaseAnalytics;

import in.completecourse.questionbank.fragment.HomeFragment;
import in.completecourse.questionbank.fragment.NewArrivalFragment;
import in.completecourse.questionbank.fragment.NotificationFragment;
import in.completecourse.questionbank.fragment.ProfileFragment;
import me.nitin.lib.NiceBottomBar;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //HelperMethods.changeStatusBarColor(MainActivity.this);

        toolbar = findViewById(R.id.toolbar_main);
        titleText = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(null);
        }

        //Obtain the FirebaseAnalytics instance
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        appBarLayout = findViewById(R.id.appBAr);
        //appBarLayout.setVisibility(View.INVISIBLE);
        //toolbar.setVisibility(View.GONE);

        NiceBottomBar navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new HideBottomViewOnScrollBehavior());
        navigation.setActiveItem(1);
        loadFragment(new HomeFragment());

        navigation.setBottomBarCallback(new NiceBottomBar.BottomBarCallback() {
            @Override
            public void onItemSelect(int i) {
                switch (i){
                    case 0:
                        loadFragment(new NewArrivalFragment());
                        titleText.setText("New Arrivals");
                        break;
                    case 1:
                        loadFragment(new HomeFragment());
                        titleText.setText("Home");
                        break;
                    case 2:
                        loadFragment(new NotificationFragment());
                        titleText.setText("Notifications");
                        break;
                    case 3:
                        loadFragment(new ProfileFragment());
                        titleText.setText("Profile");
                        break;
                }
            }

            @Override
            public void onItemReselect(int i) {

            }
        });
    }


    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    @SuppressWarnings("JavaDoc")
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
