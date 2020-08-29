package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import in.completecourse.questionbank.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.OtpView;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.completecourse.questionbank.MainActivity;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class OTPFragment extends Fragment {
    private FirebaseAuth mAuth;
    private String verificationId;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String username;
    private FirebaseFirestore db;
    private String number;

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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
            number = bundle.getString("mobileNumber");
            sendVerificationCode("+91" + number); //send OTP on phoneNumber
        }
    }

    private void sendVerificationCode(String number){
        //show Progress Bar
        progressBar.setVisibility(View.VISIBLE);
        //[START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //The SMS verification code has been sent to the provided phone number, we
            //now need to ask the user to enter the code and then construct a credential
            //by combining the code with a verification ID
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //This callback will be invoked in two situations
            // 1 - Instant verification:
            // In some cases the phone number can be instantly verified
            // without needing to send or enter a verification code.
            // 2 - Auto-retrieval:
            // On some devices Google Play Services can automatically detect
            // the incoming verification SMS and perform verification without user action.
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.GONE);
                otpView.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //This callback is invoked if an invalid request for verification is made,
            //for instance if the phone number format is not valid
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE))
                .addOnSuccessListener(authResult -> {
                    if (getContext() != null){
                        if (authResult.getUser() != null){
                            progressBar.setVisibility(View.VISIBLE);
                            db.collection("qb_users").document(authResult.getUser().getUid())
                                    .get()
                                    .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE))
                                    .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()){
                                    //User details already exists
                                    if (getContext() != null){
                                        PrefManager prefManager = new PrefManager(getContext());
                                        prefManager.setFirstTimeLaunch(false);
                                        prefManager.setLogin(true);
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }else{
                                    //User details are not in the database save them
                                    Map<String, String> userDetails = new HashMap<>();
                                    userDetails.put("name", username);
                                    userDetails.put("phone", number);
                                    if (authResult.getUser() != null)
                                        userDetails.put("userid", authResult.getUser().getUid());
                                    db.collection("qb_users")
                                            .document(authResult.getUser().getUid())
                                            .set(userDetails)
                                            .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE))
                                            .addOnSuccessListener(aVoid -> {
                                                if (getContext() != null){
                                                    PrefManager prefManager = new PrefManager(getContext());
                                                    prefManager.setFirstTimeLaunch(false);
                                                    prefManager.setLogin(true);
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                }
                            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }
        }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
