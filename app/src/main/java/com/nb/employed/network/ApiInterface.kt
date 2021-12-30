package com.nb.employed.network

import com.nb.employed.data.EmployeeDetails
import com.nb.employed.data.EmployeeModel
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("att_rprt_api.php?e76c37b493ea168cea60b8902072387caf297979")
    suspend fun getEmployees(): Response<List<EmployeeDetails>>

    @FormUrlEncoded
    @POST("att_rprt_api.php?e76c37b493ea168cea60b8902072387caf297979")
    suspend fun getEmployeeDetail(@Field("emp_id") emp_id: String,
                                  @Field("from_dt") from_dt: String,
                                  @Field("to_dt") to_dt:String ) : Response<EmployeeModel>
}