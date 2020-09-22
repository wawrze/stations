package com.wawra.stations.database.daos

import androidx.room.Dao
import androidx.room.Insert
import com.wawra.stations.database.entities.Model
import io.reactivex.Single

@Dao
interface ModelDao {

    @Insert
    fun insert(model: Model): Single<Long>

}