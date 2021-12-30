package com.nb.employed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nb.employed.network.ApiInterface
import com.nb.employed.repository.EmployeeRepository

class ViewModelFactory(private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(employeeRepository) as T
    }
}