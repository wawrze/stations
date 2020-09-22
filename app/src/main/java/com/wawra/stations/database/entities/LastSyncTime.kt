package com.wawra.stations.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_sync_time")
data class LastSyncTime(
    @PrimaryKey
    @ColumnInfo(name = "last_sync")
    val lastSync: Long
)