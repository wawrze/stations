package com.wawra.stations.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.stations.base.BaseViewModel
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.calculations.DistanceCalculator
import com.wawra.stations.logic.errors.DataOutOfDateException
import com.wawra.stations.logic.errors.ErrorCodes
import com.wawra.stations.logic.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class MainViewModel @Inject constructor(var stationRepository: StationRepository) :
    BaseViewModel() {

    private val mStations1 = MutableLiveData<List<Station>>()
    private val mStations2 = MutableLiveData<List<Station>>()
    private val mDistance = MutableLiveData<Double>()
    private val mDataOutOfDateError = MutableLiveData<Boolean>()
    private val mUnknownError = MutableLiveData<Int>()

    val stations1: LiveData<List<Station>>
        get() = mStations1
    val stations2: LiveData<List<Station>>
        get() = mStations2
    val distance: LiveData<Double>
        get() = mDistance
    val dataOutOfDateError: LiveData<Boolean>
        get() = mDataOutOfDateError
    val unknownError: LiveData<Int>
        get() = mUnknownError

    var selectedStation1: Station? = null
    var selectedStation2: Station? = null

    fun getMatchingStations(text: String, forStation1: Boolean) {
        stationRepository.getStationsByKeyword(text)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { if (forStation1) mStations1.postValue(it) else mStations2.postValue(it) },
                {
                    if (it is DataOutOfDateException) {
                        mDataOutOfDateError.postValue(true)
                    } else {
                        mUnknownError.postValue(ErrorCodes.GET_STATIONS_MAIN_VIEW_MODEL.code)
                    }
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
                { mDistance.postValue(it) },
                { mUnknownError.postValue(ErrorCodes.CALCULATE_DISTANCE.code) }
            )
            .addToDisposables()
    }

}