package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import in.completecourse.questionbank.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;
import in.completecourse.questionbank.MainActivity;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class OTPFragment extends Fragment {
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_verify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        OtpView otpView = view.findViewById(R.id.otp_view);
        Bundle bundle = getArguments();
        //Button submitButton = view.findViewById(R.id.submit_otp_button);
        final PrefManager prefManager = new PrefManager(Objects.requireNonNull(getActivity()).getApplicationContext());

        if (bundle != null) {
            final String otp = bundle.getString("otp");
            final String contact = bundle.getString("contact");
            final String email = bundle.getString("email");
            otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
                @Override
                public void onOtpCompleted(String s) {
                    if (s.equals(otp)) {
                        final String id = HelperMethods.generateChecksum();
                        final String allow = "1";
                        JSONObject dataObj = new JSONObject();
                        try {
                            dataObj.putOpt("myemail", email);
                            dataObj.putOpt("mycontact", contact);
                            dataObj.putOpt("id", id);
                            dataObj.putOpt("allow", allow);
                            OTPFragment.JSONTransmitter jsonTransmitter = new OTPFragment.JSONTransmitter(OTPFragment.this);
                            jsonTransmitter.execute(dataObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(view.getContext(), "Please enter the correct OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }



    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<OTPFragment> activityWeakReference;
        String message;
        PrefManager prefManager;

        JSONTransmitter(OTPFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            OTPFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final OTPFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String SIGNUP_URL = AppConfig.URL_USER_SUCCESS;
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

                if (!jsonResponse.has("success")){
                    //String distributor_id = jsonResponse.getString("distToken");
                    prefManager =  new PrefManager(activity.getContext().getApplicationContext());
                    message = jsonResponse.getString("success");
                    activity.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    prefManager.setLogin(true);
                    activity.startActivity(new Intent(activity.getContext(), MainActivity.class));
                    activity.getActivity().finish();

                }else{
                    final String msg = jsonResponse.getString("error");
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity.getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } catch (Exception e) { e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            OTPFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }
}
