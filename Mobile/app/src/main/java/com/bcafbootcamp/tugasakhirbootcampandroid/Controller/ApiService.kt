package com.bcafbootcamp.tugasakhirbootcampandroid.Controller

import com.bcafbootcamp.tugasakhirbootcampandroid.Model.CustomerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/customers")
    fun getCustomers(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortOrder") sortOrder: String,
        @Query("vehicleType") vehicleType: String,
        @Query("customerId") customerId: String
    ): Call<CustomerResponse>
}
