package com.nb.employed.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.SearchView.OnQueryTextListener
import android.widget.SearchView.VERTICAL
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.nb.employed.R
import com.nb.employed.adapter.RecyclerViewAdapter
import com.nb.employed.data.EmployeeDetails
import com.nb.employed.repository.EmployeeRepository
import com.nb.employed.ui.MainActivity
import com.nb.employed.utils.ItemClickListener
import com.nb.employed.utils.hide
import com.nb.employed.utils.show
import com.nb.employed.viewmodel.SearchViewModel
import com.nb.employed.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search_employee.*
import kotlinx.android.synthetic.main.fragment_search_employee.view.*

/**
* This is a Search dialog fragment
* */
class SearchEmployeeFragment : DialogFragment(), SearchView.OnQueryTextListener, ItemClickListener {

    lateinit var viewModel: SearchViewModel
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    lateinit var dialogListener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.fragment_search_employee, LinearLayout(activity), false)

        val builder = Dialog(requireActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        view?.let {
            builder.setContentView(it)
            innit(view)
            return builder
        }

        return builder
    }

    private fun innit(view: View) {

        dialogListener = activity as DialogListener

        /**
        * setting SearchView
        * */
        view.search_view.requestFocus()
        view.search_view.setOnQueryTextListener(this)

        /**
        * Setting RecyclerView
        * */
        view.recyclerview_emp.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        view.recyclerview_emp.hasFixedSize()
        recyclerViewAdapter = RecyclerViewAdapter(this)

        val repository = EmployeeRepository()

        view.progress_bar.show()
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(SearchViewModel::class.java)
        viewModel.getAllEmployees()
        viewModel.employeeList.observe(this, {
            /**
            * passing the list to RecyclerView Adapter
            * */
            recyclerViewAdapter.setEmployee(it.filter {
                !it.emp_id.isNullOrEmpty()
            })
            view.recyclerview_emp?.adapter = recyclerViewAdapter
            recyclerViewAdapter.notifyDataSetChanged()

            view.progress_bar.hide()
        })

        view.btn_cancel.setOnClickListener { dismiss() }

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            recyclerViewAdapter.filter.filter(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            recyclerViewAdapter.filter.filter(newText)
        }
        return true
    }

    interface DialogListener{
        fun onFinishSearch(employeeDetails: EmployeeDetails)
    }

    /**
    * After clicking on particular item of a RecyclerView we receive data here
    * */
    override fun onItemClickListener(employeeDetails: EmployeeDetails) {
        dialogListener.onFinishSearch(employeeDetails)
        dismiss()
    }

}

