package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.model.Component
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

class ComponentAdapter(private val context: Context?, private var activityItemsList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
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
                val unifiedNativeLayoutView = LayoutInflater.from(context).inflate(R.layout.ad_unified_new, viewGroup, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (getItemViewType(i)) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = activityItemsList[i] as UnifiedNativeAd
                HelperMethods.populateAdView(nativeAd, (holder as UnifiedNativeAdViewHolder).adView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val viewHolder = holder as ViewHolder
                val activityItem = activityItemsList[i] as Component
                viewHolder.tvTitle.text = activityItem.getmComponentName()
                viewHolder.tvSerial.text = activityItem.getmComponentName()[0].toString()
                if (context != null) {
                    when {
                        i % 10 == 0 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_one, null)
                        i % 10 == 1 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_two, null)
                        i % 10 == 2 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_three, null)
                        i % 10 == 3 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_four, null)
                        i % 10 == 4 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_five, null)
                        i % 10 == 5 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_six, null)
                        i % 10 == 6 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_seven, null)
                        i % 10 == 7 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_eight, null)
                        i % 10 == 8 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_nine, null)
                        i % 10 == 9 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_ten, null)
                    }
                }
            }
            else -> {
                val viewHolder = holder as ViewHolder
                val activityItem = activityItemsList[i] as Component
                viewHolder.tvTitle.text = activityItem.getmComponentName()
                viewHolder.tvSerial.text = activityItem.getmComponentName()[0].toString()
                if (context != null) {
                    when {
                        i % 10 == 0 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_one, null)
                        i % 10 == 1 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_two, null)
                        i % 10 == 2 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_three, null)
                        i % 10 == 3 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_four, null)
                        i % 10 == 4 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_five, null)
                        i % 10 == 5 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_six, null)
                        i % 10 == 6 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_seven, null)
                        i % 10 == 7 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_eight, null)
                        i % 10 == 8 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_nine, null)
                        i % 10 == 9 -> viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(context.resources, R.drawable.gradient_ten, null)
                    }
                }
            }
        }
    }

    fun setItems(activityItemsList: List<Any>) {
        this.activityItemsList = activityItemsList
    }

    internal class UnifiedNativeAdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adView: UnifiedNativeAdView = view.findViewById(R.id.unified_ad)

        init {
            adView.mediaView = view.findViewById(R.id.ad_media)
            adView.headlineView = view.findViewById(R.id.ad_headline)
            adView.iconView = view.findViewById(R.id.ad_icon)
            adView.priceView = view.findViewById(R.id.ad_price)
            adView.starRatingView = view.findViewById(R.id.ad_stars)
        }
    }

    companion object {
        private const val MENU_ITEM_VIEW_TYPE = 0
        private const val UNIFIED_NATIVE_AD_VIEW_TYPE = 1
    }
}