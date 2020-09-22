package com.wawra.stations.network

import com.wawra.stations.network.models.KeywordResponse
import com.wawra.stations.network.models.StationResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("/stations")
    fun getStations(): Single<List<StationResponse>>

    @GET("/station_keywords")
    fun getKeywords(): Single<List<KeywordResponse>>

}