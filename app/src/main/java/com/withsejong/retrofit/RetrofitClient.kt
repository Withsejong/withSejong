package com.withsejong.retrofit

import com.google.gson.GsonBuilder
import com.withsejong.BuildConfig
import com.withsejong.BuildConfig.BASEURL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASEURL = "http://${BuildConfig.BASEURL}"
    private const val SEJONGAUTHBASEURL = BuildConfig.SEJONGAUTHBASEURL



    val instance : Api by lazy {
        //로깅 인터셉터 설정
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        //gson 유연화 설정
        //MalformedJsonException 때문에 설정

        val gson = GsonBuilder()
            .setLenient()
            .create()

        //커스텀okhttp클라이언트 생성
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(1, TimeUnit.MINUTES)  // 연결 타임아웃 설정
            .readTimeout(1, TimeUnit.MINUTES)     // 읽기 타임아웃 설정
            .writeTimeout(1, TimeUnit.MINUTES)    // 쓰기 타임아웃 설정
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
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
            .baseUrl(SEJONGAUTHBASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }

    val chat : Api by lazy {
        //로깅 인터셉터 설정
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        //gson 유연화 설정
        //MalformedJsonException 때문에 설정

        val gson = GsonBuilder()
            .setLenient()
            .create()

        //커스텀okhttp클라이언트 생성
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("ws://${BuildConfig.BASEURL}/ws/chat")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(Api::class.java)
    }


}