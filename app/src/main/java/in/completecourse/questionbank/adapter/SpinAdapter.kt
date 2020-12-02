package `in`.completecourse.questionbank.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import `in`.completecourse.questionbank.R

class SpinAdapter(context: Context, resource: Int, strArr: Array<String>) : ArrayAdapter<String?>(context, resource, 0, strArr) {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mStringArray: Array<String> = strArr
    private val mResource: Int = resource
    private fun homeView(i: Int, viewGroup: ViewGroup): View {
        val view = mLayoutInflater.inflate(mResource, viewGroup, false)
        when (i % 3) {
            0, 2 -> {
                (view.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] +
                        "<sup><small>th</small></sup>")
                (view.findViewById<View>(R.id.tvclass) as TextView).text = "Class"
            }
            1 -> {
                (view.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] +
                        "<sup><small>वीं</small></sup>")
                (view.findViewById<View>(R.id.tvclass) as TextView).text = "कक्षा"
            }
        }
        return view
    }

    override fun getDropDownView(i: Int, view: View?, parent: ViewGroup): View {
        var myView = view
        if (myView == null) {
            myView = mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false)
        }
        when (i % 3) {
            0, 2 -> {
                (myView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] +
                        "<sup><small>th</small></sup>")
                (myView.findViewById<View>(R.id.tvclass) as TextView).text = "Class"
            }
            1 -> {
                (myView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] +
                        "<sup><small>वीं</small></sup>")
                (myView.findViewById<View>(R.id.tvclass) as TextView).text = "कक्षा"
            }
        }
        return myView!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return homeView(position, parent)
    }

}