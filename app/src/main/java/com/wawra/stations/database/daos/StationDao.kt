package com.wawra.stations.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.Station
import io.reactivex.Single

@Dao
abstract class StationDao {

    @Insert
    protected abstract fun insertKeywords(keywords: List<Keyword>): Single<List<Long>>

    @Insert
    protected abstract fun insertStations(stations: List<Station>): Single<List<Long>>

    @Query("DELETE FROM keyword")
    protected abstract fun deleteAllKeywords(): Single<Int>

    @Query("DELETE FROM station")
    protected abstract fun deleteAllStations(): Single<Int>

    @Query(
        """
        SELECT s.*
        FROM station s
        JOIN keyword k ON k.station_id = s.station_id AND k.keyword LIKE :text 
        """
    )
    protected abstract fun getStationsByKeyword(text: String): Single<List<Station>>

    fun getMatchingStations(text: String) = getStationsByKeyword("%$text%")

    fun updateStations(stations: List<Station>) = deleteAllStations()
        .flatMap { insertStations(stations) }
        .map { it.isNotEmpty() }

    fun updateKeywords(keywords: List<Keyword>) = deleteAllKeywords()
        .flatMap { insertKeywords(keywords) }
        .map { it.isNotEmpty() }

}