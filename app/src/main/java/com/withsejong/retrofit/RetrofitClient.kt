package com.withsejong.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASEURL = "http://43.201.66.172:8080"
    val instance : Api by lazy {
        //로깅 인터셉터 설정
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        //커스텀okhttp클라이언트 생성
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()



        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }

    val onlySejongAuth : Api by lazy {
        //로깅 인터셉터 설정
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        //커스텀okhttp클라이언트 생성
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://auth.imsejong.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }

}