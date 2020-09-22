package com.wawra.stations.di.components

import com.wawra.stations.App
import com.wawra.stations.di.modules.*
import com.wawra.stations.di.scopes.AppScoped
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScoped
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DatabaseModule::class,
        FragmentBuilderModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): ApplicationComponent

    }

}