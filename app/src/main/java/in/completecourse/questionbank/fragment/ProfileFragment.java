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
import android.widget.TextView;
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
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.PrefManager;

public class ProfileFragment extends Fragment {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.mobile)
    TextView mobileNumber;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.school)
    TextView school;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.type)
    TextView type;
    private ProgressDialog pDialog;
    private PrefManager mPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPrefManager = new PrefManager(view.getContext().getApplicationContext());

        if (isNetworkAvailable()) {

            String mUserId = mPrefManager.getUserId();
            String id = HelperMethods.generateChecksum();

            JSONObject dataObj = new JSONObject();
            try {
                dataObj.putOpt("id", id);
                dataObj.putOpt("uid", mUserId);
                ProfileFragment.JSONTransmitter jsonTransmitter = new ProfileFragment.JSONTransmitter(ProfileFragment.this);
                jsonTransmitter.execute(dataObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(view.getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_logout)
    void logout(){
        mPrefManager.logoutUser();
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
        private final WeakReference<ProfileFragment> activityWeakReference;

        JSONTransmitter(ProfileFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ProfileFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final ProfileFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String SIGNUP_URL = AppConfig.URL_USER_DETAILS;
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
                    //String distributor_id = jsonResponse.getString("distToken");
                    String username = jsonResponse.getString("username");
                    String uemail = jsonResponse.getString("uemail");
                    String umobile = jsonResponse.getString("umobile");
                    String uclass = jsonResponse.getString("uclass");
                    String uschool = jsonResponse.getString("uschool");
                    String ucity = jsonResponse.getString("ucity");
                    String utype = jsonResponse.getString("utype");
                    if (activity.getActivity() != null){
                        activity.getActivity().runOnUiThread(() -> {
                            activity.name.setText(username);
                            activity.email.setText(uemail);
                            activity.mobileNumber.setText(umobile);
                            activity.tvClass.setText(uclass);
                            activity.school.setText(uschool);
                            activity.city.setText(ucity);
                            activity.type.setText(utype);
                        });
                    }
            } catch (Exception e) { e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ProfileFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }
}
