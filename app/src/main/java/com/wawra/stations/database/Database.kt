package com.wawra.stations.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.LastSyncTime
import com.wawra.stations.database.entities.Station

@Database(
    entities = [Keyword::class, LastSyncTime::class, Station::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun stationDao(): StationDao

    abstract fun lastSyncDao(): LastSyncTimeDao

}