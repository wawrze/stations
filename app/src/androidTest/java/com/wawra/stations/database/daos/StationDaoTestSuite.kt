package com.wawra.stations.database.daos

import com.wawra.stations.BaseDaoTestSuite
import com.wawra.stations.database.entities.Keyword
import com.wawra.stations.database.entities.Station
import junit.framework.TestCase
import org.junit.Test

class StationDaoTestSuite : BaseDaoTestSuite() {

    private val objectUnderTest = db.stationDao()

    @Test
    fun shouldFetchStationByKeywordsStartingWith() {
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
            ),
            Station(
                3L,
                "station3",
                "station3slug",
                8.6,
                4.2,
                23,
                33L,
                "city3",
                "region3",
                "country3",
                "name3"
            ),
            Station(
                4L,
                "station4",
                "station4slug",
                1.3,
                5.7,
                24,
                34L,
                "city4",
                "region4",
                "country4",
                "name4"
            ),
            Station(
                5L,
                "station5",
                "station5slug",
                1.987,
                6.5432,
                25,
                35L,
                "city5",
                "region5",
                "country5",
                "name5"
            )
        )
        val keywords = listOf(
            Keyword(11L, "keyword", 1L),
            Keyword(12L, "keyw", 2L),
            Keyword(13L, "word", 3L),
            Keyword(14L, "test", 4L),
            Keyword(15L, "other", 5L),
        )
        // when
        objectUnderTest.updateStations(stations).blockingGet()
        objectUnderTest.updateKeywords(keywords).blockingGet()
        val result = objectUnderTest.getMatchingStations("key").blockingGet()
        // then
        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals(1L, result[0].stationId)
        TestCase.assertEquals("station1", result[0].name)
        TestCase.assertEquals(2L, result[1].stationId)
        TestCase.assertEquals("station2", result[1].name)
    }

    @Test
    fun shouldFetchStationsByKeywordsEndingWith() {
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
            ),
            Station(
                3L,
                "station3",
                "station3slug",
                8.6,
                4.2,
                23,
                33L,
                "city3",
                "region3",
                "country3",
                "name3"
            ),
            Station(
                4L,
                "station4",
                "station4slug",
                1.3,
                5.7,
                24,
                34L,
                "city4",
                "region4",
                "country4",
                "name4"
            ),
            Station(
                5L,
                "station5",
                "station5slug",
                1.987,
                6.5432,
                25,
                35L,
                "city5",
                "region5",
                "country5",
                "name5"
            )
        )
        val keywords = listOf(
            Keyword(11L, "keyword", 1L),
            Keyword(12L, "keyw", 2L),
            Keyword(13L, "word", 3L),
            Keyword(14L, "test", 4L),
            Keyword(15L, "other", 5L),
        )
        // when
        objectUnderTest.updateStations(stations).blockingGet()
        objectUnderTest.updateKeywords(keywords).blockingGet()
        val result = objectUnderTest.getMatchingStations("ord").blockingGet()
        // then
        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals(1L, result[0].stationId)
        TestCase.assertEquals("station1", result[0].name)
        TestCase.assertEquals(3L, result[1].stationId)
        TestCase.assertEquals("station3", result[1].name)
    }

    @Test
    fun shouldFetchMatchingKeywordsExactMatching() {
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
            ),
            Station(
                3L,
                "station3",
                "station3slug",
                8.6,
                4.2,
                23,
                33L,
                "city3",
                "region3",
                "country3",
                "name3"
            ),
            Station(
                4L,
                "station4",
                "station4slug",
                1.3,
                5.7,
                24,
                34L,
                "city4",
                "region4",
                "country4",
                "name4"
            ),
            Station(
                5L,
                "station5",
                "station5slug",
                1.987,
                6.5432,
                25,
                35L,
                "city5",
                "region5",
                "country5",
                "name5"
            )
        )
        val keywords = listOf(
            Keyword(11L, "keyword", 1L),
            Keyword(12L, "keyw", 2L),
            Keyword(13L, "word", 3L),
            Keyword(14L, "test", 4L),
            Keyword(15L, "other", 5L),
        )
        // when
        objectUnderTest.updateStations(stations).blockingGet()
        objectUnderTest.updateKeywords(keywords).blockingGet()
        val result = objectUnderTest.getMatchingStations("test").blockingGet()
        // then
        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(4L, result[0].stationId)
        TestCase.assertEquals("station4", result[0].name)
    }

}