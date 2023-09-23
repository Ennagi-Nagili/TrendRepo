package com.annaginagili.trendrepo.api

import com.annaginagili.trendrepo.model.RepoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("repositories")
    fun getLastDay(@Query("q") date: String, @Query("sort") stars: String, @Query("order") desc: String,
                   @Query("page") page: String): Call<RepoModel>
}