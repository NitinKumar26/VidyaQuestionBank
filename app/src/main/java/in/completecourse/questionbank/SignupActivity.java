package in.completecourse.questionbank;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.completecourse.questionbank.fragment.EasyLoginFragment;
import in.completecourse.questionbank.fragment.RegistrationFragment1;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        loadFragment(new EasyLoginFragment());

    }

        /**
         * loading fragment into FrameLayout
         * @param fragment is the fragment which we want to load in our FrameLayout
         */
        private void loadFragment(Fragment fragment) {
            // load fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutSignup, fragment);
            transaction.commit();
        }
}
