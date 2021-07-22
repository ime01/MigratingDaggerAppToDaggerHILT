package com.flowz.daggerexampleapp.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
//Added when we first added the DaggerHilt Dependencies
//@DisableInstallInCheck
//removed when we configure project now work with DaggerHilt
//Now replaced with @Installin(ApplicationComponent::class)
@InstallIn(SingletonComponent::class)
class RetroModule {

    val baseURL = "https://api.github.com/search/" // repositories?q=newyork"

    @Singleton
    @Provides
    fun getRetroServiceInterface(retrofit: Retrofit):RetroServiceInterface{
        return retrofit.create(RetroServiceInterface::class.java)
    }


    @Singleton
    @Provides
    fun getRetroFitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}