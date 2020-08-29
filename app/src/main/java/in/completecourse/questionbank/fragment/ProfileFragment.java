package in.completecourse.questionbank.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.questionbank.EditProfileActivity;
import in.completecourse.questionbank.MoreAppsActivity;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.helper.PrefManager;

public class ProfileFragment extends Fragment {

    @BindView(R.id.tv_userName) TextView name;
    @BindView(R.id.tv_email) TextView email;
    @BindView(R.id.tv_contact) TextView mobileNumber;
    @BindView(R.id.tv_class) TextView tvClass;
    @BindView(R.id.tv_school) TextView school;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.adView) AdView adView;

    private PrefManager mPrefManager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mPrefManager = new PrefManager(view.getContext().getApplicationContext());

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (isNetworkAvailable()) {
            getUserProfile(mAuth.getUid());
        }
    }

    @OnClick(R.id.btn_logout)
    void logout(){
        mAuth.signOut();
        mPrefManager.logoutUser();
    }

    @OnClick(R.id.btn_edit_profile)
    void editProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_share)
    void shareApp(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "Hey download Vidya Question Bank App and learn on the go. \n https://play.google.com/store/apps/details?id=in.completecourse.questionbank";
        intent.putExtra(Intent.EXTRA_SUBJECT, "Download App");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share via"));
    }


    @OnClick(R.id.btn_more_app)
    void moreApps(){
        Intent intent = new Intent(getContext(), MoreAppsActivity.class);
        startActivity(intent);
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        if (getActivity() != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) ((getActivity())).getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getUserProfile(String userId){
        db.collection("qb_users").document(userId).get().addOnCompleteListener(task -> progressBar.setVisibility(View.GONE)).addOnSuccessListener(documentSnapshot -> {

            name.setText(getString(R.string.username, documentSnapshot.getString("name")));

            if (documentSnapshot.getString("email") == null) email.setText(getString(R.string.email_pro, "Not Provided"));
            else email.setText(getString(R.string.email_pro, documentSnapshot.getString("email")));

            if (documentSnapshot.getString("class") == null) tvClass.setText(getString(R.string.user_class, "Not Provided"));
            else tvClass.setText(getString(R.string.user_class, documentSnapshot.getString("class")));

            if (documentSnapshot.getString("school") == null) school.setText(getString(R.string.school_pro, "Not Provided"));
            else school.setText(getString(R.string.school_pro, documentSnapshot.getString("school")));

            if (documentSnapshot.getString("phone") == null) mobileNumber.setText(getString(R.string.contact, "Not Provided"));
            else mobileNumber.setText(getString(R.string.contact, documentSnapshot.getString("phone")));

        }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}
