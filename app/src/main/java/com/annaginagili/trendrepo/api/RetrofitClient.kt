package com.annaginagili.trendrepo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }
            return instance as RetrofitClient
        }
    }

    private val retrofit = Retrofit.Builder().baseUrl(Constants.baseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val myApi: Api = retrofit.create(Api::class.java)

    fun getApi(): Api {
        return myApi
    }
}