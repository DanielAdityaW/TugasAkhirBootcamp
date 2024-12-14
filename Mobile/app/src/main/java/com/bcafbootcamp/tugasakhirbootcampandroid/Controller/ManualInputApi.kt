package com.bcafbootcamp.tugasakhirbootcampandroid.Controller

import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionRequest
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ManualInputApi {
    @POST("api/predict-manual")
    fun predictRisk(@Body request: PredictionRequest): Call<PredictionResponse>
}