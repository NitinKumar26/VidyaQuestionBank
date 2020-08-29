package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.questionbank.MainActivity;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class EasyLoginFragment extends Fragment {
    private ProgressDialog pDialog;
    @BindView(R.id.edTv_name)
    EditText edTvName;
    @BindView(R.id.edTv_mobile_number)
    EditText edTvMobileNumber;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN = 9001;
    private FirebaseFirestore db;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
    }

    @OnClick(R.id.send_verification_code_button)
    void sendVerificationCode(){
        String name = edTvName.getText().toString().trim();
        String mobileNumber = edTvMobileNumber.getText().toString().trim();
        if (HelperMethods.INSTANCE.isNetworkAvailable(getActivity())) {
            if (!name.isEmpty() && !mobileNumber.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("username", name);
                bundle.putString("mobileNumber", mobileNumber);
                OTPFragment otpFragment = new OTPFragment();
                otpFragment.setArguments(bundle);
                HelperMethods.INSTANCE.loadFragment(otpFragment, (AppCompatActivity) getActivity());


            } else {
                Toast.makeText(getContext(), "Please fill the required details", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Please check your Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.sign_with_google)
    void signInWithGoogle(){
        //[START config_sign_in]
        //Configure Google Sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //[END config_sign_in]
        if (getContext() != null){
            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
            pDialog.show();
            signIn();
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google sign in was successful, authenticate with Firebase
                Toast.makeText(getContext(), "Google Sign in successful ", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    username = account.getDisplayName();
                    //userDocId = account.getId();
                    firebaseAuthWithGoogle(account);
                }

            }catch (ApiException e){
                //Google Sign in failed update the UI accordingly
                if (pDialog.isShowing()) pDialog.dismiss();
                Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "Google Sign in Failed", e);
            }
        }

    }

    //[START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        if (getActivity()!= null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task ->
                            pDialog.hide())
                    .addOnSuccessListener(authResult -> {
                        if (getContext() != null) {
                            if (authResult.getUser() != null) {
                                pDialog.show();
                                db.collection("qb_users").document(authResult.getUser().getUid()).get()
                                        .addOnCompleteListener(task -> pDialog.dismiss())
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()){
                                                //User details are already in the database
                                                if (getContext() != null) {
                                                    PrefManager prefManager = new PrefManager(getContext());
                                                    prefManager.setFirstTimeLaunch(false);
                                                    prefManager.setLogin(true);
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            }else{
                                                pDialog.dismiss();
                                                //User details not available in database save them
                                                Map<String, String> userDetails = new HashMap<>();
                                                userDetails.put("name", username);

                                                if (mAuth.getCurrentUser() != null) {
                                                    userDetails.put("email", mAuth.getCurrentUser().getEmail());
                                                    userDetails.put("userid", authResult.getUser().getUid());
                                                    db.collection("qb_users").document(authResult.getUser().getUid()).set(userDetails)
                                                            .addOnCompleteListener(task -> pDialog.dismiss()).addOnSuccessListener(aVoid -> {
                                                        if (getContext() != null) {
                                                            PrefManager prefManager = new PrefManager(getContext());
                                                            prefManager.setFirstTimeLaunch(false);
                                                            prefManager.setLogin(true);
                                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                                }
                                            }
                                        }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        }
                    });
        }

    }


}
