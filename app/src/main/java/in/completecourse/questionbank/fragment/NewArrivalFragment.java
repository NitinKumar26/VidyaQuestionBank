package in.completecourse.questionbank.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.completecourse.questionbank.PDFActivity;
import in.completecourse.questionbank.R;
import in.completecourse.questionbank.adapter.NewArrivalAdapter;
import in.completecourse.questionbank.app.AppConfig;
import in.completecourse.questionbank.helper.HttpHandler;
import in.completecourse.questionbank.model.BookNewArrival;


public class NewArrivalFragment extends Fragment {

    private List<BookNewArrival> itemsList;
    private NewArrivalAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar_fragment_store)
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_arrival, container, false);
        ButterKnife.bind(this, view);
        itemsList = new ArrayList<>();
        mAdapter = new NewArrivalAdapter(view.getContext(), itemsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(), 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (isNetworkAvailable()){
            new GetLatestBooks(NewArrivalFragment.this).execute();

        }else{
            Toast.makeText(container.getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        return view;
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


    private static class GetLatestBooks extends AsyncTask<Void, Void, Void> {
        BookNewArrival bookNewArrival;
        private final WeakReference<NewArrivalFragment> activityWeakReference;

        GetLatestBooks(NewArrivalFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            NewArrivalFragment newArrivalFragment = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            String url = AppConfig.URL_LATEST_BOOKS;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            //Log.e("Company Activity ",  "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bookNewArrival = new BookNewArrival();
                        JSONObject c = jsonArray.getJSONObject(i);
                        //bookNewArrival.setCode(c.getString("arrivalkacode"));
                        bookNewArrival.setTitle(c.getString("arrivalkanaam"));
                        bookNewArrival.setRate(c.getString("arrivalkarate"));
                        bookNewArrival.setUrl(c.getString("arrivalkaimageurl"));
                        bookNewArrival.setSiteUrl(c.getString("arrivalkasiteurl"));
                        newArrivalFragment.itemsList.add(bookNewArrival);
                     }
                } catch (final JSONException e) {

                    Toast.makeText(newArrivalFragment.getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final NewArrivalFragment newArrivalFragment = activityWeakReference.get();
            newArrivalFragment.mAdapter.notifyDataSetChanged();
            newArrivalFragment.progressBar.setVisibility(View.INVISIBLE);

            newArrivalFragment.recyclerView.addOnItemTouchListener(new NewArrivalAdapter.RecyclerTouchListener(newArrivalFragment.getContext(), position -> {
                String url = newArrivalFragment.itemsList.get(position).getSiteUrl();
                Intent intent = new Intent(newArrivalFragment.getActivity(), PDFActivity.class);
                intent.putExtra("url", url);
                newArrivalFragment.startActivity(intent);
            }));

        }

    }





}