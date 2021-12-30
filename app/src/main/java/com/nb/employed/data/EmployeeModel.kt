package com.nb.employed.data

import java.io.Serializable


class EmployeeModel : ArrayList<EmployeeModelItem>()

data class EmployeeModelItem(
    val emp_id: String,
    val entry_at: String,
    val exit_at: String
)

data class EmployeeItem(
    val emp_id: String,
    val emp_name: String,
    val entry_at: String,
    val exit_at: String
) : Serializable


data class EmployeeDetail(
    val emp_id: String,
    val name: String
): Serializable

data class AllDetails(
    val employeeItem: EmployeeItem,
    val deviceDetails: DeviceDetails,
    val responseCode: Int,
    val currentDateTime: String
)

class LoggedDetailsList : ArrayList<LoggedDetails>()

data class LoggedDetails(
    val day: Int,
    val hoursLogged: String
)

data class DeviceDetails(
    val Android_Version: String,
    val App_Version: String,
    val Brand: String,
    val Model: String
)