package in.completecourse.questionbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.completecourse.questionbank.adapter.NotificationAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.helper.HttpHandler;
import in.completecourse.questionbank.model.NotificationModel;

public class NotificationActivity extends AppCompatActivity {
    private ArrayList<NotificationModel> itemsList;
    private NotificationAdapter mAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout emptyView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        recyclerView = findViewById(R.id.recyclerView_notification);
        ImageView icUp = findViewById(R.id.ic_up);
        //progressBar = view.findViewById(R.id.progressbar_fragment_store);
        itemsList = new ArrayList<>();

        emptyView = findViewById(R.id.empty_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(NotificationActivity.this, DividerItemDecoration.VERTICAL));

        recyclerView.setNestedScrollingEnabled(false);

        if (HelperMethods.INSTANCE.isNetworkAvailable(NotificationActivity.this)){
            new GetNotifications(NotificationActivity.this).execute();

        }else{
            Toast.makeText(NotificationActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        icUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private static class GetNotifications extends AsyncTask<Void, Void, Void> {
        NotificationModel model;
        private final WeakReference<NotificationActivity> activityWeakReference;

        GetNotifications(NotificationActivity context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            NotificationActivity activity = activityWeakReference.get();
            super.onPreExecute();
            activity.pDialog = new ProgressDialog(activity);
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            NotificationActivity newArrivalFragment = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            String url = AppConfig.URL_NOTIFICATION;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            //Log.e("Company Activity ",  "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new NotificationModel();
                        JSONObject c = jsonArray.getJSONObject(i);
                        //bookNewArrival.setCode(c.getString("arrivalkanaam"));
                        model.setmHeading(c.getString("notifyheading"));
                        model.setmSubHeading(c.getString("notifydetails"));
                        model.setUrl(c.getString("notifyURL"));
                        model.setSerial((i + 1) + ". ");
                        newArrivalFragment.itemsList.add(model);
                    }
                } catch (final JSONException e) {
                    newArrivalFragment.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(newArrivalFragment, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                newArrivalFragment.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(newArrivalFragment, "Couldn't get data from server.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final NotificationActivity newArrivalFragment = activityWeakReference.get();
            if (newArrivalFragment.pDialog.isShowing()) {
                newArrivalFragment.pDialog.dismiss();
            }
            //newArrivalFragment.mAdapter.notifyDataSetChanged();
            if (newArrivalFragment.itemsList.isEmpty()){
                newArrivalFragment.emptyView.setVisibility(View.VISIBLE);
            }
            newArrivalFragment.mAdapter = new NotificationAdapter(newArrivalFragment, newArrivalFragment.itemsList);
            newArrivalFragment.recyclerView.setAdapter(newArrivalFragment.mAdapter);
            //newArrivalFragment.progressBar.setVisibility(View.INVISIBLE);
            newArrivalFragment.recyclerView.addOnItemTouchListener(new NotificationAdapter.RecyclerTouchListener(newArrivalFragment, position -> {
                String url = newArrivalFragment.itemsList.get(position).getUrl();
                Intent intent = new Intent(newArrivalFragment, PDFActivity.class);
                intent.putExtra("url", url);
                newArrivalFragment.startActivity(intent);
            }));
        }
    }
}
