package com.wawra.stations.network

import com.wawra.stations.BaseNetworkTestSuite
import com.wawra.stations.network.models.KeywordResponse
import com.wawra.stations.network.models.StationResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class ApiInterfaceTestSuite : BaseNetworkTestSuite() {

    private lateinit var mockServer: MockWebServer
    private lateinit var objectUnderTest: ApiInterface

    @Before
    fun init() {
        mockServer = MockWebServer()
        mockServer.start(9080)
        objectUnderTest = createRetrofit(mockServer.url("/"))
            .create(ApiInterface::class.java)
    }

    @After
    fun cleanUp() = mockServer.shutdown()

    @Test
    fun shouldFetchStations() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"name\": \"Name 1\"," +
                        "\"name_slug\": \"name-1\"," +
                        "\"latitude\": 0.1234," +
                        "\"longitude\": 5.6789," +
                        "\"hits\": 2," +
                        "\"ibnr\": 3," +
                        "\"city\": \"City1\"," +
                        "\"region\": \"region1\"," +
                        "\"country\": \"country1\"," +
                        "\"localised_name\": \"localisedName1\"" +
                        "}," +
                        "{" +
                        "\"id\": 2," +
                        "\"name\": \"Name 2\"," +
                        "\"name_slug\": \"name-2\"," +
                        "\"latitude\": 9.8765," +
                        "\"longitude\": 4.3210," +
                        "\"hits\": 4," +
                        "\"ibnr\": 5," +
                        "\"city\": \"City2\"," +
                        "\"region\": \"region2\"," +
                        "\"country\": \"country2\"," +
                        "\"localised_name\": null" +
                        "}" +
                        "]"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getStations()
        val testObserver = TestObserver<List<StationResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/stations", request?.path)
        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Name 1", result[0].name)
        assertEquals("name-1", result[0].nameSlug)
        assertEquals(0.1234, result[0].latitude, 0.0)
        assertEquals(5.6789, result[0].longitude, 0.0)
        assertEquals(2, result[0].hits)
        assertEquals(3, result[0].ibnr)
        assertEquals("City1", result[0].city)
        assertEquals("region1", result[0].region)
        assertEquals("country1", result[0].country)
        assertEquals("localisedName1", result[0].localisedName)
        assertNull(result[1].localisedName)
    }

    @Test
    fun shouldFetchOneStation() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"name\": \"Name 1\"," +
                        "\"name_slug\": \"name-1\"," +
                        "\"latitude\": 0.1234," +
                        "\"longitude\": 5.6789," +
                        "\"hits\": 2," +
                        "\"ibnr\": 3," +
                        "\"city\": \"City1\"," +
                        "\"region\": \"region1\"," +
                        "\"country\": \"country1\"," +
                        "\"localised_name\": \"localisedName1\"" +
                        "}" +
                        "]"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getStations()
        val testObserver = TestObserver<List<StationResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/stations", request?.path)
        assertEquals(1, result.size)
    }

    @Test
    fun shouldFetchStationEmptyList() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[]")
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getStations()
        val testObserver = TestObserver<List<StationResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/stations", request?.path)
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchKeywords() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"keyword\": \"keyword1\"," +
                        "\"station_id\": 3" +
                        "}," +
                        "{" +
                        "\"id\": 2," +
                        "\"keyword\": \"keyword2\"," +
                        "\"station_id\": 4" +
                        "}" +
                        "]"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getKeywords()
        val testObserver = TestObserver<List<KeywordResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/station_keywords", request?.path)
        assertEquals(2, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("keyword1", result[0].keyword)
        assertEquals(3L, result[0].stationId)
    }

    @Test
    fun shouldFetchOneKeyword() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"keyword\": \"keyword1\"," +
                        "\"station_id\": 3" +
                        "}" +
                        "]"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getKeywords()
        val testObserver = TestObserver<List<KeywordResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/station_keywords", request?.path)
        assertEquals(1, result.size)
    }

    @Test
    fun shouldFetchKeywordsEmptyList() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[]")
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getKeywords()
        val testObserver = TestObserver<List<KeywordResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertEquals("/station_keywords", request?.path)
        assertEquals(0, result.size)
    }

}