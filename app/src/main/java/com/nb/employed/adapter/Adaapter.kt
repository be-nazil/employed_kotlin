package com.nb.employed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nb.employed.R
/*

class RecyclerAdapter(
    var moviesList: List<String>,
    recyclerViewClickInterface: RecyclerViewClickInterface
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val recyclerViewClickInterface: RecyclerViewClickInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rowCountTextView.text = position.toString()
        holder.textView.text = moviesList[position]
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView: TextView
        var rowCountTextView: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            textView = itemView.findViewById(R.id.textView)
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView)
            itemView.setOnClickListener { recyclerViewClickInterface.onItemClick(adapterPosition) }
            itemView.setOnLongClickListener { //                    moviesList.remove(getAdapterPosition());
                //                    notifyItemRemoved(getAdapterPosition());
                recyclerViewClickInterface.onLongItemClick(adapterPosition)
                true
            }
        }
    }

    companion object {
        private const val TAG = "RecyclerAdapter"
    }

    init {
        this.recyclerViewClickInterface = recyclerViewClickInterface
    }
}*/
