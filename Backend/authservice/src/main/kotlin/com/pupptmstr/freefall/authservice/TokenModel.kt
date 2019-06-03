package com.pupptmstr.freefall.authservice

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("additionalDamage")
    val additionalDamage: Int,
    @SerializedName("additionalDefence")
    val additionalDefence: Int,
    @SerializedName("health")
    val health: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("iss")
    val iss: String
)