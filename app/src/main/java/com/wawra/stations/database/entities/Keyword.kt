package com.wawra.stations.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyword")
data class Keyword(
    @PrimaryKey
    @ColumnInfo(name = "keyword_id")
    val keywordId: Long,

    @ColumnInfo(name = "keyword")
    val keyword: String,

    @ColumnInfo(name = "station_id")
    val stationId: Long
)