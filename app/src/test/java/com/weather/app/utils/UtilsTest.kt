package com.weather.app.utils

import com.weather.app.BuildConfig
import com.weather.app.data.local.entity.Data
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * @author Hassan Jamil
 */
class UtilsTest {
    /*
    BUILD TESTS
     */
    @Test
    fun testBuild() {
        assertEquals("com.weather.app", BuildConfig.APPLICATION_ID)
        assertEquals("debug", BuildConfig.BUILD_TYPE)
        assertEquals(1, BuildConfig.VERSION_CODE)
        assertEquals(true, BuildConfig.DEBUG)
        assertEquals("1.0", BuildConfig.VERSION_NAME)
        assertEquals("5e18463872mshb9da089921e0e15p10fe85jsn39fbf83f4351", BuildConfig.API_KEY)
        assertEquals("https://community-open-weather-map.p.rapidapi.com/",
            BuildConfig.BASE_URL_WEATHER)
    }

    /*
    UTILITY TESTS
     */
    @Test
    fun testDateConversion() {
        val actual = "2022-02-08 14:08:30".toDateFormat("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")
        assertEquals("2022-02-08", actual)
    }

    @Test
    fun testResource() {
        assertNotNull(Resource.success(Data(name = "ABC")))
        assertNull(Resource.error("Exception", null).data)
        assertNull(Resource.loading(null).message)
    }

    @Test
    fun testStatus() {
        assertEquals(Status.ERROR.ordinal, 1)
    }
}