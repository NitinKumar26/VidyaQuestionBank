package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.adapter.NotificationAdapter
import `in`.completecourse.questionbank.app.AppConfig
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.helper.HttpHandler
import `in`.completecourse.questionbank.model.NotificationModel
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_notifications.recyclerView_notification
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class NotificationActivity : AppCompatActivity(), HelperMethods.ClickListener {
    private var itemsList: ArrayList<NotificationModel>? = null
    private var mAdapter: NotificationAdapter? = null

    private var pDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val icUp = findViewById<ImageView>(R.id.ic_up)

        itemsList = ArrayList<NotificationModel>()

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@NotificationActivity, RecyclerView.VERTICAL, false)
        recyclerView_notification.setLayoutManager(mLayoutManager)
        recyclerView_notification.addItemDecoration(DividerItemDecoration(this@NotificationActivity, DividerItemDecoration.VERTICAL))
        recyclerView_notification.setNestedScrollingEnabled(false)
        if (HelperMethods.isNetworkAvailable(this@NotificationActivity)) {
            GetNotifications(this@NotificationActivity).execute()
        } else {
            Toast.makeText(this@NotificationActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
        icUp.setOnClickListener { finish() }
    }

    private class GetNotifications constructor(context: NotificationActivity) : AsyncTask<Void?, Void?, Void?>() {
        var model: NotificationModel? = null
        private val activityWeakReference: WeakReference<NotificationActivity> = WeakReference(context)

        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            super.onPreExecute()
            activity!!.pDialog = ProgressDialog(activity)
            activity.pDialog!!.setMessage("Please wait...")
            activity.pDialog!!.setCancelable(false)
            activity.pDialog!!.show()
        }

        protected override fun doInBackground(vararg arg0: Void?): Void? {
            val newArrivalFragment = activityWeakReference.get()
            val sh = HttpHandler()
            val url: String = AppConfig.URL_NOTIFICATION
            // Making a request to url and getting response
            val jsonStr = sh.makeServiceCall(url)
            //Log.e("Company Activity ",  "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    val jsonObject = JSONObject(jsonStr)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        model = NotificationModel()
                        val c = jsonArray.getJSONObject(i)

                        model?.setmHeading(c.getString("notifyheading"))
                        model?.setmSubHeading(c.getString("notifydetails"))
                        model?.url = c.getString("notifyURL")
                        model?.serial = (i + 1).toString() + ". "
                        newArrivalFragment!!.itemsList!!.add(model!!)
                    }
                } catch (e: JSONException) {
                    newArrivalFragment!!.runOnUiThread { Toast.makeText(newArrivalFragment, "Json parsing error: " + e.message, Toast.LENGTH_LONG).show() }
                }
            } else {
                newArrivalFragment!!.runOnUiThread { Toast.makeText(newArrivalFragment, "Couldn't get data from server.", Toast.LENGTH_SHORT).show() }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            val newArrivalFragment = activityWeakReference.get()
            if (newArrivalFragment!!.pDialog!!.isShowing) {
                newArrivalFragment.pDialog!!.dismiss()
            }

            if (newArrivalFragment.itemsList!!.isEmpty()) {
                newArrivalFragment.empty_layout!!.visibility = View.VISIBLE
            }

            newArrivalFragment.mAdapter = NotificationAdapter(newArrivalFragment, newArrivalFragment.itemsList!!)
            newArrivalFragment.recyclerView_notification.adapter = newArrivalFragment.mAdapter

            newArrivalFragment.recyclerView_notification.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(newArrivalFragment,newArrivalFragment))
        }
    }

    override fun onClick(position: Int) {
        val url: String? = itemsList!![position].url
        val intent = Intent(this, PDFActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}