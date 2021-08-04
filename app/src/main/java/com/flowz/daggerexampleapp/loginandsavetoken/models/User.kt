package com.flowz.daggerexampleapp.loginandsavetoken.models

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("user_id", alternate = ["id"])
    val user_id: String? = "",
    @SerializedName("username")
    val userName: String? = "",
    val email: String? = "",
    @SerializedName("device_imei")
    val deviceId: String? = "",
    @SerializedName("picture_url")
    val profilePicUrl: String? = null,
    @SerializedName("admoni_coins")
    val admoniCoins: Double? = 0.0,
    @SerializedName("jwt")
    val userToken: String? = "",
    @SerializedName("firstname")
    val firstName: String? = "",
    @SerializedName("phone_number")
    val phoneNumber: String? ="",
    @SerializedName("gender")
    val gender: String?= "",
    @SerializedName("surname")
    val surname: String? = "",
    @SerializedName("dob")
    val dob: String? = "",
    @SerializedName("group_id")
    val groupId: Int? = 1,
    val referralCode: String? = "",
    val referralCount: Int? = 0,
    @SerializedName("onesignal_id")
    val oneSignalId: String? ="",
    val isBusiness: Boolean
)