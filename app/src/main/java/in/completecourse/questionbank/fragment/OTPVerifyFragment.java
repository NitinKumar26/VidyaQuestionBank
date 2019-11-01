package in.completecourse.questionbank.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mukesh.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.helper.HelperMethods;

public class OTPVerifyFragment extends Fragment {
    @BindView(R.id.otp_view)
    OtpView otpView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp_verify, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            final String otp = bundle.getString("otp");
            final String contact = bundle.getString("contact");
            otpView.setOtpCompletionListener(s -> {
                if (s.equals(otp)) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("umobile", contact);
                    ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                    resetPasswordFragment.setArguments(bundle1);
                    if (getActivity() != null)
                        HelperMethods.loadFragment(resetPasswordFragment, getActivity(), R.id.frameLayoutSignup, true, "reset");
                } else {
                    Toast.makeText(view.getContext(), "Please enter the correct OTP", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
