package `in`.completecourse.questionbank.adapter

import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.model.CardModel
import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import java.util.*

class ImageAdapter(eventList: ArrayList<CardModel>, context: Context?) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    private val list: ArrayList<CardModel> = eventList
    private val myContext: Context? = context

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail_company)
        val textName: TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val company: CardModel = list[position]
        holder.textName.text = company.name
        if (myContext != null) Glide.with(myContext).load(company.thumbnail).into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}