package com.nb.employed.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nb.employed.R
import com.nb.employed.data.EmployeeDetails
import com.nb.employed.data.EmployeeItem
import com.nb.employed.data.EmployeeModelItem
import com.nb.employed.repository.EmployeeRepository
import com.nb.employed.ui.fragment.SearchEmployeeFragment
import com.nb.employed.utils.*
import com.nb.employed.viewmodel.SearchViewModel
import com.nb.employed.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array

class MainActivity : AppCompatActivity(), SearchEmployeeFragment.DialogListener {

    companion object{
         const val TAG = "MainActivity"
         const val EMP_DETAILS = "EMP_DETAILS"
        private const val START_DATE = "01"
    }

    lateinit var viewModel: SearchViewModel
    lateinit var tempEmpList: ArrayList<String>
    lateinit var employeeDetails: EmployeeDetails
    lateinit var year : List<Int>
    var selectedMonth: Int = 1
    var selectedYear: Int = 2021
    var gotEmpDetails: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = EmployeeRepository()
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(SearchViewModel::class.java)

        innit()
        clickListeners()

    }

    private fun innit(){
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December")

        year = listOf(2021, 2020, 2019)

        val adapter = ArrayAdapter(this, R.layout.spinner_item, months)
        spinner_month.adapter = adapter

        val adapter1 = ArrayAdapter(this, R.layout.spinner_item, year)
        spinner_year.adapter = adapter1

        tempEmpList = ArrayList()
        viewModel.getAllEmployees()
        viewModel.employeeList.observe(this, Observer {list->
            list.forEach {
                if (it.emp_id != null) {
                    tempEmpList.add(it.name)
                }
            }
            val adapter
                    = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, tempEmpList)
            autoCompleteText.setAdapter(adapter)
        })

    }

    private fun clickListeners() {
        tview_search.setOnClickListener {
            if (isOnline(this)) {
                val searchEmployeeFragment = SearchEmployeeFragment()
                searchEmployeeFragment.show(supportFragmentManager, "dialog")
            }else{
                showShortToast(getString(R.string.no_internet_conn))
            }
        }

        spinner_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedMonth = position.plus(1)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        spinner_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedYear = year[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        autoCompleteText.setOnItemClickListener { adapterView, view, i, l ->

        }

        btn_proceed.setOnClickListener {
            if (!isOnline(this)){
                showShortToast(getString(com.nb.employed.R.string.no_internet_conn))
                return@setOnClickListener
            }

            showShortToast("Number of days ${getLastDayFromMonthYear(selectedYear,selectedMonth)}")
            if (gotEmpDetails) {
                employeeDetails.let {
                    val lastDate = getLastDayFromMonthYear(selectedYear,selectedMonth)
                    val employeeItem = EmployeeItem(it.emp_id, it.name, getDate(START_DATE), getDate(lastDate.toString()))
                    startActivity(EmployeeDetailActivity.launchNextScreen(this, employeeItem))
                }
            }else{
                showShortToast("Please select an employee")
            }
        }

    }

    private fun getDate(day: String): String{
        val vMonth = if (selectedMonth > 9) "$selectedMonth" else "0$selectedMonth"
        return "$selectedYear-$vMonth-$day"
    }

    /**
    * When the search is done, data will be available here of that select employee
    * */
    override fun onFinishSearch(employeeDetails: EmployeeDetails) {
        this.employeeDetails = employeeDetails
        tview_search.text = "${employeeDetails.name}  ${employeeDetails.emp_id}"
        gotEmpDetails = true
        linearLayout4.visibility =VISIBLE
        linearLayout3.visibility =VISIBLE
        btn_proceed.visibility = VISIBLE
    }


}