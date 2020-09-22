package com.wawra.stations.di.modules

import androidx.room.Room
import com.wawra.stations.App
import com.wawra.stations.database.Database
import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    companion object {
        private const val DB_NAME = "stations.db"
    }

    private lateinit var database: Database

    @Provides
    fun provideDataBase(app: App): Database = if (!::database.isInitialized) {
        database = Room.databaseBuilder(app, Database::class.java, DB_NAME).build()
        database
    } else {
        database
    }

    @Provides
    fun provideStationDao(database: Database): StationDao = database.stationDao()

    @Provides
    fun provideLastSyncTimeDao(database: Database): LastSyncTimeDao = database.lastSyncDao()

}