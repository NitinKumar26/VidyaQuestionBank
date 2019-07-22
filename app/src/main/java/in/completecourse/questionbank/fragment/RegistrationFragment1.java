package in.completecourse.questionbank.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import in.completecourse.questionbank.R;

public class RegistrationFragment1 extends Fragment implements View.OnClickListener{
    private TextInputEditText edTvName, edTvEmail, edTvMobileNumber, edTvPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_signup).setOnClickListener(this);
        edTvName = view.findViewById(R.id.edTv_name);
        edTvEmail = view.findViewById(R.id.edTv_email);
        edTvMobileNumber = view.findViewById(R.id.edTv_mobile_number);
        edTvPassword = view.findViewById(R.id.edTv_password);
        TextView loginText = view.findViewById(R.id.login_text);
        loginText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String name, email, mobileNumber, password;
        switch (view.getId()){
            case R.id.btn_signup:
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
                break;
            case R.id.login_text:
                loadFragment(new EasyLoginFragment());
        }
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
