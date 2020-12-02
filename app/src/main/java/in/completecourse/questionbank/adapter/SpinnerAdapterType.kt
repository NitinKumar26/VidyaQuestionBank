package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapterType(context: Context, resource: Int, strArr: Array<String>) : ArrayAdapter<String?>(context, resource, 0, strArr) {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mStringArray: Array<String> = strArr
    private val mResource: Int = resource
    private fun homeView(i: Int, viewGroup: ViewGroup): View {
        val inflate = mLayoutInflater.inflate(mResource, viewGroup, false)
        (inflate.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i])
        return inflate
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var myView = convertView
        if (myView == null) {
            myView = mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false)
        }
        (myView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[position])
        return myView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return homeView(position, parent)
    }

}