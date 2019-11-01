package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.adapter.SpinAdapter;
import in.completecourse.questionbank.adapter.SpinnerAdapterType;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;

public class RegistrationFragment2 extends Fragment {
    private ProgressDialog pDialog;
    private Bundle bundle;
    private String userClass;
    private String userType;
    @BindView(R.id.classSelection)
    Spinner classSelectionSpinner;
    @BindView(R.id.typeSelection)
    Spinner typeSelectionSpinner;
    @BindView(R.id.edTv_school)
    TextInputEditText edTvSchool;
    @BindView(R.id.edTv_city)
    TextInputEditText edTvCity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        String[] spinnerItemsClass = new String[]{"Choose Class", "10", "12"};
        String[] spinnerItemsType = new String[]{"Choose Type", "Teacher", "Student"};
        SpinAdapter spinnerAdapter = new SpinAdapter(view.getContext(), R.layout.spinner_row , spinnerItemsClass);
        SpinnerAdapterType spinAdapter = new SpinnerAdapterType(view.getContext(), R.layout.spinner_row, spinnerItemsType);
        typeSelectionSpinner.setAdapter(spinAdapter);
        classSelectionSpinner.setAdapter(spinnerAdapter);

    }

    @OnClick(R.id.send_verification_code_button)
    void sendVerificationCode(){
            int position = classSelectionSpinner.getSelectedItemPosition();
            int positionType = typeSelectionSpinner.getSelectedItemPosition();
            if (position == 0){
                userClass = "none";
            }else if (position == 1){
                userClass = "4";
            }else if (position == 2){
                userClass = "3";
            }else if(position  == 3){
                userClass = "2";
            }else if(position == 4){
                userClass = "1";
            }

            if (positionType == 0 ){
                userType = "none";
            }
            else if (positionType == 1){
                userType = "Teacher";
            }else if(positionType == 2){
                userType = "Student";
            }
            assert bundle != null;
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String mobileNumber = bundle.getString("mobileNumber");
        String password = bundle.getString("password");
        String userCity = edTvCity.getText().toString().toLowerCase().trim();
        String school = edTvSchool.getText().toString().toLowerCase().trim();
        String id = HelperMethods.generateChecksum();

            if (isNetworkAvailable()) {
                if (!name.isEmpty() && !email.isEmpty() && !mobileNumber.isEmpty() && !password.isEmpty()&&
                        !userClass.isEmpty()&& !school.isEmpty() && !userCity.isEmpty() && !userType.isEmpty()) {
                    if (!userClass.equalsIgnoreCase("none")) {
                        if (!userType.equalsIgnoreCase("none")) {
                            JSONObject dataObj = new JSONObject();
                            try {
                                dataObj.putOpt("uname", name.toLowerCase());
                                dataObj.putOpt("uemail", email);
                                dataObj.putOpt("umobile", mobileNumber);
                                dataObj.putOpt("upassword", password);
                                dataObj.putOpt("uclass", userClass);
                                dataObj.putOpt("uschool", school.toLowerCase());
                                dataObj.putOpt("ucity", userCity.toLowerCase());
                                dataObj.putOpt("usertype", userType);
                                dataObj.put("id", id);
                                JSONTransmitter jsonTransmitter = new JSONTransmitter(RegistrationFragment2.this);
                                jsonTransmitter.execute(dataObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getContext(), "Please choose your type", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please choose your class", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill the required details", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Please check your Internet connection.", Toast.LENGTH_SHORT).show();
            }
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

    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<RegistrationFragment2> activityWeakReference;
        Bundle bundle;
        String otp, myContact, myEmail;

        JSONTransmitter(RegistrationFragment2 context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            RegistrationFragment2 activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final RegistrationFragment2 activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String SIGNUP_URL = AppConfig.URL_SIGNUP;
            HttpPost post = new HttpPost(SIGNUP_URL);
            try {
                StringEntity se = new StringEntity( json.toString());
                post.addHeader("content-type", "application/json");
                post.addHeader("accept", "application/json");
                post.setEntity(se);
                HttpResponse response;
                response = client.execute(post);
                String resFromServer = EntityUtils.toString(response.getEntity());
                jsonResponse = new JSONObject(resFromServer);

                if (!jsonResponse.has("error")){
                    //String distributor_id = jsonResponse.getString("distToken");
                    otp = jsonResponse.getString("otp");
                    myContact = jsonResponse.getString("mycontact");
                    myEmail = jsonResponse.getString("myemail");
                    bundle = new Bundle();
                    bundle.putString("otp", otp);
                    bundle.putString("email", myEmail);
                    bundle.putString("contact", myContact);
                    OTPFragment otpFragment = new OTPFragment();
                    otpFragment.setArguments(bundle);
                    activity.loadFragment(otpFragment);

                }else{
                    final String msg = jsonResponse.getString("error");
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), msg, Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (Exception e) { e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            RegistrationFragment2 activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }

        }
    }


    /**
     * loading fragment into FrameLayout
     *
     */
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
