package com.nb.employed.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nb.employed.R
import com.nb.employed.adapter.EmployeeRecyclerAdapter
import com.nb.employed.data.*
import com.nb.employed.repository.EmployeeRepository
import com.nb.employed.ui.MainActivity.Companion.EMP_DETAILS
import com.nb.employed.utils.*
import com.nb.employed.viewmodel.SearchViewModel
import com.nb.employed.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_employee_detail.*

class EmployeeDetailActivity : AppCompatActivity() {

    private lateinit var employeeItem: EmployeeItem
    private lateinit var employeeRecyclerAdapter: EmployeeRecyclerAdapter
    private lateinit var loggedDetailsList: LoggedDetailsList
    private var hoursList: ArrayList<Int>? = null
    lateinit var viewModel: SearchViewModel

    private var lastDate:Int = 31
    private var holidays:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
        innit()
    }


    private fun innit() {
        employeeItem = intent.getSerializableExtra(EMP_DETAILS) as EmployeeItem
        setActionBarDetail(employeeItem)
        lastDate = getDDFromYYYYMMDD(employeeItem.exit_at)

        val repository = EmployeeRepository()
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(SearchViewModel::class.java)

        getEmployeeDetails(viewModel)

    }

    private fun getEmployeeDetails(view_Model: SearchViewModel) {
        progress_bar.show()
        employeeRecyclerAdapter = EmployeeRecyclerAdapter()
        hoursList = ArrayList()
        setUpData()

        view_Model.getEmpDetails(employeeItem)

        viewModel.isLoading.observe(this, {
            if (it){
                progress_bar.show()
            }else{
                progress_bar.hide()
            }
        })

        viewModel.errorMessage1.observe(this, {
            showShortToast(it)
        })

        viewModel.loggedDetailsList.observe(this, Observer {
            Log.i("EMPLOYEEDETAILS", "observing data inside $it")
            updateUI(it)
        })


        viewModel.loggedHours.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                loggedHours(it.sum(),it.size, lastDate.minus(it.size).minus(viewModel.holidays.value?:0))
                tview_no_records.visibility = GONE
                recyclerView.visibility = VISIBLE
            }else{
                loggedHours(0,0,0)
                tview_no_records.visibility = VISIBLE
                recyclerView.visibility = GONE
            }
        })

    }

    private fun updateUI(it: LoggedDetailsList) {
        Log.i("updateUI", "inside updateUI")
        if (!it.isNullOrEmpty()) {
            employeeRecyclerAdapter.setEmployee(it)
            recyclerView.adapter = employeeRecyclerAdapter
            employeeRecyclerAdapter.notifyDataSetChanged()

        }
    }

    private fun loggedHours(hours: Int, daysPresent: Int, absent: Int){
        if (hours == 0 && daysPresent == 0 && absent == 0){
            tview_total_hours.text = "Total hours Logged\n\nNo record"
            tview_days_present.text = "Days present\n\nNo record"
            tview_days_absent.text = "Days absent\n\nNo record"
        }else {
            tview_total_hours.text = "Total hours Logged\n\n$hours"
            tview_days_present.text = "Days present\n\n$daysPresent"
            tview_days_absent.text = "Days absent\n\n$absent"
        }
    }

    private fun setUpData(){
        loggedDetailsList = LoggedDetailsList()
        if (lastDate > 0) {
            for(i in 1..lastDate) {
                loggedDetailsList.add(LoggedDetails(i, "0"))
            }
        }
    }

    private fun setActionBarDetail(employeeItem: EmployeeItem) {
        if (supportActionBar != null){
            supportActionBar!!.title = "${employeeItem.emp_name}'s Attendance| ${getMonthFromDate(employeeItem.entry_at)}"
        }
    }




    companion object {
        fun launchNextScreen(context: Context, employeeItem: EmployeeItem): Intent {
            val intent = Intent(context, EmployeeDetailActivity::class.java)
            intent.putExtra(EMP_DETAILS, employeeItem)
            return intent
        }
    }

}