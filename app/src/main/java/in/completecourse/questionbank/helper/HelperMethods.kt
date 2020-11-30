package `in`.completecourse.questionbank.helper

import `in`.completecourse.questionbank.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import java.util.*

object HelperMethods {
    /**
     * Making notification bar transparent
     */
    fun changeStatusBarColor(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun loadFragment(fragment: Fragment?, activity: AppCompatActivity) {
        // load fragment
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment!!)
        transaction.commit()
    }

    fun showFragment(fragment: Fragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.frame_container, fragment)
            transaction.show(fragment)
        }
        transaction.commit()
    }

    fun hideFragment(fragment: Fragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (fragment.isAdded) transaction.hide(fragment)
        transaction.commit()
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    class RecyclerTouchListener(context: Context?, private val clickListener: ClickListener?) : OnItemTouchListener {

        private val gestureDetector: GestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })
        override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
            val child: View? = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(recyclerView.getChildAdapterPosition(child))
            }
            return false
        }
        override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {}
    }

    fun generateChecksum(): String? {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val date: Int = calendar.get(Calendar.DATE)

        //Months are indexed from 0
        val currentMonth = month + 1
        val sum = year + currentMonth + date
        val mul = year * currentMonth * date
        val sum5 = sum * 5
        return mul.toString() + "31" + sum5
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    fun isNetworkAvailable(activity: Activity?): Boolean {
        var activeNetworkInfo: NetworkInfo? = null
        if (activity != null) {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @JvmStatic
    fun populateAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        //Some assets are guaranteed to be in every UnifiedNativeAd
        (adView.headlineView as TextView).text = nativeAd.headline
        //((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        //(adView.callToActionView as Button).text = nativeAd.callToAction

        //These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        //check before trying to display them
        val icon = nativeAd.icon

        if (icon == null) {
            adView.iconView.visibility = View.GONE
        }
        else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) adView.priceView.visibility = View.GONE else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.mediaContent == null) adView.mediaView.visibility = View.GONE else{
            if (icon == null){
                adView.mediaView.visibility = View.VISIBLE
                adView.mediaView.setMediaContent(nativeAd.mediaContent)
            }
        }

        /*
        if (nativeAd.getStore() == null)
            adView.getStoreView().setVisibility(View.INVISIBLE);
        else{
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
         */

        if (nativeAd.starRating == null) adView.starRatingView.visibility = View.GONE else {
            adView.starRatingView.visibility = View.VISIBLE
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
        }

        /*
        if (nativeAd.getAdvertiser() == null)
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        else{
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }
         */

        //Assign native ad object to the native view
        adView.setNativeAd(nativeAd)
    }
}