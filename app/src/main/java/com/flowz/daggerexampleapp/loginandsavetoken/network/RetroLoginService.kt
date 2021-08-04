package com.flowz.daggerexampleapp.loginandsavetoken.network

import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetroLoginService {

    @POST("v1/sign-in")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}