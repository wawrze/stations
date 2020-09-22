package com.wawra.stations.di.modules

import com.wawra.stations.di.scopes.AppScoped
import com.wawra.stations.network.ApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "https://koleo.pl/api/v2/main"
    }

    @AppScoped
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @AppScoped
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor {
            val newRequest = it.request().newBuilder()
                .addHeader("X-KOLEO-Version", "1")
                .method(it.request().method(), it.request().body())
                .build()
            it.proceed(newRequest)
        }
        .build()

    @AppScoped
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface = retrofit
        .create(ApiInterface::class.java)

}