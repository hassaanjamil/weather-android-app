package com.weather.app.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.weather.app.BuildConfig
import com.weather.app.data.remote.model.other.Article
import com.weather.app.data.remote.model.other.Medium
import com.weather.app.data.remote.model.other.ResponseArticles
import com.weather.app.data.repository.MainRepository
import com.weather.app.ui.home.HomeViewModel
import com.weather.app.utils.Resource
import com.weather.app.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteDataViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiHelper

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<ResponseArticles>>

    @Before
    fun setUp() {
        // Anything that can happen before test will be here
    }

    @After
    fun tearDown() {

    }

    /*
     MODEL TEST
     */

    @Test
    fun testBuild() {

        val model = Article("", "",1221L, 100L, "NY Times",
            "2022-02-08", "today", "news", "", "",
            "","","Hassan", "","SampleTitle","",
            listOf(), listOf(), listOf(), listOf(), listOf(),1)
        listOf(model)
        val response = ResponseArticles("200", "", 1, listOf(model))
        assertEquals("200", response.status)
        assertEquals("", response.copyright)
        assertEquals(1, response.numResults)
        assertEquals(listOf(model), response.results)
        assertEquals("", model.url)
        assertEquals(1221L, model.id)
        assertEquals(100L, model.assetId)
        assertEquals("NY Times", model.source)
        assertEquals("2022-02-08", model.publishedDate)
        assertEquals("today", model.updated)
        assertEquals("news", model.section)
        assertEquals("", model.subsection)
        assertEquals("", model.nytdsection)
        assertEquals("", model.adxKeywords)
        assertEquals("", model.column)
        assertEquals("Hassan", model.byline)
        assertEquals("", model.type)
        assertEquals("SampleTitle", model.title)
        assertEquals("", model.uri)
        assertEquals("", model.abstract)
        assertEquals(1, model.etaId)
        assertEquals(emptyList<String>(), model.desFacet)
        assertEquals(emptyList<String>(), model.orgFacet)
        assertEquals(emptyList<String>(), model.perFacet)
        assertEquals(emptyList<String>(), model.geoFacet)
        assertEquals(emptyList<Medium>(), model.media)
    }

    /*
    URL VALIDITY TESTs
     */

    @Test
    fun testRequestUrlValidation() {
        if(BuildConfig.BASE_URL.contains("https")) {
            Assert.assertEquals("https://", BuildConfig.BASE_URL.substring(0, 8))
        } else {
            Assert.assertEquals("http://", BuildConfig.BASE_URL.substring(0, 8))
        }
    }

    @Test
    fun testRequestBaseUrlValidation() {
        val baseUrl = BuildConfig.BASE_URL
        Assert.assertEquals("https://api.nytimes.com/svc/mostpopular/v2/mostviewed/", baseUrl)
    }

    /*
    ARTICLES API TESTs
     */

    @Test
    fun givenServerResponse200_whenFetchMostPopularArticles_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(ResponseArticles())
                .`when`(apiHelper)
                .getMostPopularArticles()
            val viewModel = HomeViewModel(MainRepository(apiHelper))
            viewModel.getMostPopularArticles().observeForever(apiUsersObserver)
            Mockito.verify(apiHelper).getMostPopularArticles()
            Mockito.verify(apiUsersObserver).onChanged(Resource.success(ResponseArticles()))
            viewModel.getMostPopularArticles().removeObserver(apiUsersObserver)
        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            Mockito.doThrow(RuntimeException(errorMessage))
                .`when`(apiHelper)
                .getMostPopularArticles()
            val viewModel = HomeViewModel(MainRepository(apiHelper))
            viewModel.getMostPopularArticles().observeForever(apiUsersObserver)
            Mockito.verify(apiHelper).getMostPopularArticles()
            Mockito.verify(apiUsersObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString(),
                    null
                )
            )
            viewModel.getMostPopularArticles().removeObserver(apiUsersObserver)
        }
    }

}