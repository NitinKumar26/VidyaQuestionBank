package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.model.MoreAppsItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class MoreAppsAdapter(mContext: Context, moreAppsItems: ArrayList<MoreAppsItem>) : RecyclerView.Adapter<MoreAppsAdapter.ViewHolder>() {
    private var moreAppsItems: ArrayList<MoreAppsItem>
    private val mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_more_apps, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: MoreAppsItem = moreAppsItems[position]
        Glide.with(mContext).load(item.icon_url).into(holder.imgAppIcon)
        holder.tvAppName.text = item.app_name
        holder.tvAppDesc.text = item.app_desc
    }

    override fun getItemCount(): Int {
        return moreAppsItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAppName: TextView = itemView.findViewById(R.id.tv_title)
        val tvAppDesc: TextView = itemView.findViewById(R.id.tv_subtitle)
        val imgAppIcon: ImageView = itemView.findViewById(R.id.img)

    }

    fun setItems(items: ArrayList<MoreAppsItem>) {
        moreAppsItems = items
    }

    init {
        this.moreAppsItems = moreAppsItems
        this.mContext = mContext
    }
}