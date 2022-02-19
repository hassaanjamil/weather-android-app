package com.weather.app.ui.detail

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
class DetailViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val responseCurrentWeather = MutableLiveData<Resource<ResponseWeather>>()
    val location = MutableLiveData<Resource<Location>>()

    fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        query: String = "dubai",
    ) {
        viewModelScope.launch {
            this@DetailViewModel.responseCurrentWeather.postValue(Resource.loading(null))
            try {
                val weather = mainRepository.getCurrentWeather(lat, lon, query)
                responseCurrentWeather.postValue(Resource.success(weather))
            } catch (e: Exception) {
                responseCurrentWeather.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getCurrentWeather(): LiveData<Resource<ResponseWeather>> {
        return responseCurrentWeather
    }

    fun getLocation(): LiveData<Resource<Location>> {
        return location
    }
}