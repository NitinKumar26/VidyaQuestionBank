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
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.completecourse.questionbank.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import in.completecourse.questionbank.PDFActivity;
import in.completecourse.questionbank.adapter.NotificationAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HttpHandler;
import in.completecourse.questionbank.model.NotificationModel;


public class NotificationFragment extends Fragment {

    private ArrayList<NotificationModel> itemsList;
    private NotificationAdapter mAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout emptyView;
    private ProgressDialog pDialog;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_notification);
        //progressBar = view.findViewById(R.id.progressbar_fragment_store);
        itemsList = new ArrayList<>();

        emptyView = view.findViewById(R.id.empty_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setNestedScrollingEnabled(false);

        if (isNetworkAvailable()){
            new GetNotifications(NotificationFragment.this).execute();

        }else{
            Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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



    private static class GetNotifications extends AsyncTask<Void, Void, Void> {
        private String url;
        NotificationModel model;
        private final WeakReference<NotificationFragment> activityWeakReference;

        GetNotifications(NotificationFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            NotificationFragment activity = activityWeakReference.get();
            super.onPreExecute();
            activity.pDialog = new ProgressDialog(activity.getContext());
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            NotificationFragment newArrivalFragment = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            url = AppConfig.URL_NOTIFICATION;
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
                        model.setSerial(String.valueOf(i+1) + ". ");
                        newArrivalFragment.itemsList.add(model);
                    }
                } catch (final JSONException e) {

                    Toast.makeText(newArrivalFragment.getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(newArrivalFragment.getActivity(), "Couldn't get data from server.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final NotificationFragment newArrivalFragment = activityWeakReference.get();
            if (newArrivalFragment.pDialog.isShowing()) {
                newArrivalFragment.pDialog.dismiss();
            }
            //newArrivalFragment.mAdapter.notifyDataSetChanged();
            if (newArrivalFragment.itemsList.isEmpty()){
                newArrivalFragment.emptyView.setVisibility(View.VISIBLE);
            }
            newArrivalFragment.mAdapter = new NotificationAdapter(newArrivalFragment.getActivity(), newArrivalFragment.itemsList);
            newArrivalFragment.recyclerView.setAdapter(newArrivalFragment.mAdapter);
            //newArrivalFragment.progressBar.setVisibility(View.INVISIBLE);
            newArrivalFragment.recyclerView.addOnItemTouchListener(new NotificationAdapter.RecyclerTouchListener(newArrivalFragment.getContext(), new NotificationAdapter.ClickListener() {
                @Override
                public void onClick(int position) {
                    String url = newArrivalFragment.itemsList.get(position).getUrl();
                    Intent intent = new Intent(newArrivalFragment.getActivity(), PDFActivity.class);
                    intent.putExtra("url", url);
                    newArrivalFragment.startActivity(intent);
                }
            }));
        }
    }

}



