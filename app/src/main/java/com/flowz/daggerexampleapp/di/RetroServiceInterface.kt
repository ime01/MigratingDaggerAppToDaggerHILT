package com.flowz.daggerexampleapp.di

import com.flowz.daggerexampleapp.model.RecyclerList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServiceInterface {

    @GET("https://api.github.com/search/repositories")
//    fun getDataFromApi(@Query(value = "q") query: String): Call<RecyclerList>?
     suspend fun getDataFromApi(@Query(value = "q") query: String): RecyclerList?
}