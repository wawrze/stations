package com.wawra.stations.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class Station(
    @PrimaryKey
    @ColumnInfo(name = "station_id")
    val stationId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "name_slug")
    val nameSlug: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "hits")
    val hits: Int,

    @ColumnInfo(name = "ibnr")
    val ibnr: Long,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "region")
    val region: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "localised_name")
    val localisedName: String?
)