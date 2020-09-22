package com.wawra.stations.network.models

import com.google.gson.annotations.SerializedName

data class Station(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("name_slug")
    val nameSlug: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("hits")
    val hits: Int,

    @SerializedName("ibnr")
    val ibnr: Long,

    @SerializedName("city")
    val city: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("localised_name")
    val localisedName: String?
)