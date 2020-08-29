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

public class ResetPasswordFragment extends Fragment {
    private String mobileNumber;
    private ProgressDialog pDialog;
    @BindView(R.id.edTv_password)
    EditText password;
    @BindView(R.id.edTv_re_enter_password)
    EditText reEnterPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
            mobileNumber = bundle.getString("umobile");
    }

    @OnClick(R.id.btn_reset_password)
    void resetPassword(){
        String sPassword = password.getText().toString();
        String sPasswordRepeat = reEnterPassword.getText().toString();
        JSONObject dataObj = new JSONObject();
        if (sPassword.isEmpty() && sPasswordRepeat.isEmpty())
            Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        else{
            if (sPassword.equalsIgnoreCase(sPasswordRepeat)) {
                if (HelperMethods.INSTANCE.isNetworkAvailable(getActivity())) {
                    try {
                        dataObj.putOpt("umobile", mobileNumber);
                        dataObj.putOpt("upassword", sPassword);
                        dataObj.putOpt("id", HelperMethods.INSTANCE.generateChecksum());
                        JSONTransmitter jsonTransmitter = new JSONTransmitter(ResetPasswordFragment.this);
                        jsonTransmitter.execute(dataObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(getContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
        }
    }

    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<ResetPasswordFragment> activityWeakReference;
        private String message;

        JSONTransmitter(ResetPasswordFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ResetPasswordFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final ResetPasswordFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            HttpPost post = new HttpPost(AppConfig.URL_RESET_PASSWORD);
            try {
                StringEntity se = new StringEntity( json.toString());
                post.addHeader("content-type", "application/json");
                post.addHeader("accept", "application/json");
                post.setEntity(se);
                HttpResponse response;
                response = client.execute(post);
                String resFromServer = EntityUtils.toString(response.getEntity());
                jsonResponse = new JSONObject(resFromServer);

                if (jsonResponse.has("success")){
                    //String distributor_id = jsonResponse.getString("distToken");
                    message = jsonResponse.getString("success");

                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), message, Toast.LENGTH_SHORT).show());
                        //HelperMethods.INSTANCE.loadFragment(new EasyLoginFragment(), activity.getActivity());
                    }

                }
            } catch (Exception e) { e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ResetPasswordFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }
}
