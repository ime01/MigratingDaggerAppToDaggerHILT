package com.flowz.daggerexampleapp

import android.app.Application
//import com.flowz.daggerexampleapp.di.DaggerRetroComponent
//import com.flowz.daggerexampleapp.di.RetroComponent
import com.flowz.daggerexampleapp.di.RetroModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application()

//{

//  Code under commented out because DaggerHilt does the work now

//    private lateinit var retroComponent: RetroComponent
//
//    override fun onCreate() {
//        super.onCreate()
//
//        retroComponent = DaggerRetroComponent.builder()
//            .retroModule(RetroModule())
//            .build()
//
//    }
//
//    fun getRetroComponent():RetroComponent{
//        return retroComponent
//    }
//}