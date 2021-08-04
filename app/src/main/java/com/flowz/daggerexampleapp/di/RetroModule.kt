package com.flowz.daggerexampleapp.di


import android.content.Context
import com.flowz.daggerexampleapp.loginandsavetoken.network.RetroLoginService
import com.flowz.daggerexampleapp.loginandsavetoken.preference.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
//Added when we first added the DaggerHilt Dependencies
//@DisableInstallInCheck
//removed when we configure project now work with DaggerHilt
//Now replaced with @Installin(ApplicationComponent::class)
@InstallIn(SingletonComponent::class)
class RetroModule {

//    val baseURL = "https://api.github.com/search/" // repositories?q=newyork"
    val baseURL = "https://api.admoni.net/"

    @Provides
    @Singleton
    fun provideUserSessionManager(@ApplicationContext context: Context): UserSessionManager{
        return UserSessionManager(context)
    }

    @Singleton
    @Provides
    fun getRetroServiceInterface(retrofit: Retrofit):RetroServiceInterface{
        return retrofit.create(RetroServiceInterface::class.java)
    }

    @Singleton
    @Provides
    fun getRetroLoginServiceInterface(retrofit: Retrofit): RetroLoginService {
        return retrofit.create(RetroLoginService::class.java)
    }

    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()


    @Singleton
    @Provides
    fun getRetroFitInstance(): Retrofit{
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}