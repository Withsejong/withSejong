package com.withsejong.retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("/signup")
    fun signup(
        @Body jsonParams : JsonElement
    ): Call<UserSignupResponse>


    @POST("/auth?method=MoodlerSession")
    fun checkSejong(
        @Body jsonParams: JsonElement

    ): Call<SejongAuthResponse>

    @POST("/login")
    fun login(
        @Body jsonParams: JsonElement
    ): Call<LoginResponse>

    //http://43.201.66.172:8080/http://12.12.12.12:8080/checkNickname?nickname=misterjerry
    @GET("/checkNickname")
    fun isDuplicatedNickname(
        @Query("nickname") nickname:String
    ):Call<Boolean>

    @GET("/checkStudentId")
    fun IsDuplicatedID(
        @Query("studentId") id:String
    ):Call<Boolean>

}