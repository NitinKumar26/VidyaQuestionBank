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
import in.completecourse.questionbank.PDFActivity;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.SubjectActivity;
import in.completecourse.questionbank.adapter.ClassActivityAdapter;
import in.completecourse.questionbank.adapter.ComponentAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.model.ActivityItem;
import in.completecourse.questionbank.model.Component;


public class ComponentDetailsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private ArrayList<Component> activityItemArrayList;
    private ProgressDialog pDialog;
    public static String activityKiId;
    private static String classStringFinal;
    private ComponentAdapter adapter;

    public ComponentDetailsFragment() {
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


        if (ComponentActivity.intent != null) {
            activityKiId = ComponentActivity.activityId;
            String id = HelperMethods.generateChecksum();
            JSONObject dataObj = new JSONObject();
            try {
                dataObj.putOpt("id", id);
                Log.e("id", id);
                dataObj.putOpt("activitykiid", activityKiId);
                JSONTransmitter jsonTransmitter = new JSONTransmitter(ComponentDetailsFragment.this);
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
        private final WeakReference<ComponentDetailsFragment> activityWeakReference;
        Component item;
        JSONTransmitter(ComponentDetailsFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ComponentDetailsFragment activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final ComponentDetailsFragment activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String ACTIVITIES_URL = AppConfig.URL_COMPONENTS;
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
                        item =  new Component();
                        JSONObject chapterObject = jsonArray.getJSONObject(i);
                        item.setmComponentKiId(chapterObject.getString("componentkiid"));
                        //Log.e("activityname", item.getActivityKaName());
                        item.setmComponentName(chapterObject.getString("componentname"));
                        item.setmComponentURL(chapterObject.getString("componenturl"));
                        activity.activityItemArrayList.add(item);
                        switch (i%10){
                            case 0:
                                item.setCardBackground(activity.getResources().getColor(R.color.color10));
                                break;
                            case 1:
                                item.setCardBackground(activity.getResources().getColor(R.color.color9));
                                break;
                            case 2:
                                item.setCardBackground(activity.getResources().getColor(R.color.color8));
                                break;
                            case 3:
                                item.setCardBackground(activity.getResources().getColor(R.color.color7));
                                break;
                            case 4:
                                item.setCardBackground(activity.getResources().getColor(R.color.color6));
                                break;
                            case 5:
                                item.setCardBackground(activity.getResources().getColor(R.color.color5));
                                break;
                            case 6:
                                item.setCardBackground(activity.getResources().getColor(R.color.color4));
                                break;
                            case 7:
                                item.setCardBackground(activity.getResources().getColor(R.color.color3));
                                break;
                            case 8:
                                item.setCardBackground(activity.getResources().getColor(R.color.color2));
                                break;
                            case 9:
                                item.setCardBackground(activity.getResources().getColor(R.color.color1));
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
            final ComponentDetailsFragment activity = activityWeakReference.get();
            if (activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }

            activity.adapter = new ComponentAdapter(activity.getActivity(), activity.activityItemArrayList);
            activity.recyclerView.setAdapter(activity.adapter);
            activity.recyclerView.addOnItemTouchListener(new ClassActivityAdapter.RecyclerTouchListener(activity.getContext(), new ClassActivityAdapter.ClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(activity.getContext(), PDFActivity.class);
                    intent.putExtra("url", activity.activityItemArrayList.get(position).getmComponentURL());
                    activity.startActivity(intent);

                }
            }));
        }
    }

}