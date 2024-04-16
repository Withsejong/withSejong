package com.withsejong.retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/user/signup")
    fun signup(
        @Body jsonParams : JsonElement
    ): Call<UserSignupResponse>


    @POST("/auth?method=MoodlerSession")
    fun checkSejong(
        @Body jsonParams: JsonElement

    ): Call<SejongAuthResponse>

    @POST("/user/login")
    fun login(
        @Body jsonParams: JsonElement
    ): Call<LoginResponse>

}