package in.completecourse.questionbank;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import in.completecourse.questionbank.fragment.EasyLoginFragment;
import in.completecourse.questionbank.helper.HelperMethods;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        HelperMethods.INSTANCE.loadFragment(new EasyLoginFragment(), this);
    }

}
