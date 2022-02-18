package com.weather.app.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.data.repository.MainRepository
import com.weather.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    //private val articles = MutableLiveData<Resource<ResponseArticles>>()
    private val responseCurrentWeather = MutableLiveData<Resource<ResponseWeather>>()
    val location = MutableLiveData<Resource<Location>>()
    /*init {
        fetchMostPopularArticles()
    }*/

    /*private fun fetchMostPopularArticles() {
        viewModelScope.launch {
            articles.postValue(Resource.loading(null))
            try {
                val articlesFromApi = mainRepository.getMostPopularArticles()
                articles.postValue(Resource.success(articlesFromApi))
            } catch (e: Exception) {
                articles.postValue(Resource.error(e.toString(), null))
            }
        }
    }*/

    fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        query: String = "dubai",
    ) {
        viewModelScope.launch {
            this@HomeViewModel.responseCurrentWeather.postValue(Resource.loading(null))
            try {
                val response = mainRepository.getCurrentWeather(lat, lon, query)
                responseCurrentWeather.postValue(Resource.success(response))
            } catch (e: Exception) {
                responseCurrentWeather.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    /*fun getMostPopularArticles(): LiveData<Resource<ResponseArticles>> {
        return articles
    }*/

    fun getCurrentLocation(): LiveData<Resource<Location>> {
        return location
    }

    fun getCurrentWeather(): LiveData<Resource<ResponseWeather>> {
        return responseCurrentWeather
    }
}