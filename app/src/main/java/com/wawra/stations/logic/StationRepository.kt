package com.wawra.stations.logic

import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import com.wawra.stations.network.ApiInterface
import javax.inject.Inject

class StationRepository @Inject constructor(
    var stationDao: StationDao,
    var lastSyncTimeDao: LastSyncTimeDao,
    var api: ApiInterface
) {
}