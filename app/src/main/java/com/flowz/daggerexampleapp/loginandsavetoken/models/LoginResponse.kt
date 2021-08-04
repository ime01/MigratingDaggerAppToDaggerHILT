package com.flowz.daggerexampleapp.loginandsavetoken.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwt")
    val userToken: String,
    @SerializedName("data")
    val user: User,

)
