package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.model.Update
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import java.util.*

class SliderAdapter(private val mContext: Context?, private val mUpdateList: ArrayList<Update>) : PagerAdapter() {
    override fun getCount(): Int {
        return mUpdateList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val update = mUpdateList[position]
        var view: View? = null
        if (mContext != null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.slider_item, container, false)
            val imageView = view!!.findViewById<ImageView>(R.id.slider_imageView)
            Glide.with(mContext)
                    .load(update.banner_url) //.placeholder(R.drawable.background_gradient)
                    .into(imageView)
            val viewPager = container as ViewPager
            viewPager.addView(view, 0)
        }
        return view!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}