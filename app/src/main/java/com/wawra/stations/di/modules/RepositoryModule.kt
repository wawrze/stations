package com.wawra.stations.di.modules

import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import com.wawra.stations.di.scopes.AppScoped
import com.wawra.stations.logic.repositories.StationRepository
import com.wawra.stations.network.ApiInterface
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @AppScoped
    @Provides
    fun provideModelRepository(
        stationDao: StationDao,
        lastSyncDao: LastSyncTimeDao,
        api: ApiInterface
    ): StationRepository = StationRepository(stationDao, lastSyncDao, api)

}