package com.wawra.stations.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.stations.base.BaseViewModel
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.calculations.DistanceCalculator
import com.wawra.stations.logic.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class MainViewModel @Inject constructor(var stationRepository: StationRepository) :
    BaseViewModel() {

    private val mStations = MutableLiveData<List<Station>>()
    private val mGetStationsResult = MutableLiveData<Boolean>()
    private val mDistance = MutableLiveData<Double>()
    private val mGetDistanceResult = MutableLiveData<Boolean>()

    val stations: LiveData<List<Station>>
        get() = mStations
    val getStationsResult: LiveData<Boolean>
        get() = mGetStationsResult
    val distance: LiveData<Double>
        get() = mDistance
    val getDistanceResult: LiveData<Boolean>
        get() = mGetDistanceResult

    fun getMatchingStations(text: String) {
        stationRepository.getStationsByKeyword(text)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    mStations.postValue(it)
                    mGetStationsResult.postValue(true)
                },
                {
                    mGetStationsResult.postValue(false)
                }
            )
            .addToDisposables()
    }

    fun getDistance(station1: Station, station2: Station) {
        DistanceCalculator.calculateDistanceInKm(
            station1.latitude,
            station1.longitude,
            station2.latitude,
            station2.longitude
        ).subscribeOn(computation())
            .observeOn(mainThread())
            .subscribe(
                {
                    mDistance.postValue(it)
                    mGetDistanceResult.postValue(true)
                },
                {
                    mGetDistanceResult.postValue(false)
                }
            )
            .addToDisposables()
    }

}