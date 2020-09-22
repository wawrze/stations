package com.wawra.stations.di.modules

import com.wawra.stations.di.scopes.FragmentScoped
import com.wawra.stations.presentation.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainFragment(): MainFragment?

}