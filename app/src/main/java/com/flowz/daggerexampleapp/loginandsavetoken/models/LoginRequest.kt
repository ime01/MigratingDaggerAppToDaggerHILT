package com.flowz.daggerexampleapp.loginandsavetoken.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String,
    @SerializedName("imei")
    val deviceID: String,
    @SerializedName("onesignal_id")
    val oneSignal: String
)
