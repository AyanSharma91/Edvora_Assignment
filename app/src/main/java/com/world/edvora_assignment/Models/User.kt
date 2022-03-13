package com.world.edvora_assignment.Models

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("station_code")
    val stationCode: Int,
    @SerializedName("url")
    val url: String
)