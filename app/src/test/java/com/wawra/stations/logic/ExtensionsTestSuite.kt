package com.wawra.stations.logic

import com.wawra.stations.BaseTestSuite
import org.junit.Assert
import org.junit.Test

class ExtensionsTestSuite : BaseTestSuite() {

    @Test
    fun shouldFixString() {
        // given
        val stringToFix = "ąĘłÓźż asdf GHJK öšáŠýš"
        val expectedResult = "AELOZZ ASDF GHJK OSASYS"
        // when
        val result = stringToFix.fix()
        // then
        Assert.assertEquals(expectedResult, result)
    }

}