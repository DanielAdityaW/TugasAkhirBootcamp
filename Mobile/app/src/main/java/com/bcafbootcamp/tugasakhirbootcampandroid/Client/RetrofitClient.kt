package com.bcafbootcamp.tugasakhirbootcampandroid.Client

import com.bcafbootcamp.tugasakhirbootcampandroid.Controller.ApiService
import com.bcafbootcamp.tugasakhirbootcampandroid.Controller.ExcelUploadApi
import com.bcafbootcamp.tugasakhirbootcampandroid.Controller.LogoutApi
import com.bcafbootcamp.tugasakhirbootcampandroid.Controller.ManualInputApi
import com.bcafbootcamp.tugasakhirbootcampandroid.Controller.PredictionHistoryApi
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionHistory
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://4ef1-103-165-222-114.ngrok-free.app/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val predictionHistoryApi: PredictionHistoryApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PredictionHistoryApi::class.java)
    }

    val excelUploadApi: ExcelUploadApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ExcelUploadApi::class.java)
    }

    val manualInputApi: ManualInputApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ManualInputApi::class.java)
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Replace with your backend URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val logoutApi: LogoutApi = retrofit.create(LogoutApi::class.java)
}