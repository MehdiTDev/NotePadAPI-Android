package com.example.noteapi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL = "http://10.0.2.2:8000"
    private val okHttp = OkHttpClient.Builder()
    // retrofit builder
    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun<T> buildService(serviceType: Class<T>):T{
        return retrofit.create(serviceType)
    }
}