package com.bcafbootcamp.tugasakhirbootcampandroid.Controller

import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionHistory
import retrofit2.http.GET

interface PredictionHistoryApi {
    @GET("api/history")
    suspend fun getPredictionHistory(): List<PredictionHistory>
}