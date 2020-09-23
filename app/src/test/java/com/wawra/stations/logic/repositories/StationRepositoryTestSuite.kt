package com.wawra.stations.logic.repositories

import com.wawra.stations.BaseTestSuite
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.errors.DataOutOfDateException
import com.wawra.stations.network.models.KeywordResponse
import com.wawra.stations.network.models.StationResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Test

class StationRepositoryTestSuite : BaseTestSuite() {

    private val objectUnderTest = StationRepository(mockk(), mockk(), mockk())

    @Test
    fun shouldNotUpdateDataSyncNotNeeded() {
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(false)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify(exactly = 0) { objectUnderTest.lastSyncTimeDao.updateLastSync() }
        assertTrue(result)
    }

    @Test
    fun shouldNotUpdateDataUpdateStationsError() {
        // given
        val keywords = listOf(KeywordResponse(1L, "keyword1", 11L))
        val keywordsSlot = slot<List<Keyword>>()
        val stationsSlot = slot<List<Station>>()
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(true)
        every { objectUnderTest.api.getKeywords() } returns Single.just(keywords)
        every {
            objectUnderTest.stationDao.updateKeywords(capture(keywordsSlot))
        } returns Single.just(true)
        every { objectUnderTest.api.getStations() } returns Single.error(Exception())
        every {
            objectUnderTest.stationDao.updateStations(capture(stationsSlot))
        } returns Single.just(false)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify { objectUnderTest.api.getKeywords() }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify { objectUnderTest.api.getStations() }
        verify { objectUnderTest.stationDao.updateStations(any()) }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify(exactly = 0) { objectUnderTest.lastSyncTimeDao.updateLastSync() }
        assertFalse(result)
        assertEquals(1, keywordsSlot.captured.size)
        assertEquals(0, stationsSlot.captured.size)
    }

    @Test
    fun shouldNotUpdateDataUpdateKeywordsError() {
        // given
        val stations = listOf(
            StationResponse(
                1L,
                "station1",
                "station1slug",
                0.1234,
                5.6789,
                21,
                31L,
                "city1",
                "region1",
                "country1",
                "name1"
            )
        )
        val keywordsSlot = slot<List<Keyword>>()
        val stationsSlot = slot<List<Station>>()
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(true)
        every { objectUnderTest.api.getKeywords() } returns Single.error(Exception())
        every {
            objectUnderTest.stationDao.updateKeywords(capture(keywordsSlot))
        } returns Single.just(false)
        every { objectUnderTest.api.getStations() } returns Single.just(stations)
        every {
            objectUnderTest.stationDao.updateStations(capture(stationsSlot))
        } returns Single.just(true)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify { objectUnderTest.api.getKeywords() }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify { objectUnderTest.api.getStations() }
        verify { objectUnderTest.stationDao.updateStations(any()) }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify(exactly = 0) { objectUnderTest.lastSyncTimeDao.updateLastSync() }
        assertFalse(result)
        assertEquals(0, keywordsSlot.captured.size)
        assertEquals(1, stationsSlot.captured.size)
    }

    @Test
    fun shouldUpdateData() {
        // given
        val keywords = listOf(KeywordResponse(1L, "keyword1", 11L))
        val stations = listOf(
            StationResponse(
                1L,
                "station1",
                "station1slug",
                0.1234,
                5.6789,
                21,
                31L,
                "city1",
                "region1",
                "country1",
                "name1"
            )
        )
        val keywordsSlot = slot<List<Keyword>>()
        val stationsSlot = slot<List<Station>>()
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(true)
        every { objectUnderTest.api.getKeywords() } returns Single.just(keywords)
        every {
            objectUnderTest.stationDao.updateKeywords(capture(keywordsSlot))
        } returns Single.just(true)
        every { objectUnderTest.api.getStations() } returns Single.just(stations)
        every {
            objectUnderTest.stationDao.updateStations(capture(stationsSlot))
        } returns Single.just(true)
        every { objectUnderTest.lastSyncTimeDao.updateLastSync() } returns Single.just(true)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify { objectUnderTest.api.getKeywords() }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify { objectUnderTest.api.getStations() }
        verify { objectUnderTest.stationDao.updateStations(any()) }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify { objectUnderTest.lastSyncTimeDao.updateLastSync() }
        assertTrue(result)
        assertEquals(1, keywordsSlot.captured.size)
        assertEquals(1, stationsSlot.captured.size)
    }

    @Test
    fun shouldFetchStations() {
        // given
        val stations = listOf(
            Station(
                1L,
                "station1",
                "station1slug",
                0.1234,
                5.6789,
                21,
                31L,
                "city1",
                "region1",
                "country1",
                "name1"
            )
        )
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(false)
        every {
            objectUnderTest.stationDao.getMatchingStations("keyword")
        } returns Single.just(stations)
        val result = objectUnderTest.getStationsByKeyword("keyword").blockingGet()
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify { objectUnderTest.stationDao.getMatchingStations("keyword") }
        assertEquals(1, result.size)
        assertEquals(1L, result[0].stationId)
        assertEquals("station1", result[0].name)
    }

    @Test
    fun shouldNotFetchStationsDataNotUpdated() {
        // given
        var result: List<Station>? = null
        var error: Exception? = null
        // when
        every { objectUnderTest.lastSyncTimeDao.isSyncNeeded() } returns Single.just(true)
        every { objectUnderTest.api.getStations() } returns Single.error(Exception())
        every { objectUnderTest.api.getKeywords() } returns Single.error(Exception())
        every { objectUnderTest.stationDao.updateStations(any()) } returns Single.just(false)
        every { objectUnderTest.stationDao.updateKeywords(any()) } returns Single.just(false)
        try {
            result = objectUnderTest.getStationsByKeyword("keyword").blockingGet()
        } catch (e: Exception) {
            error = e
        }
        // then
        verify { objectUnderTest.lastSyncTimeDao.isSyncNeeded() }
        verify { objectUnderTest.api.getStations() }
        verify { objectUnderTest.api.getKeywords() }
        verify { objectUnderTest.stationDao.updateStations(any()) }
        verify { objectUnderTest.stationDao.updateKeywords(any()) }
        verify(exactly = 0) { objectUnderTest.stationDao.getMatchingStations(any()) }
        assertNull(result)
        assertTrue(error?.cause is DataOutOfDateException)
    }

}