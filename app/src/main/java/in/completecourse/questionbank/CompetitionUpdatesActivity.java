package in.completecourse.questionbank;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import in.completecourse.questionbank.adapter.CompetitionUpdatesAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.model.UpdateItem;

public class CompetitionUpdatesActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ArrayList<UpdateItem> updatesList;
    private CompetitionUpdatesAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        String classStringFinal = SubjectActivity.classString;


        JSONObject data = new JSONObject();
        try {
            data.putOpt("classid", classStringFinal);
            GetUpdates jsonTransmitter = new GetUpdates(CompetitionUpdatesActivity.this);
            jsonTransmitter.execute(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    private static class GetUpdates extends AsyncTask<JSONObject, JSONObject, JSONObject> {
        private final WeakReference<CompetitionUpdatesActivity> activityWeakReference;
        UpdateItem item;

        GetUpdates(CompetitionUpdatesActivity context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            CompetitionUpdatesActivity activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity);
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            final CompetitionUpdatesActivity activity = activityWeakReference.get();
            JSONObject json = data[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            JSONObject jsonResponse;
            String LOGIN_URL = AppConfig.URL_COMPETITION_UPDATES;
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
                activity.updatesList = new ArrayList<>();

                if (!jsonResponse.has("message")){
                    JSONObject jsonObject = new JSONObject(resFromServer);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i<jsonArray.length(); i++) {
                        item =  new UpdateItem();
                        JSONObject chapterObject = jsonArray.getJSONObject(i);
                        item.setUpdateKaName(chapterObject.getString("comptkanaam"));
                        item.setUpdateKaLink(chapterObject.getString("referencelink"));
                        item.setUpdateKaDesc(chapterObject.getString("details"));
                        item.setSerialNumber(String.valueOf(i+1) + ".");
                        activity.updatesList.add(item);
                    }

                }else{
                    final String msg = jsonResponse.getString("message");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) { e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            final CompetitionUpdatesActivity activity = activityWeakReference.get();
            if(activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
            activity.adapter = new CompetitionUpdatesAdapter(activity, activity.updatesList);
            activity.recyclerView.setAdapter(activity.adapter);
        }
    }
}
