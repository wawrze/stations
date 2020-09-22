package com.wawra.stations.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Model(
    @PrimaryKey val id: Long,
    val data: String
)