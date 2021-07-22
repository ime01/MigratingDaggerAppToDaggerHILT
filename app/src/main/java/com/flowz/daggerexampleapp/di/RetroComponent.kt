package com.flowz.daggerexampleapp.di

import com.flowz.daggerexampleapp.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

//Now I can remove Component Classes as Hilt now does all this for us

//@Singleton
//@Component(modules = [RetroModule::class])
//interface RetroComponent {
//
//    fun inject (mainActivityViewModel: MainActivityViewModel)
//}