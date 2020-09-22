package com.wawra.stations.network.models

import com.google.gson.annotations.SerializedName

data class Keyword(
    @SerializedName("id")
    val id: Long,

    @SerializedName("keyword")
    val keyword: String,

    @SerializedName("station_id")
    val stationId: Long
)