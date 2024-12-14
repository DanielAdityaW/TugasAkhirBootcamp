package com.bcafbootcamp.tugasakhirbootcampandroid.Controller

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST

interface LogoutApi {
    @POST("api/auth/logout")
    fun logout(): Call<ResponseBody>
}