package com.wawra.stations.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wawra.stations.database.entities.LastSyncTime
import io.reactivex.Single

@Dao
abstract class LastSyncTimeDao {

    @Insert
    abstract fun insert(lastSync: LastSyncTime): Single<Long>

    @Query("DELETE FROM last_sync_time")
    protected abstract fun deleteAll(): Single<Int>

    @Query("SELECT last_sync < (:now - $DAY_MILLIS) FROM last_sync_time LIMIT 1")
    protected abstract fun isSyncNeeded(now: Long): Single<Boolean>

    fun updateLastSync() = deleteAll()
        .flatMap { insert(LastSyncTime(System.currentTimeMillis())) }
        .map { it > 0L }

    fun isSyncNeeded() = isSyncNeeded(System.currentTimeMillis())
        .onErrorReturn { true }

    companion object {
        private const val DAY_MILLIS: Long = 24 * 3600 * 1000
    }

}