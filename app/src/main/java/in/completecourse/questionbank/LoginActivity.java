package in.completecourse.questionbank;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.completecourse.questionbank.fragment.EasyLoginFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loadFragment(new EasyLoginFragment());
    }

    @Override
    public void onClick(View v) {

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
