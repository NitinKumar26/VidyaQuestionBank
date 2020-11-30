package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.model.ActivityItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

class ClassActivityAdapter(private val context: Context, private var activityItemsList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val tvSerial: TextView = itemView.findViewById(R.id.tv_serial)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.relative_serial)
    }

    override fun getItemCount(): Int {
        return activityItemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem = activityItemsList[position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return when (i) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView: View = LayoutInflater.from(context).inflate(R.layout.ad_unified_new, viewGroup, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
                MyViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
                MyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val viewType = getItemViewType(i)
        when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = activityItemsList[i] as UnifiedNativeAd
                HelperMethods.populateAdView(nativeAd, (viewHolder as UnifiedNativeAdViewHolder).view)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val holder = viewHolder as MyViewHolder
                val activityItem: ActivityItem = activityItemsList[i] as ActivityItem
                holder.tvTitle.text = activityItem.activityKaName
                holder.tvSerial.text = activityItem.activityKaName[0].toString()
                holder.tvSubTitle.text = activityItem.desc
                if (i % 5 == 0) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_two, null)
                else if (i % 5 == 1) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_three, null)
                else if (i % 5 == 2) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_four, null)
                else if (i % 5 == 3) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_five, null)
                else holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient, null)
            }
            else -> {
                val holder = viewHolder as MyViewHolder
                val activityItem: ActivityItem = activityItemsList[i] as ActivityItem
                holder.tvTitle.text = activityItem.activityKaName
                holder.tvSerial.text = activityItem.activityKaName[0].toString()
                holder.tvSubTitle.text = activityItem.desc
                if (i % 5 == 0) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_two, null)
                else if (i % 5 == 1) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_three, null)
                else if (i % 5 == 2) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_four, null)
                else if (i % 5 == 3) holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_five, null)
                else holder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient, null)
            }
        }
    }

    fun setItems(activityItemsList: List<Any>) {
        this.activityItemsList = activityItemsList
    }

    internal class UnifiedNativeAdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view: UnifiedNativeAdView = view.findViewById(R.id.unified_ad)

        init {

            //The MediaView will display a video asset if one is present in the ad, and the
            //first image asset otherwise.
            view.setMediaView(view.findViewById<View>(R.id.ad_media) as MediaView)

            //Register the view used for each individual asset
            view.setHeadlineView(view.findViewById<View>(R.id.ad_headline))
            //adView.setBodyView(adView.findViewById(R.id.ad_body));
            //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            view.setIconView(view.findViewById<View>(R.id.ad_icon))
            view.setPriceView(view.findViewById<View>(R.id.ad_price))
            view.setStarRatingView(view.findViewById<View>(R.id.ad_stars))
            //adView.setStoreView(adView.findViewById(R.id.ad_store));
            //adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }

    companion object {
        private const val MENU_ITEM_VIEW_TYPE = 0
        private const val UNIFIED_NATIVE_AD_VIEW_TYPE = 1
    }
}