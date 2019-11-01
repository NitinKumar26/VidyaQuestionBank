package in.completecourse.questionbank.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.questionbank.R;

public class RegistrationFragment1 extends Fragment{
    @BindView(R.id.edTv_name)
    TextInputEditText edTvName;
    @BindView(R.id.edTv_email)
    TextInputEditText edTvEmail;
    @BindView(R.id.edTv_mobile_number)
    TextInputEditText edTvMobileNumber;
    @BindView(R.id.edTv_password)
    TextInputEditText edTvPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_signup)
    void signUp(){
        String name, email, mobileNumber, password;
        if (edTvName.getText() != null && edTvEmail.getText() != null && edTvMobileNumber.getText() != null &&  edTvPassword.getText() != null){
            name = edTvName.getText().toString().trim();
            email = edTvEmail.getText().toString().trim();
            mobileNumber = edTvMobileNumber.getText().toString().trim();
            password = edTvPassword.getText().toString().trim();
            if (!name.isEmpty() && !email.isEmpty() && !mobileNumber.isEmpty() && !password.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("mobileNumber", mobileNumber);
                bundle.putString("password", password);
                RegistrationFragment2 registrationFragment2 = new RegistrationFragment2();
                registrationFragment2.setArguments(bundle);
                loadFragment(registrationFragment2);
            }else{
                Toast.makeText(getContext(), "Please fill all the required details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.login_text)
    void login(){
        loadFragment(new EasyLoginFragment());
    }

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    @SuppressWarnings("JavaDoc")
    private void loadFragment(Fragment fragment) {
        // load fragment
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutSignup, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
