package com.flowz.daggerexampleapp.loginandsavetoken.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserSessionManager @Inject constructor(@ApplicationContext context : Context){

    private var datastorePref = context.createDataStore(name = "LOGIN")

    suspend fun SaveUserToken(tokenkey:String, token:String){
        val tokenDSkey = preferencesKey<String>(tokenkey)
        datastorePref.edit { login->
            login[tokenDSkey] = token

        }
    }

   suspend fun ReadUserToken(key: String):String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = datastorePref.data.first()
        return preferences[dataStoreKey]
    }
}