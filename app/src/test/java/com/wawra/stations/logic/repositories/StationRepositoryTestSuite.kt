package com.wawra.stations.logic.repositories

import com.wawra.stations.BaseTestSuite
import com.wawra.stations.database.daos.LastSyncTimeDao
import com.wawra.stations.database.daos.StationDao
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.errors.DataOutOfDateException
import com.wawra.stations.network.ApiInterface
import com.wawra.stations.network.models.KeywordResponse
import com.wawra.stations.network.models.StationResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StationRepositoryTestSuite : BaseTestSuite() {

    private lateinit var stationDaoMock: StationDao
    private lateinit var lastSyncTimeDaoMock: LastSyncTimeDao
    private lateinit var apiMock: ApiInterface

    private lateinit var objectUnderTest: StationRepository

    @Before
    fun init() {
        stationDaoMock = mockk()
        lastSyncTimeDaoMock = mockk()
        apiMock = mockk()
        objectUnderTest = StationRepository(stationDaoMock, lastSyncTimeDaoMock, apiMock)
    }

    @Test
    fun shouldNotUpdateDataSyncNotNeeded() {
        // when
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(false)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify(exactly = 0) { lastSyncTimeDaoMock.updateLastSync() }
        assertTrue(result)
    }

    @Test
    fun shouldNotUpdateDataUpdateStationsError() {
        // given
        val keywords = listOf(KeywordResponse(1L, "keyword1", 11L))
        val keywordsSlot = slot<List<Keyword>>()
        val stationsSlot = slot<List<Station>>()
        // when
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(true)
        every { apiMock.getKeywords() } returns Single.just(keywords)
        every {
            stationDaoMock.updateKeywords(capture(keywordsSlot))
        } returns Single.just(true)
        every { apiMock.getStations() } returns Single.error(Exception())
        every {
            stationDaoMock.updateStations(capture(stationsSlot))
        } returns Single.just(false)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify { apiMock.getKeywords() }
        verify { stationDaoMock.updateKeywords(any()) }
        verify { apiMock.getStations() }
        verify { stationDaoMock.updateStations(any()) }
        verify { stationDaoMock.updateKeywords(any()) }
        verify(exactly = 0) { lastSyncTimeDaoMock.updateLastSync() }
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
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(true)
        every { apiMock.getKeywords() } returns Single.error(Exception())
        every {
            stationDaoMock.updateKeywords(capture(keywordsSlot))
        } returns Single.just(false)
        every { apiMock.getStations() } returns Single.just(stations)
        every {
            stationDaoMock.updateStations(capture(stationsSlot))
        } returns Single.just(true)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify { apiMock.getKeywords() }
        verify { stationDaoMock.updateKeywords(any()) }
        verify { apiMock.getStations() }
        verify { stationDaoMock.updateStations(any()) }
        verify { stationDaoMock.updateKeywords(any()) }
        verify(exactly = 0) { lastSyncTimeDaoMock.updateLastSync() }
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
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(true)
        every { apiMock.getKeywords() } returns Single.just(keywords)
        every {
            stationDaoMock.updateKeywords(capture(keywordsSlot))
        } returns Single.just(true)
        every { apiMock.getStations() } returns Single.just(stations)
        every {
            stationDaoMock.updateStations(capture(stationsSlot))
        } returns Single.just(true)
        every { lastSyncTimeDaoMock.updateLastSync() } returns Single.just(true)
        val result = objectUnderTest.updateDataIfNeeded().blockingGet()
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify { apiMock.getKeywords() }
        verify { stationDaoMock.updateKeywords(any()) }
        verify { apiMock.getStations() }
        verify { stationDaoMock.updateStations(any()) }
        verify { stationDaoMock.updateKeywords(any()) }
        verify { lastSyncTimeDaoMock.updateLastSync() }
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
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(false)
        every {
            stationDaoMock.getMatchingStations("keyword")
        } returns Single.just(stations)
        val result = objectUnderTest.getStationsByKeyword("keyword").blockingGet()
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify { stationDaoMock.getMatchingStations("keyword") }
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
        every { lastSyncTimeDaoMock.isSyncNeeded() } returns Single.just(true)
        every { apiMock.getStations() } returns Single.error(Exception())
        every { apiMock.getKeywords() } returns Single.error(Exception())
        every { stationDaoMock.updateStations(any()) } returns Single.just(false)
        every { stationDaoMock.updateKeywords(any()) } returns Single.just(false)
        try {
            result = objectUnderTest.getStationsByKeyword("keyword").blockingGet()
        } catch (e: Exception) {
            error = e
        }
        // then
        verify { lastSyncTimeDaoMock.isSyncNeeded() }
        verify { apiMock.getStations() }
        verify { apiMock.getKeywords() }
        verify { stationDaoMock.updateStations(any()) }
        verify { stationDaoMock.updateKeywords(any()) }
        verify(exactly = 0) { stationDaoMock.getMatchingStations(any()) }
        assertNull(result)
        assertTrue(error?.cause is DataOutOfDateException)
    }

}