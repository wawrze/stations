package com.wawra.stations.network

import com.wawra.stations.network.models.Keyword
import com.wawra.stations.network.models.Station
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("/stations")
    fun getStations(): Single<List<Station>>

    @GET("/station_keywords")
    fun getKeywords(): Single<List<Keyword>>

}