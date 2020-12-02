package `in`.completecourse.questionbank.fragment

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.ScanActivity
import `in`.completecourse.questionbank.SearchActivity
import `in`.completecourse.questionbank.SubjectActivity
import `in`.completecourse.questionbank.adapter.ImageAdapter
import `in`.completecourse.questionbank.adapter.SliderAdapter
import `in`.completecourse.questionbank.app.AppConfig
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.helper.HttpHandler
import `in`.completecourse.questionbank.helper.PrefManager
import `in`.completecourse.questionbank.model.CardModel
import `in`.completecourse.questionbank.model.Update
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import cz.msebera.android.httpclient.HttpResponse
import cz.msebera.android.httpclient.client.HttpClient
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient
import cz.msebera.android.httpclient.params.HttpConnectionParams
import cz.msebera.android.httpclient.util.EntityUtils
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class HomeFragment : Fragment(), HelperMethods.ClickListener {
    private var updateList: ArrayList<Update>? = null
    private var pDialog: ProgressDialog? = null

    private var prefManager: PrefManager? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == 100 && data != null) {
            urlQR = data.getStringExtra("url")
            GetBookName(this@HomeFragment).execute()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(view.context.applicationContext)
        val cardList: ArrayList<CardModel> = ArrayList<CardModel>()
        cardList.add(CardModel(R.drawable.manual_search, "Manual Search"))
        cardList.add(CardModel(R.drawable.scan_qr, "Scan QR Code"))
        //use a linear layout manager
        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = gridLayoutManager

        //specify an adapter
        val recyclerViewAdapter = ImageAdapter(cardList, context)
        recycler_view.adapter = recyclerViewAdapter

        recycler_view.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(context, this))

        if (isNetworkAvailable) {
            updates
        } else {
            Toast.makeText(view.context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }

        btn_request_now.setOnClickListener{
            requestBook()
        }

        //MediationTestSuite.launch(getContext());
    }

    fun requestBook() {
        if (isNetworkAvailable) {
            val id: String? = HelperMethods.generateChecksum()
            val userRequest = edTv_request_book.text.toString().trim { it <= ' ' }
            val mUserId: String? = prefManager?.userId
            if (userRequest.isNotEmpty()) {
                val dataObj = JSONObject()
                try {
                    dataObj.putOpt("id", id)
                    dataObj.putOpt("uid", mUserId)
                    dataObj.putOpt("userrequest", userRequest)
                    val jsonTransmitter = JSONTransmitter(this@HomeFragment)
                    jsonTransmitter.execute(dataObj)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else Toast.makeText(context, "Please enter your request", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show()
    }

    private inner class SliderTimer : TimerTask() {
        override fun run() {
            if (activity != null) {
                activity!!.runOnUiThread {
                    if (viewPager.currentItem < updateList!!.size - 1) {
                        viewPager.currentItem = viewPager.currentItem + 1
                    } else {
                        viewPager.currentItem = 0
                    }
                }
            }
        }
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private val isNetworkAvailable: Boolean get() {
            var activeNetworkInfo: NetworkInfo? = null
            if (activity != null) {
                val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private class GetBookName(context: HomeFragment) : AsyncTask<Void?, Void?, Void?>() {
        private val activityWeakReference: WeakReference<HomeFragment> = WeakReference(context)

        override fun doInBackground(vararg arg0: Void?): Void? {
            val activity = activityWeakReference.get()
            val sh = HttpHandler()
            //tokenNumber = url.substring(url.lastIndexOf('/') + 1, url.length() - 4);
            //Log.e("token", tokenNumber);
            val url = "$urlQR/get"
            // Making a request to url and getting response
            val jsonStr = sh.makeServiceCall(url)
            //Log.e("Company Activity ",  "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    val jsonObject = JSONObject(jsonStr)
                    val studentSubject = jsonObject.getString("subjectkiid")
                    val studentClass = jsonObject.getString("uclass")
                    //Log.e("class", studentClass);
                    //Log.e("subject", studentSubject);
                    val intent = Intent(activity!!.activity, SubjectActivity::class.java)
                    intent.putExtra("uclass", studentClass)
                    intent.putExtra("subjectkiid", studentSubject)
                    activity.startActivity(intent)
                } catch (e: JSONException) {
                    //Log.e("COMAPANY JSON EXCEPTION", "Json parsing error: " + e.getMessage());
                    if (activity!!.activity != null) activity.activity!!.runOnUiThread { Toast.makeText(activity.context, "Json parsing error: " + e.message, Toast.LENGTH_LONG).show() }
                }
            } else {
                if (activity!!.activity != null) activity.activity!!.runOnUiThread {
                    Toast.makeText(activity.context, "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show()
                }
            }
            return null
        }

    }

    private class JSONTransmitter(context: HomeFragment) : AsyncTask<JSONObject?, JSONObject?, JSONObject?>() {
        private val activityWeakReference: WeakReference<HomeFragment> = WeakReference(context)

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
            val SIGNUP_URL: String = AppConfig.URL_REQUEST_BOOK
            val post = HttpPost(SIGNUP_URL)
            try {
                val se = StringEntity(json.toString())
                post.addHeader("content-type", "application/json")
                post.addHeader("accept", "application/json")
                post.entity = se
                val response: HttpResponse
                response = client.execute(post)
                val resFromServer = EntityUtils.toString(response.entity)
                jsonResponse = JSONObject(resFromServer)
                if (!jsonResponse.has("success")) {
                    val success = jsonResponse.getString("success")
                    if (activity!!.activity != null) {
                        activity.activity!!.runOnUiThread { Toast.makeText(activity.context, success, Toast.LENGTH_SHORT).show() }
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
        }

    }

    private val updates: Unit get() {
            FirebaseFirestore.getInstance().collection("updates").get()
                    .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val updates = queryDocumentSnapshots.toObjects(Update::class.java)
                        updateList = ArrayList()
                        updateList!!.addAll(updates)
                        val sliderAdapter = SliderAdapter(context, updateList!!)
                        viewPager.adapter = sliderAdapter
                        indicator!!.setupWithViewPager(viewPager, true)
                        val timer = Timer()
                        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
                    }
        }

    companion object {
        private var urlQR: String? = null
    }

    override fun onClick(position: Int) {
        if (position == 0) {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        } else if (position == 1) {
            val qrCodeActivityIntent = Intent(context, ScanActivity::class.java)
            startActivityForResult(qrCodeActivityIntent, 100)
        }
    }

}