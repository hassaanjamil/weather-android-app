package com.weather.app.utils

import com.weather.app.BuildConfig
import com.weather.app.data.remote.model.Article
import org.junit.Test

import org.junit.Assert.*

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
        assertEquals("OQcEo9fdy3pn8YA5xt5Fow5RyU4bH3mE", BuildConfig.API_KEY)
        assertEquals("https://api.nytimes.com/svc/mostpopular/v2/mostviewed/", BuildConfig.BASE_URL)
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
        assertNotNull(Resource.success(Article(title = "ABC")))
        assertNull(Resource.error("Exception", null).data)
        assertNull(Resource.loading(null).message)
    }

    @Test
    fun testStatus() {
        assertEquals(Status.ERROR.ordinal, 1)
    }
}