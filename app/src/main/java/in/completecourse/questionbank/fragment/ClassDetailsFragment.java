package in.completecourse.questionbank.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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


public class ClassDetailsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private ArrayList<ActivityItem> activityItemArrayList, updatesList;
    private ProgressDialog pDialog;
    public static String subjectStringFinal;
    private static String classStringFinal;
    private ClassActivityAdapter adapter;

    public ClassDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        activityItemArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3, RecyclerView.VERTICAL, false));


        if (SubjectActivity.intent != null) {
            subjectStringFinal = SubjectActivity.subjectString;
            classStringFinal = SubjectActivity.classString;
            String id = HelperMethods.generateChecksum();
            JSONObject dataObj = new JSONObject();
            try {
                dataObj.putOpt("id", id);
                Log.e("id", id);
                dataObj.putOpt("subjectkiid", subjectStringFinal);
                Log.e("subjectkiid", subjectStringFinal);
                JSONTransmitter jsonTransmitter = new JSONTransmitter(ClassDetailsFragment.this);
                jsonTransmitter.execute(dataObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }



    public static class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<ClassDetailsFragment> activityWeakReference;
        ActivityItem item;
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
                        item =  new ActivityItem();
                        JSONObject chapterObject = jsonArray.getJSONObject(i);
                        item.setActivityKaName(chapterObject.getString("activityname"));
                        Log.e("activityname", item.getActivityKaName());
                        item.setActivityKiId(chapterObject.getString("activitykiid"));
                        activity.activityItemArrayList.add(item);

                        switch (i%10){
                            case 0:
                                item.setCardBackground(activity.getResources().getColor(R.color.color1));
                                break;
                            case 1:
                                item.setCardBackground(activity.getResources().getColor(R.color.color2));
                                break;
                            case 2:
                                item.setCardBackground(activity.getResources().getColor(R.color.color3));
                                break;
                            case 3:
                                item.setCardBackground(activity.getResources().getColor(R.color.color4));
                                break;
                            case 4:
                                item.setCardBackground(activity.getResources().getColor(R.color.color5));
                                break;
                            case 5:
                                item.setCardBackground(activity.getResources().getColor(R.color.color6));
                                break;
                            case 6:
                                item.setCardBackground(activity.getResources().getColor(R.color.color7));
                                break;
                            case 7:
                                item.setCardBackground(activity.getResources().getColor(R.color.color8));
                                break;
                            case 8:
                                item.setCardBackground(activity.getResources().getColor(R.color.color9));
                                break;
                            case 9:
                                item.setCardBackground(activity.getResources().getColor(R.color.color10));
                                break;
                        }
                    }

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
            final ClassDetailsFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }

            activity.adapter = new ClassActivityAdapter(activity.getActivity(), activity.activityItemArrayList);
            activity.recyclerView.setAdapter(activity.adapter);
            activity.recyclerView.addOnItemTouchListener(new ClassActivityAdapter.RecyclerTouchListener(activity.getContext(), new ClassActivityAdapter.ClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(activity.getContext(), ComponentActivity.class);
                    Bundle bundle  = new Bundle();
                    bundle.putString("activitykiid", activity.activityItemArrayList.get(position).getActivityKiId());
                    bundle.putString("activityname", activity.activityItemArrayList.get(position).getActivityKaName());
                    bundle.putInt("cardColor", activity.activityItemArrayList.get(position).getCardBackground());
                    bundle.putString("subjectkiid", SubjectActivity.subjectString);
                    bundle.putString("uclass", SubjectActivity.classString);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            }));
        }
    }

}