package com.flowz.daggerexampleapp.di

import com.flowz.daggerexampleapp.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {

    fun inject (mainActivityViewModel: MainActivityViewModel)
}