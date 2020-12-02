package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.helper.PrefManager
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    private var layouts: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val prefManager = PrefManager(this)

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(
                R.layout.welcome_siide1,
                R.layout.welcome_slide2)

        // adding bottom dots
        addBottomDots(0)

        val myViewPagerAdapter = MyViewPagerAdapter()
        view_pager.adapter = myViewPagerAdapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            prefManager.isFirstTimeLaunch = false
            startActivity(intent)
            finish()
        }

        btn_next.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val nextItem = item
            if (nextItem < layouts!!.size) {
                // move to next screen
                view_pager.currentItem = nextItem
            } else {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                prefManager.isFirstTimeLaunch = false
                startActivity(intent)
                finish()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts!!.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        layoutDots.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private val item: Int get() = view_pager.currentItem + 1

    //  viewpager change listener
    private val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts?.size?.minus(1) ?: 0) {
                // last page. make button text to GOT IT
                btn_next.text = getString(R.string.start)
                btn_skip.visibility = View.GONE
            } else {
                // still pages are left
                btn_next.text = getString(R.string.next)
                btn_skip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * View pager adapter
     */
    internal inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            if (layouts?.size != null)
                return layouts!!.size
            else
                return 0
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}