package com.wawra.stations.presentation.main

import com.wawra.stations.BaseTestSuite
import com.wawra.stations.database.entities.Station
import com.wawra.stations.logic.calculations.DistanceCalculator
import com.wawra.stations.logic.errors.DataOutOfDateException
import com.wawra.stations.logic.repositories.StationRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTestSuite : BaseTestSuite() {

    private lateinit var stationRepositoryMock: StationRepository
    private lateinit var objectUnderTest: MainViewModel

    @Before
    fun prepare() {
        stationRepositoryMock = mockk()
        objectUnderTest = MainViewModel(stationRepositoryMock)
    }

    @After
    fun cleanUp() {
        objectUnderTest.cancelActions()
    }

    @Test
    fun shouldFetchStations1() {
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
            stationRepositoryMock.getStationsByKeyword("TEXT")
        } returns Single.just(stations)
        objectUnderTest.getMatchingStations("text", true)
        // then
        verify { stationRepositoryMock.getStationsByKeyword("TEXT") }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultStations = objectUnderTest.stations1.value
        assertNull(dataOutOfDateError)
        assertNull(unknownError)
        assertNotNull(resultStations)
        resultStations!!
        assertEquals(2, resultStations.size)
        assertEquals(1L, resultStations[0].stationId)
        assertEquals(2L, resultStations[1].stationId)
    }

    @Test
    fun shouldNotFetchStations1() {
        // when
        every {
            stationRepositoryMock.getStationsByKeyword("TEXT")
        } returns Single.error(DataOutOfDateException())
        objectUnderTest.getMatchingStations("text", true)
        // then
        verify { stationRepositoryMock.getStationsByKeyword("TEXT") }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultStations = objectUnderTest.stations1.value
        assertTrue(dataOutOfDateError == true)
        assertNull(unknownError)
        assertNull(resultStations)
    }

    @Test
    fun shouldFetchStations2() {
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
            stationRepositoryMock.getStationsByKeyword("TEXT")
        } returns Single.just(stations)
        objectUnderTest.getMatchingStations("text", false)
        // then
        verify { stationRepositoryMock.getStationsByKeyword("TEXT") }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultStations = objectUnderTest.stations2.value
        assertNull(dataOutOfDateError)
        assertNull(unknownError)
        assertNotNull(resultStations)
        resultStations!!
        assertEquals(2, resultStations.size)
        assertEquals(1L, resultStations[0].stationId)
        assertEquals(2L, resultStations[1].stationId)
    }

    @Test
    fun shouldNotFetchStations2() {
        // when
        every {
            stationRepositoryMock.getStationsByKeyword("TEXT")
        } returns Single.error(DataOutOfDateException())
        objectUnderTest.getMatchingStations("text", false)
        // then
        verify { stationRepositoryMock.getStationsByKeyword("TEXT") }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultStations = objectUnderTest.stations2.value
        assertTrue(dataOutOfDateError == true)
        assertNull(unknownError)
        assertNull(resultStations)
    }

    @Test
    fun shouldGetDistance() {
        // given
        val station1 = Station(
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
        val station2 = Station(
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
        mockkObject(DistanceCalculator)
        // when
        every {
            DistanceCalculator.calculateDistanceInKm(0.1234, 5.6789, 9.876, 5.4321)
        } returns Single.just(123.45)
        objectUnderTest.getDistance(station1, station2)
        // then
        verify { DistanceCalculator.calculateDistanceInKm(0.1234, 5.6789, 9.876, 5.4321) }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultDistance = objectUnderTest.distance.value
        assertNull(dataOutOfDateError)
        assertNull(unknownError)
        assertEquals(123.45, resultDistance ?: 0.0, 0.0)
        // clean up
        unmockkAll()
    }

    @Test
    fun shouldNotGetDistance() {
        // given
        val station1 = Station(
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
        val station2 = Station(
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
        mockkObject(DistanceCalculator)
        // when
        every {
            DistanceCalculator.calculateDistanceInKm(0.1234, 5.6789, 9.876, 5.4321)
        } returns Single.error(Exception())
        objectUnderTest.getDistance(station1, station2)
        // then
        verify { DistanceCalculator.calculateDistanceInKm(0.1234, 5.6789, 9.876, 5.4321) }
        val dataOutOfDateError = objectUnderTest.dataOutOfDateError.value
        val unknownError = objectUnderTest.unknownError.value
        val resultDistance = objectUnderTest.distance.value
        assertNull(dataOutOfDateError)
        assertEquals(3, unknownError)
        assertNull(resultDistance)
        // clean up
        unmockkAll()
    }

}