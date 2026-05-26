package com.example.appcrud

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val URL_CRUD = "http://10.0.2.2:8080/"
    val api: ApiCrud by lazy {
        Retrofit.Builder()
            .baseUrl(URL_CRUD)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCrud::class.java)
    }
}