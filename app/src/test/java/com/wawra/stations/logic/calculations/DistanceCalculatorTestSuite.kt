package com.wawra.stations.logic.calculations

import com.wawra.stations.BaseTestSuite
import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceCalculatorTestSuite : BaseTestSuite() {

    @Test
    fun shouldCalculateDistance1() {
        // given
        val lat1 = 53.0
        val lon1 = 22.0
        val lat2 = 54.0
        val lon2 = 23.0
        // when
        val result = DistanceCalculator.calculateDistanceInKm(lat1, lon1, lat2, lon2).blockingGet()
        // then
        assertEquals(129.38, result, 0.1)
    }

    @Test
    fun shouldCalculateDistance2() {
        // given
        val lat1 = 1.234
        val lon1 = -5.6789
        val lat2 = 9.87654
        val lon2 = 3.21
        // when
        val result = DistanceCalculator.calculateDistanceInKm(lat1, lon1, lat2, lon2).blockingGet()
        // then
        assertEquals(1374.56, result, 0.1)
    }

    @Test
    fun shouldCalculateDistance3() {
        // given
        val lat1 = -1.234
        val lon1 = -5.6789
        val lat2 = 9.87654
        val lon2 = 3.21
        // when
        val result = DistanceCalculator.calculateDistanceInKm(lat1, lon1, lat2, lon2).blockingGet()
        // then
        assertEquals(1579.42, result, 0.1)
    }

    @Test
    fun shouldCalculateDistance4() {
        // given
        val lat1 = 12.345
        val lon1 = 6.789
        val lat2 = -1.234
        val lon2 = -5.678
        // when
        val result = DistanceCalculator.calculateDistanceInKm(lat1, lon1, lat2, lon2).blockingGet()
        // then
        assertEquals(2043.12, result, 0.1)
    }

    @Test
    fun shouldCalculateDistance5() {
        // given
        val lat1 = 53.0
        val lon1 = 22.0
        val lat2 = 53.0
        val lon2 = 22.0
        // when
        val result = DistanceCalculator.calculateDistanceInKm(lat1, lon1, lat2, lon2).blockingGet()
        // then
        assertEquals(0.0, result, 0.0)
    }

}