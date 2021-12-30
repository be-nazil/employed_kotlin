package com.nb.employed.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.nb.employed.data.*
import com.nb.employed.network.RetrofitClient
import com.nb.employed.utils.*
import com.nb.employed.viewmodel.SearchViewModel


class EmployeeRepository {

    companion object{
        val SATURDAY = "SATURDAY"
        val SUNDAY = "SUNDAY"
        val ABSENT = "ABSENT"
        private val TAG = "EmployeeRepository"
    }


    private val apiInterface = RetrofitClient.buildApi()

    private val mutableList : MutableLiveData<List<EmployeeDetails>> = MutableLiveData()
    private var mutableEmpObj : MutableLiveData<EmployeeDetails> = MutableLiveData()
    private var tempList : MutableList<EmployeeDetails> = mutableListOf()

    val employeeList: LiveData<List<EmployeeDetails>>
        get() {
            return mutableList
        }

    private val errMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() {
            return errMessage
        }

    suspend fun getEmployees(){

        Log.i(TAG, "inside  getEmployee")

        val response = apiInterface.getEmployees()
        if (response.isSuccessful) {
            if (response.body() != null) {
                mutableList.postValue(response.body()!!.filter {
                    (!it.name.isNullOrBlank())
                })
            } else {
                Log.i(TAG, response.message())
                errMessage.postValue("Failed to get employee list")
            }
        }else{
            Log.i(TAG, "Empty response")
            errMessage.postValue("Failed to get employee list")
        }
    }


    private val mutableSingleEmpDataList: MutableLiveData<EmployeeModel> = MutableLiveData()
    val singleEmployeeDetails: LiveData<EmployeeModel>
        get() {
            return mutableSingleEmpDataList
        }

    private val errMessage1 = MutableLiveData<String>()
    val errorMessage1: LiveData<String>
        get() {
            return errMessage1
        }

    private val isLoadingg = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() {
            return isLoadingg
        }

    suspend fun getEmployeeDetail(employeeItem: EmployeeItem){
        isLoadingg.postValue(true)
        Log.i(TAG, "getting employee details from ${employeeItem.entry_at} , to ${employeeItem.exit_at}")
        val response = apiInterface.getEmployeeDetail(
            employeeItem.emp_id,
            employeeItem.entry_at,
            employeeItem.exit_at
        )
        sendDetailsToFirebase(response.code(), employeeItem)
        if (response.isSuccessful) {
            if (response.body() != null) {
                mutableSingleEmpDataList.postValue(response.body())
                response.body()!!.forEach {
                    Log.i(TAG, "Entry at ${it.entry_at}")
                }
                setLoggedHours(employeeItem,response.body()!!)
            } else {
                Log.i(TAG, response.message())
                errMessage1.postValue("Failed to get employee list")
            }
            isLoadingg.postValue(false)
        }else{
            isLoadingg.postValue(false)
            Log.i(TAG, "Empty response")
            errMessage1.postValue("Failed to get employee list - No response")
        }
    }
    private val loggedDetailsList: MutableLiveData<LoggedDetailsList> = MutableLiveData()

    val loggedHoursList: MutableLiveData<List<Int>> = MutableLiveData()

    val loggedDetails: LiveData<LoggedDetailsList>
        get() {
            return loggedDetailsList
        }

    val holidays: MutableLiveData<Int> = MutableLiveData()

    private fun setLoggedHours(employeeItem: EmployeeItem, employeeModel: EmployeeModel){
        var count = 0
        val loggedDetails = LoggedDetailsList()
        val loggedHrs: ArrayList<Int> = ArrayList()
        val lastDate = getDDFromYYYYMMDD(employeeItem.exit_at)
        if (lastDate > 0) {
            for (i in 1..lastDate) {
                Log.i(TAG, "i = $i")
                val day = checkDay(i,employeeItem)
                loggedDetails.add(LoggedDetails(i, day))
                if (day.equals(SATURDAY,true) || day.equals(SUNDAY,true)){
                    count = count.plus(1)
                    Log.i(TAG, "Its Holiday $count")
                }
            }

            /**
            * Adding logged hours to the 30 days list
            * */
            for (j in 0 until employeeModel.size) {
                val day = getDay(employeeModel[j])
                Log.i(TAG, "day = $day")
                if (day < loggedDetails.size) {
                    val hrs = employeeModel[j].let {
                        getNumberOfHours(it.entry_at, it.exit_at)
                    }
                    loggedDetails.removeAt(day)
                    loggedDetails.add(day,LoggedDetails(day, hrs.toString()))
                    loggedHrs.add(hrs)
                }
            }
        }


        holidays.postValue(count)
        loggedDetailsList.postValue(loggedDetails)
        loggedHoursList.postValue(loggedHrs)
        Log.i(TAG, "posted to loggedList ${holidays.value}")

    }


    private fun getDay(employeeModelItem: EmployeeModelItem?): Int{
        return employeeModelItem?.let { getDDFromDate(it.entry_at) }?:0
    }


    /**
    * Checking if day is normal week day or weekend
    * */
    private fun checkDay(day: Int,employeeItem: EmployeeItem):String{
        val EEEDay = getEEEDay(employeeItem.entry_at,day)
        return when {
            SATURDAY.equals(EEEDay,true) -> {
                SATURDAY
            }
            SUNDAY.equals(EEEDay,true) -> {
                SUNDAY
            }
            else -> ABSENT
        }

    }


    /**
    * Storing device details and employee details on FirebaseDb
    * */
    private fun sendDetailsToFirebase(responseCode: Int, employeeItem: EmployeeItem) {
        val firebaseReference  = FirebaseDatabase.getInstance().getReference("Employee")
        val empId = firebaseReference.push().key
        empId?.let {
            firebaseReference.child(it).setValue(AllDetails(employeeItem, getDeviceDetail(),responseCode, getCurrentDate()))
                .addOnCompleteListener {
                    Log.i("EmployeeRepository", "Saved in firebaseDB")
                }
        }
    }


}