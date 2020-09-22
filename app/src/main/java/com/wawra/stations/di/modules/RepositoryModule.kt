package com.wawra.stations.di.modules

import com.wawra.stations.database.daos.ModelDao
import com.wawra.stations.di.scopes.AppScoped
import com.wawra.stations.logic.ModelRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @AppScoped
    @Provides
    fun provideModelRepository(modelDao: ModelDao): ModelRepository = ModelRepository(modelDao)

}