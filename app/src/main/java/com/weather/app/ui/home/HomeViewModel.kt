package com.weather.app.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.data.repository.MainRepository
import com.weather.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val responseCurrentWeather = MutableLiveData<Resource<ResponseWeather>>()
    private val responseForecast = MutableLiveData<Resource<ResponseForecast>>()
    val location = MutableLiveData<Resource<Location>>()

    fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        query: String = "dubai",
    ) {
        viewModelScope.launch {
            this@HomeViewModel.responseForecast.postValue(Resource.loading(null))
            try {
                val weather = mainRepository.getCurrentWeather(lat, lon, query)
                responseCurrentWeather.postValue(Resource.success(weather))
                val forecast = mainRepository.getMonthlyForecast(lat, lon, query)
                responseForecast.postValue(Resource.success(forecast))
            } catch (e: Exception) {
                responseForecast.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getCurrentLocation(): LiveData<Resource<Location>> {
        return location
    }

    fun getCurrentWeather(): LiveData<Resource<ResponseWeather>> {
        return responseCurrentWeather
    }

    fun getMonthlyForecast(): LiveData<Resource<ResponseForecast>> {
        return responseForecast
    }
}