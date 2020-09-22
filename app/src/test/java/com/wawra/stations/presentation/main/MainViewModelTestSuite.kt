package com.wawra.stations.presentation.main

import com.wawra.stations.BaseTestSuite
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.exceptions.DataOutOfDateException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTestSuite : BaseTestSuite() {

    private lateinit var objectUnderTest: MainViewModel

    @Before
    fun prepare() {
        objectUnderTest = MainViewModel(mockk())
    }

    @After
    fun cleanUp() {
        objectUnderTest.cancelActions()
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
            ),
            Station(
                2L,
                "station2",
                "station2slug",
                9.876,
                5.4321,
                22,
                32L,
                "city2",
                "region2",
                "country2",
                "name2"
            )
        )
        // when
        every {
            objectUnderTest.stationRepository.getStationsByKeyword("text")
        } returns Single.just(stations)
        objectUnderTest.getMatchingStations("text")
        // then
        verify { objectUnderTest.stationRepository.getStationsByKeyword("text") }
        val actionResult = objectUnderTest.getStationsResult.value
        val resultStations = objectUnderTest.stations.value
        assertTrue(actionResult == true)
        assertNotNull(resultStations)
        resultStations!!
        assertEquals(2, resultStations.size)
        assertEquals(1L, resultStations[0].stationId)
        assertEquals(2L, resultStations[1].stationId)
    }

    @Test
    fun shouldNotFetchStations() {
        // when
        every {
            objectUnderTest.stationRepository.getStationsByKeyword("text")
        } returns Single.error(DataOutOfDateException())
        objectUnderTest.getMatchingStations("text")
        // then
        verify { objectUnderTest.stationRepository.getStationsByKeyword("text") }
        val actionResult = objectUnderTest.getStationsResult.value
        val resultStations = objectUnderTest.stations.value
        assertTrue(actionResult == false)
        assertNull(resultStations)
    }

}