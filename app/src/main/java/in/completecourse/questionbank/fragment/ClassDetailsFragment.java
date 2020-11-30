package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import in.completecourse.questionbank.ComponentActivity;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.SubjectActivity;
import in.completecourse.questionbank.adapter.ClassActivityAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.model.ActivityItem;

public class ClassDetailsFragment extends Fragment{
    private ProgressDialog pDialog;
    public static String subjectStringFinal;
    private ClassActivityAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //The AdLoader used to load ads
    private AdLoader adLoader;

    //List of quizItems and native ads that populate the RecyclerView;
    private List<Object> mRecyclerViewItems = new ArrayList<>();
    //List of nativeAds that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getContext() != null) {
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId(getContext().getString(R.string.admob_interstitial));

            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    //Load the next interstitial ad
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

        if (SubjectActivity.intent != null) {
            subjectStringFinal = SubjectActivity.subjectString;
            String id = HelperMethods.INSTANCE.generateChecksum();
            JSONObject dataObj = new JSONObject();
            try {
                dataObj.putOpt("id", id);
                dataObj.putOpt("subjectkiid", subjectStringFinal);
                JSONTransmitter jsonTransmitter = new JSONTransmitter(ClassDetailsFragment.this);
                jsonTransmitter.execute(dataObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<ClassDetailsFragment> activityWeakReference;

        JSONTransmitter(ClassDetailsFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ClassDetailsFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final ClassDetailsFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String ACTIVITIES_URL = AppConfig.URL_ACTIVITIES;
            HttpPost post = new HttpPost(ACTIVITIES_URL);
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
                    JSONObject jsonObject = new JSONObject(resFromServer);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ActivityItem item = new ActivityItem();
                        JSONObject chapterObject = jsonArray.getJSONObject(i);
                        item.setActivityKaName(chapterObject.getString("activityname"));
                        item.setActivityKiId(chapterObject.getString("activitykiid"));
                        activity.mRecyclerViewItems.add(item);

                        switch (i%10){
                            case 0:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient, null));
                                break;
                            case 1:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_one, null));
                                break;
                            case 2:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_two, null));
                                break;
                            case 3:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_three, null));
                                break;
                            case 4:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_four, null));
                                break;
                            case 5:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_five, null));
                                break;
                            case 6:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_six, null));
                                break;
                            case 7:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_seven, null));
                                break;
                            case 8:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_eight, null));
                                break;
                            case 9:
                                item.setCardBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_nine, null));
                                break;
                        }
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
            final ClassDetailsFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }

            activity.adapter = new ClassActivityAdapter(activity.getActivity(), activity.mRecyclerViewItems);
            activity.recyclerView.setAdapter(activity.adapter);

            activity.loadNativeAds();

            activity.recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(activity.getContext(), position -> {
                if (activity.adapter.getItemViewType(position) == 0) {
                    ActivityItem item = (ActivityItem) activity.mRecyclerViewItems.get(position);
                    if (activity.mInterstitialAd.isLoaded())
                        activity.mInterstitialAd.show(); //Show Interstitial Ad;
                    Intent intent = new Intent(activity.getContext(), ComponentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("activitykiid", item.getActivityKiId());
                    bundle.putString("activityname", item.getActivityKaName());
                    bundle.putInt("cardColorPosition", position);
                    bundle.putString("subjectkiid", SubjectActivity.subjectString);
                    bundle.putString("uclass", SubjectActivity.classString);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            }));
        }
    }

    private void insertAdsInMenuItems(List<UnifiedNativeAd> mNativeAds, List<Object> mRecyclerViewItems) {
        if (mNativeAds.size() <= 0) {
            return;
        }
        int offset = (mRecyclerViewItems.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index = index + offset;
            adapter.setItems(mRecyclerViewItems);
            adapter.notifyDataSetChanged();
        }
    }

    private void loadNativeAds() {
        if (getContext() != null) {
            AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.admob_native));
            adLoader = builder.forUnifiedNativeAd(
                    unifiedNativeAd -> {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems(mNativeAds, mRecyclerViewItems);
                            //adapter.notifyDataSetChanged();
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.");
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems);
                                //adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onAdClicked() {
                            //super.onAdClicked();
                            //Ad Clicked
                            Log.e("adclicked", "yes");
                        }
                    }).build();

            //Number of Native Ads to load
            int NUMBER_OF_ADS;
            if (mRecyclerViewItems.size() <= 9)
                NUMBER_OF_ADS = 3;
            else {
                NUMBER_OF_ADS = (mRecyclerViewItems.size() / 5) + 1;
            }


            // Load the Native ads.
            adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);

            Log.e("numberOfAds", String.valueOf(NUMBER_OF_ADS));
        }
    }

}