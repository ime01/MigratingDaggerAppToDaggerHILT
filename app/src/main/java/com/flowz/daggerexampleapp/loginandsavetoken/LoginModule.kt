package com.flowz.daggerexampleapp.loginandsavetoken


import com.flowz.daggerexampleapp.loginandsavetoken.network.RetroLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//Added when we first added the DaggerHilt Dependencies
//@DisableInstallInCheck
//removed when we configure project now work with DaggerHilt
//Now replaced with @Installin(ApplicationComponent::class)
//@InstallIn(SingletonComponent::class)
//class LoginModule {






//    @Singleton
//    @Provides
//    fun getRetroFitLoginInstance(): Retrofit{
//        return Retrofit.Builder()
//            .baseUrl(baseURL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

//}