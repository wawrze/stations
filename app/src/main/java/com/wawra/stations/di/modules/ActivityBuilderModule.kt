package com.wawra.stations.di.modules

import com.wawra.stations.di.scopes.ActivityScoped
import com.wawra.stations.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity?

}