package com.withsejong.retrofit

import com.google.gson.JsonObject
import retrofit2.http.Body

data class UserSignupResponse(
    val name: String,
    val studentId: String,

)
data class SejongAuthResponse(
    val msg:String,
    val result:SejongAuthResponseResultJson
)
data class SejongAuthResponseResultJson(
    val authenticator:String,
    val code:String,
    val is_auth:String,
    val status_code:String,
    val success:String,
    val body:SejongAuthResponseResultBodyJson
)
data class SejongAuthResponseResultBodyJson(
    val name:String,
    val major:String
)

data class LoginResponse(
    val studentId: String,
    val nickname: String,
    val major: String,
    val authToken : LoginResponseAuthToken


)
data class LoginResponseAuthToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)