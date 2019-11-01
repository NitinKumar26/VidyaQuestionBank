package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import in.completecourse.questionbank.MainActivity;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class EasyLoginFragment extends Fragment {
    private ProgressDialog pDialog;
    @BindView(R.id.edTv_email_login)
    EditText edTvEmail;
    @BindView(R.id.edTv_password)
    EditText edTvPassword;

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
        edTvEmail = view.findViewById(R.id.edTv_email_login);
        edTvPassword = view.findViewById(R.id.edTv_password);
    }

    @OnClick(R.id.send_verification_code_button)
    void sendVerficationCode(){
        String email = edTvEmail.getText().toString().trim();
        String password = edTvPassword.getText().toString().trim();
        String id = HelperMethods.generateChecksum();
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            if (!email.isEmpty() && !password.isEmpty()) {
                JSONObject dataObj = new JSONObject();
                try {
                    dataObj.putOpt("uemail", email);
                    dataObj.putOpt("upassword", password);
                    dataObj.putOpt("id", id);
                    JSONTransmitter jsonTransmitter = new JSONTransmitter(EasyLoginFragment.this);
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

    @OnClick(R.id.signup_text)
    void signUp(){
        if (getActivity() != null)
        HelperMethods.loadFragment(new RegistrationFragment1(), getActivity(), R.id.frameLayoutSignup, true, "registration_fragment");
    }

    @OnClick(R.id.tv_reset_password)
    void resetPassword(){
        if (getActivity() != null)
            HelperMethods.loadFragment(new SendOTPFragment(), getActivity(), R.id.frameLayoutSignup, true, "otp_fragment");
    }

    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<EasyLoginFragment> activityWeakReference;
        String userId, userEmail;
        private PrefManager prefManager;


        JSONTransmitter(EasyLoginFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            EasyLoginFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final EasyLoginFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String LOGIN_URL = AppConfig.URL_LOGIN;
            HttpPost post = new HttpPost(LOGIN_URL);
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
                    userEmail = jsonResponse.getString("email");
                    userId = jsonResponse.getString("userid");

                    if (activity.getContext() != null) prefManager = new PrefManager(activity.getContext().getApplicationContext());
                    prefManager.setUserDetails(userId, userEmail);
                    prefManager.setLogin(true);

                    //activity.prefManager.setUserDetails(userId, userEmail);

                    activity.startActivity(new Intent(activity.getContext(), MainActivity.class));
                    if (activity.getActivity() != null)
                    activity.getActivity().finish();

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
            EasyLoginFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }
}
