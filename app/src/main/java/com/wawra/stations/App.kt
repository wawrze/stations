package com.wawra.stations

import androidx.appcompat.app.AppCompatDelegate
import com.wawra.stations.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        return DaggerApplicationComponent.builder().application(this).build()
    }

}