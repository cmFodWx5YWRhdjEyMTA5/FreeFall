package com.pupptmstr.freefall.authservice

import com.google.gson.annotations.SerializedName

data class AuthModel (
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("additionalInfo")
    val additionalInfo: String
)