package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;

public class SendOTPFragment extends Fragment {
    private ProgressDialog pDialog;
    private String mobileNumber;
    @BindView(R.id.edTv_mobile_number)
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_otp, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.send_verification_code_button)
    void sendVerificationCode(){
        mobileNumber = editText.getText().toString();
        String id = HelperMethods.generateChecksum();
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            if (!mobileNumber.isEmpty() ) {
                JSONObject dataObj = new JSONObject();
                try {
                    dataObj.putOpt("umobile", mobileNumber);
                    dataObj.putOpt("id", id);
                    JSONTransmitter jsonTransmitter = new JSONTransmitter(SendOTPFragment.this);
                    jsonTransmitter.execute(dataObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Please fill the required details", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Please check your Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<SendOTPFragment> activityWeakReference;
        Bundle bundle;
        String otp;

        JSONTransmitter(SendOTPFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            SendOTPFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final SendOTPFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String SEND_OTP_URL = AppConfig.URL_SEND_OTP;
            HttpPost post = new HttpPost(SEND_OTP_URL);
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
                    bundle = new Bundle();
                    bundle.putString("otp", otp);
                    bundle.putString("contact", activity.mobileNumber);
                    OTPVerifyFragment otpFragment = new OTPVerifyFragment();
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
            SendOTPFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }

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
