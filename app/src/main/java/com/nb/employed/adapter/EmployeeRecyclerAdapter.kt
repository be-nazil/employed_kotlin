package com.nb.employed.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nb.employed.R
import com.nb.employed.data.EmployeeModel
import com.nb.employed.data.LoggedDetailsList
import com.nb.employed.utils.getDDFromDate
import com.nb.employed.utils.getNumberOfHours
import com.nb.employed.viewmodel.SearchViewModel.Companion.ABSENT
import com.nb.employed.viewmodel.SearchViewModel.Companion.SATURDAY
import com.nb.employed.viewmodel.SearchViewModel.Companion.SUNDAY
import kotlinx.android.synthetic.main.layout_single_employee_detail.view.*

class EmployeeRecyclerAdapter : RecyclerView.Adapter<EmployeeRecyclerAdapter.MyViewHolder>() {

    private lateinit var employeeModelList: LoggedDetailsList

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tview_month_days: TextView = view.tview_month_days
        val tview_num_hours_logged: TextView = view.tview_num_hours_logged
    }

    fun setEmployee(employeeModel: LoggedDetailsList){
        this.employeeModelList = employeeModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_single_employee_detail, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItemData = employeeModelList[position]
        /*Log.i("IN Adapter", currentItemData.entry_at)
        holder.tview_month_days.text = getDDFromDate(currentItemData.entry_at).toString()
        holder.tview_num_hours_logged.text = getNumberOfHours(currentItemData.entry_at,currentItemData.exit_at).toString()*/

        Log.i("IN Adapter", currentItemData.hoursLogged)
        holder.tview_month_days.text = currentItemData.day.toString()
        holder.tview_num_hours_logged.text = currentItemData.hoursLogged

        if (currentItemData.hoursLogged == SATURDAY || currentItemData.hoursLogged == SUNDAY){
            holder.tview_num_hours_logged.setTextColor(Color.RED)
        }else if (currentItemData.hoursLogged == ABSENT){
            holder.tview_num_hours_logged.setTextColor(Color.LTGRAY)
        }else{
            holder.tview_num_hours_logged.setTextColor(Color.GRAY)
        }

    }

    override fun getItemCount(): Int {
        return employeeModelList.size
    }

}