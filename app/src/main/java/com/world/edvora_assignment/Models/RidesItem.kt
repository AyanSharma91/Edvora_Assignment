package com.world.edvora_assignment.Models

import com.google.gson.annotations.SerializedName


data class RidesItem(
    @SerializedName("city")
    val city: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("destination_station_code")
    val destinationStationCode: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("map_url")
    val mapUrl: String,
    @SerializedName("origin_station_code")
    val originStationCode: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("station_path")
    val stationPath: List<Int>
)