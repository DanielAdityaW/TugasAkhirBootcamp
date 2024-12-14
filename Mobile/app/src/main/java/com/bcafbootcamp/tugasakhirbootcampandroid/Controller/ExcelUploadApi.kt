package com.bcafbootcamp.tugasakhirbootcampandroid.Controller

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ExcelUploadApi {

    @Multipart
    @POST("/api/upload-excel")
    fun uploadExcel(
        @Part file: MultipartBody.Part
    ): Call<Map<String, String>>

    @GET("/api/download-predicted-results")
    fun downloadProcessedFile(): Call<ResponseBody>
}