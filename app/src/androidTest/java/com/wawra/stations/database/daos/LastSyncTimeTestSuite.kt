package com.wawra.stations.database.daos

import com.wawra.stations.BaseDaoTestSuite
import com.wawra.stations.database.entities.LastSyncTime
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LastSyncTimeTestSuite : BaseDaoTestSuite() {

    private val objectUnderTest = db.lastSyncDao()

    @Test
    fun shouldReturnSyncNeededNoLastSync() {
        // when
        val result = objectUnderTest.isSyncNeeded().blockingGet()
        // then
        assertTrue(result)
    }

    @Test
    fun shouldReturnSyncNoNeeded() {
        // given
        val lastSync = LastSyncTime(System.currentTimeMillis() - 1000L)
        // when
        objectUnderTest.insert(lastSync).blockingGet()
        val result = objectUnderTest.isSyncNeeded().blockingGet()
        // then
        assertFalse(result)
    }

    @Test
    fun shouldReturnSyncNeeded() {
        // given
        val lastSync = LastSyncTime(System.currentTimeMillis() - 1000L * 3600 * 25)
        // when
        objectUnderTest.insert(lastSync).blockingGet()
        val result = objectUnderTest.isSyncNeeded().blockingGet()
        // then
        assertTrue(result)
    }

    @Test
    fun shouldUpdateLastSyncNoLastSync() {
        // when
        val result = objectUnderTest.updateLastSync().blockingGet()
        val isSyncNeeded = objectUnderTest.isSyncNeeded().blockingGet()
        // then
        assertTrue(result)
        assertFalse(isSyncNeeded)
    }

    @Test
    fun shouldUpdateLastSync() {
        // given
        val lastSync = LastSyncTime(System.currentTimeMillis() - 1000L * 3600 * 25)
        // when
        objectUnderTest.insert(lastSync).blockingGet()
        val result = objectUnderTest.updateLastSync().blockingGet()
        val isSyncNeeded = objectUnderTest.isSyncNeeded().blockingGet()
        // then
        assertTrue(result)
        assertFalse(isSyncNeeded)
    }

}