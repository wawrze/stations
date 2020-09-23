package com.wawra.stations.di.modules

import com.wawra.stations.di.scopes.FragmentScoped
import com.wawra.stations.presentation.DataOutOfDateDialogFragment
import com.wawra.stations.presentation.ErrorDialogFragment
import com.wawra.stations.presentation.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainFragment(): MainFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeDataOutOfDateDialogFragment(): DataOutOfDateDialogFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeErrorDialogFragment(): ErrorDialogFragment?

}