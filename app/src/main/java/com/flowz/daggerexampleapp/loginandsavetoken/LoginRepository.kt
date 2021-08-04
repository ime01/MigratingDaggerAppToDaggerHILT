package com.flowz.daggerexampleapp.loginandsavetoken

import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginResponse
import com.flowz.daggerexampleapp.loginandsavetoken.network.RetroLoginService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val apiClient: RetroLoginService ) {

    suspend fun loginUser(loginRequest: LoginRequest):LoginResponse{
        return apiClient.loginUser(loginRequest)
    }
}