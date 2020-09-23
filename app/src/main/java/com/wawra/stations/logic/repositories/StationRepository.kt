package com.wawra.stations.logic.repositories

import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.errors.DataOutOfDateException
import com.wawra.stations.network.ApiInterface
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class StationRepository @Inject constructor(
    private val stationDao: StationDao,
    private val lastSyncTimeDao: LastSyncTimeDao,
    private val api: ApiInterface
) {

    fun getStationsByKeyword(text: String) = updateDataIfNeeded()
        .flatMap { isDataUpToDate ->
            if (isDataUpToDate) {
                stationDao.getMatchingStations(text)
            } else {
                throw DataOutOfDateException()
            }
        }

    fun updateDataIfNeeded() = lastSyncTimeDao.isSyncNeeded()
        .flatMap { if (it) updateData() else Single.just(true) }

    private fun updateData() = Single.zip(
        updateStations(),
        updateKeywords()
    ) { result1, result2 -> result1 && result2 }
        .flatMap { if (it) lastSyncTimeDao.updateLastSync() else Single.just(false) }

    private fun updateStations() = api.getStations()
        .onErrorReturn { listOf() }
        .map { response ->
            response.map {
                Station(
                    it.id,
                    it.name,
                    it.nameSlug,
                    it.latitude,
                    it.longitude,
                    it.hits,
                    it.ibnr,
                    it.city,
                    it.region,
                    it.country,
                    it.localisedName
                )
            }
        }
        .flatMap { stationDao.updateStations(it) }
        .subscribeOn(io())

    private fun updateKeywords() = api.getKeywords()
        .onErrorReturn { listOf() }
        .map { response ->
            response.map {
                Keyword(
                    it.id,
                    it.keyword,
                    it.stationId
                )
            }
        }
        .flatMap { stationDao.updateKeywords(it) }
        .subscribeOn(io())

}