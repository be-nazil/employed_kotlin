package com.nb.employed.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nb.employed.data.*
import com.nb.employed.repository.EmployeeRepository
import com.nb.employed.utils.getDDFromDate
import com.nb.employed.utils.getEEEDay
import com.nb.employed.utils.getNumberOfHours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class SearchViewModel(private val employeeRepository: EmployeeRepository) : ViewModel() {

    companion object{
        val SATURDAY = "SATURDAY"
        val SUNDAY = "SUNDAY"
        val ABSENT = "ABSENT"
    }

    val employeeList: LiveData<List<EmployeeDetails>>
        get() {
            return employeeRepository.employeeList
        }

    /**
    * Getting all employees list
    * */
    fun getAllEmployees(){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.getEmployees()
        }
    }


    /**
    * Getting particular employee details
    * */
    fun getEmpDetails(employeeItem: EmployeeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.getEmployeeDetail(employeeItem)
        }
    }

    val isLoading : LiveData<Boolean>
        get() {
            return employeeRepository.isLoading
        }

    val errorMessage1: LiveData<String>
        get() = employeeRepository.errorMessage1

    /**
    * returning 30 days list
    * */
    val loggedDetailsList: LiveData<LoggedDetailsList>
        get() {
            return employeeRepository.loggedDetails
        }

    /**
    * Returning list of hours logged
    * */
    val loggedHours: LiveData<List<Int>>
        get() {
            return employeeRepository.loggedHoursList
        }


    /**
    * Returning number of Holidays (count of WeekDays)
    * */
    val holidays: LiveData<Int>
        get() {
            return employeeRepository.holidays
        }



}