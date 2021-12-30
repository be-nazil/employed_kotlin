package com.nb.employed.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nb.employed.R
import com.nb.employed.data.EmployeeDetails
import com.nb.employed.utils.ItemClickListener
import kotlinx.android.synthetic.main.layout_single_employee_item.view.*

class RecyclerViewAdapter(private var onItemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(), Filterable {

    private lateinit var listData: List<EmployeeDetails>
    private lateinit var tempListDataFull: List<EmployeeDetails>


    fun setEmployee(list: List<EmployeeDetails>){
        this.listData = list
        this.tempListDataFull = listData
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textViewName: TextView
        var textViewId: TextView
        init {
             textViewName = itemView.text_emp_name
             textViewId = itemView.text_emp_id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_single_employee_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewName.text = listData[position].name
        holder.textViewId.text = listData[position].emp_id

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClickListener(listData[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<EmployeeDetails> = ArrayList()

            if (constraint.isEmpty()) {
                filteredList.addAll(tempListDataFull)
            } else {
                val filterPattern: String = constraint.toString().toLowerCase().trim()
                for (item in tempListDataFull) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            listData = emptyList()
            listData = filterResults.values as List<EmployeeDetails>
            notifyDataSetChanged()
        }
    }

}