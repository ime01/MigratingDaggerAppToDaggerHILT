package com.flowz.daggerexampleapp

import android.app.Application
import com.flowz.daggerexampleapp.di.DaggerRetroComponent
import com.flowz.daggerexampleapp.di.RetroComponent
import com.flowz.daggerexampleapp.di.RetroModule


class MyApplication: Application() {

    private lateinit var retroComponent: RetroComponent

    override fun onCreate() {
        super.onCreate()

        retroComponent = DaggerRetroComponent.builder()
            .retroModule(RetroModule())
            .build()

    }

    fun getRetroComponent():RetroComponent{
        return retroComponent
    }
}