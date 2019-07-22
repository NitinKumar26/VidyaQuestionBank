package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import in.completecourse.questionbank.MainActivity;
import in.completecourse.questionbank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class EasyLoginFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText edTvEmail, edTvPassword;
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_easy_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.send_verification_code_button).setOnClickListener(this);
        edTvEmail = view.findViewById(R.id.edTv_email_login);
        edTvPassword = view.findViewById(R.id.edTv_password);
        view.findViewById(R.id.signup_text).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_verification_code_button:
                String email = edTvEmail.getText().toString().trim();
                String password = edTvPassword.getText().toString().trim();
                String id = HelperMethods.generateChecksum();
                if (isNetworkAvailable()) {
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
                break;
            case R.id.signup_text:
                loadFragment(new RegistrationFragment1());
                break;
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







    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<EasyLoginFragment> activityWeakReference;
        String userId, userEmail;


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

                    PrefManager prefManager = new PrefManager(activity.getContext().getApplicationContext());
                    prefManager.setUserDetails(userId, userEmail);
                    prefManager.setLogin(true);

                    //activity.prefManager.setUserDetails(userId, userEmail);
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
            EasyLoginFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }
}
