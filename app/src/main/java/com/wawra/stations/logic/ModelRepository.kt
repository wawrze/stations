package com.wawra.stations.logic

import com.wawra.stations.database.daos.ModelDao
import javax.inject.Inject

class ModelRepository @Inject constructor(var modelDao: ModelDao) {
}