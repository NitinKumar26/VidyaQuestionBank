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
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
import in.completecourse.questionbank.ScanActivity;
import in.completecourse.questionbank.SearchActivity;
import in.completecourse.questionbank.SubjectActivity;
import in.completecourse.questionbank.adapter.ImageAdapter;
import in.completecourse.questionbank.adapter.SliderAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.HttpHandler;
import in.completecourse.questionbank.helper.PrefManager;
import in.completecourse.questionbank.model.CardModel;
import in.completecourse.questionbank.model.Update;

public class HomeFragment extends Fragment {

    private ArrayList<Update> updateList;
    private static String urlQR;
    private ProgressDialog pDialog;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    TabLayout indicator;
    @BindView(R.id.edTv_request_book)
    EditText editText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private PrefManager prefManager;
    private FirebaseFirestore db;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (requestCode == 100 && resultCode == 100 && data != null){
            urlQR = data.getStringExtra("url");
            new GetBookName(HomeFragment.this).execute();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        prefManager = new PrefManager(view.getContext().getApplicationContext());

        ArrayList<CardModel> cardList = new ArrayList<>();
        cardList.add(new CardModel(R.drawable.manual_search, "Manual Search"));
        cardList.add(new CardModel(R.drawable.scan_qr, "Scan QR Code"));
        recyclerView.setHasFixedSize(true);
        //use a linear layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() ,2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        //specify an adapter
        ImageAdapter recyclerViewAdapter = new ImageAdapter(cardList, getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new ImageAdapter.RecyclerTouchListener(getContext(), position -> {
            if (position == 0) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }else if(position == 1){
                Intent qrCodeActivityIntent = new Intent(getContext(), ScanActivity.class);
                startActivityForResult(qrCodeActivityIntent, 100);
            }
        }));

        if (isNetworkAvailable()){
            getUpdates();
        }else{
            Toast.makeText(view.getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        MediationTestSuite.launch(getContext());
    }

    @OnClick(R.id.btn_request_now)
    void requestBook(){
        if (isNetworkAvailable()) {
            String id = HelperMethods.INSTANCE.generateChecksum();
            String userrequest = editText.getText().toString().trim();
            String mUserId = prefManager.getUserId();
            if (!userrequest.isEmpty()) {
                JSONObject dataObj = new JSONObject();
                try {
                    dataObj.putOpt("id", id);
                    dataObj.putOpt("uid", mUserId);
                    dataObj.putOpt("userrequest", userrequest);
                    JSONTransmitter jsonTransmitter = new JSONTransmitter(HomeFragment.this);
                    jsonTransmitter.execute(dataObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
                Toast.makeText(getContext(), "Please enter your request", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null){
                getActivity().runOnUiThread(() -> {
                    if (mViewPager.getCurrentItem() < updateList.size() - 1) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    } else {
                        mViewPager.setCurrentItem(0);
                    }
                });
            }
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

    private static class GetBookName extends AsyncTask<Void, Void, Void> {
        private final WeakReference<HomeFragment> activityWeakReference;

        GetBookName(HomeFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final HomeFragment activity = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            //tokenNumber = url.substring(url.lastIndexOf('/') + 1, url.length() - 4);
            //Log.e("token", tokenNumber);
            String url = urlQR + "/get";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            //Log.e("Company Activity ",  "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String studentSubject = jsonObject.getString("subjectkiid");
                    String studentClass = jsonObject.getString("uclass");
                    //Log.e("class", studentClass);
                    //Log.e("subject", studentSubject);
                    Intent intent = new Intent(activity.getActivity(), SubjectActivity.class);
                    intent.putExtra("uclass", studentClass);
                    intent.putExtra("subjectkiid", studentSubject);
                    activity.startActivity(intent);
                }catch (final JSONException e) {
                    //Log.e("COMAPANY JSON EXCEPTION", "Json parsing error: " + e.getMessage());
                    if (activity.getActivity() != null)
                        activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            } else {
                if (activity.getActivity() != null)
                activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG)
                        .show());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<HomeFragment> activityWeakReference;

        JSONTransmitter(HomeFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            HomeFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final HomeFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String SIGNUP_URL = AppConfig.URL_REQUEST_BOOK;
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
                    String success = jsonResponse.getString("success");
                    if (activity.getActivity() != null){
                        activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), success, Toast.LENGTH_SHORT).show());
                    }
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
            HomeFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
        }
    }

    private void getUpdates(){
        db.collection("updates").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Update> updates = queryDocumentSnapshots.toObjects(Update.class);
                updateList = new ArrayList<>();
                updateList.addAll(updates);
                SliderAdapter sliderAdapter = new SliderAdapter(getContext(), updateList);
                mViewPager.setAdapter(sliderAdapter);
                indicator.setupWithViewPager(mViewPager, true);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
            }
        });

    }
}



