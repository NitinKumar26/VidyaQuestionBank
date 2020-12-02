package `in`.completecourse.questionbank.fragment

import `in`.completecourse.questionbank.ComponentActivity
import `in`.completecourse.questionbank.PDFActivity
import `in`.completecourse.questionbank.adapter.ComponentAdapter
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.helper.HelperMethods.generateChecksum
import `in`.completecourse.questionbank.model.Component
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import cz.msebera.android.httpclient.HttpResponse
import cz.msebera.android.httpclient.client.HttpClient
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient
import cz.msebera.android.httpclient.params.HttpConnectionParams
import cz.msebera.android.httpclient.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*
import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.app.AppConfig
import kotlinx.android.synthetic.main.fragment_class_details.*

class ComponentDetailsFragment : Fragment(), HelperMethods.ClickListener {
    private var pDialog: ProgressDialog? = null
    private var adapter: ComponentAdapter? = null
    private var mInterstitialAd: InterstitialAd? = null
    //The AdLoader used to load ads
    private var adLoader: AdLoader? = null
    //List of quizItems and native ads that populate the RecyclerView;
    private val mRecyclerViewItems: MutableList<Any> = ArrayList()
    //List of nativeAds that have been successfully loaded.
    private val mNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_class_details, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null) {
            mInterstitialAd = InterstitialAd(context)
            mInterstitialAd!!.adUnitId = context!!.getString(R.string.admob_interstitial)
            mInterstitialAd!!.loadAd(AdRequest.Builder().build())
            mInterstitialAd!!.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    //Load the next interstitial ad
                    mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerView.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(context, this))

        if (ComponentActivity.intent != null) {
            val activityKiId = ComponentActivity.activityId
            val id: String? = generateChecksum()
            val dataObj = JSONObject()
            try {
                dataObj.putOpt("id", id)
                dataObj.putOpt("activitykiid", activityKiId)
                val jsonTransmitter = JSONTransmitter(this@ComponentDetailsFragment)
                jsonTransmitter.execute(dataObj)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    class JSONTransmitter internal constructor(context: ComponentDetailsFragment) : AsyncTask<JSONObject?, JSONObject?, JSONObject?>() {
        private val activityWeakReference: WeakReference<ComponentDetailsFragment> = WeakReference(context)
        var item: Component? = null
        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.pDialog = ProgressDialog(activity.context)
            activity.pDialog!!.setMessage("Please wait...")
            activity.pDialog!!.setCancelable(false)
            activity.pDialog!!.show()
        }

        override fun doInBackground(vararg data: JSONObject?): JSONObject? {
            val activity = activityWeakReference.get()
            val json = data[0]
            val client: HttpClient = DefaultHttpClient()
            HttpConnectionParams.setConnectionTimeout(client.params, 100000)
            val jsonResponse: JSONObject
            val ACTIVITIES_URL: String = AppConfig.URL_COMPONENTS
            val post = HttpPost(ACTIVITIES_URL)
            try {
                val se = StringEntity(json.toString())
                post.addHeader("content-type", "application/json")
                post.addHeader("accept", "application/json")
                post.entity = se
                val response: HttpResponse
                response = client.execute(post)
                val resFromServer = EntityUtils.toString(response.entity)
                jsonResponse = JSONObject(resFromServer)
                if (!jsonResponse.has("error")) {
                    val jsonObject = JSONObject(resFromServer)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        item = Component()
                        val chapterObject = jsonArray.getJSONObject(i)
                        item!!.setmComponentKiId(chapterObject.getString("componentkiid"))
                        item!!.setmComponentName(chapterObject.getString("componentname"))
                        item!!.setmComponentURL(chapterObject.getString("componenturl"))
                        activity!!.mRecyclerViewItems.add(item!!)
                        when (i % 10) {
                            0 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient, null)
                            1 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_one, null)
                            2 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_two, null)
                            3 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_three, null)
                            4 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_four, null)
                            5 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_five, null)
                            6 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_six, null)
                            7 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_seven, null)
                            8 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_eight, null)
                            9 -> item!!.cardBackground = ResourcesCompat.getDrawable(activity.resources, R.drawable.gradient_nine, null)
                        }
                    }
                } else {
                    val msg = jsonResponse.getString("error")
                    if (activity!!.activity != null) {
                        activity.activity!!.runOnUiThread { Toast.makeText(activity.context, msg, Toast.LENGTH_SHORT).show() }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(jsonObject: JSONObject?) {
            val activity = activityWeakReference.get()
            if (activity!!.pDialog!!.isShowing) {
                activity.pDialog!!.dismiss()
            }
            activity.adapter = ComponentAdapter(activity.context, activity.mRecyclerViewItems)
            activity.recyclerView!!.adapter = activity.adapter
            activity.loadNativeAds()
        }

    }

    private fun insertAdsInMenuItems(mNativeAds: List<UnifiedNativeAd>, mRecyclerViewItems: MutableList<Any>) {
        if (mNativeAds.isEmpty()) {
            return
        }
        val offset = mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad)
            index += offset
            adapter?.setItems(mRecyclerViewItems)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun loadNativeAds() {
        if (context != null) {
            val builder = AdLoader.Builder(context, getString(R.string.admob_native))
            adLoader = builder.forUnifiedNativeAd { unifiedNativeAd: UnifiedNativeAd ->
                // A native ad loaded successfully, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader!!.isLoading) {
                    insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                    //adapter.notifyDataSetChanged();
                }
            }.withAdListener(
                    object : AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.")
                            if (!adLoader!!.isLoading) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                                //adapter.notifyDataSetChanged();
                            }
                        }

                        override fun onAdClicked() {
                            //super.onAdClicked();
                            //Ad Clicked
                            Log.e("adclicked", "yes")
                        }
                    }).build()

            //Number of Native Ads to load
            val NUMBER_OF_ADS: Int
            NUMBER_OF_ADS = if (mRecyclerViewItems.size <= 9) 3 else {
                mRecyclerViewItems.size / 5 + 1
            }


            // Load the Native ads.
            adLoader!!.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS)
            Log.e("numberOfAds", NUMBER_OF_ADS.toString())
        }
    }

    override fun onClick(position: Int) {
        //TODO("Not yet implemented")
        if (adapter?.getItemViewType(position) == 0) {
            if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
            val component = mRecyclerViewItems[position] as Component
            val intent = Intent(view?.context, PDFActivity::class.java)
            intent.putExtra("url", component.getmComponentURL())
            startActivity(intent)
        }
    }
}