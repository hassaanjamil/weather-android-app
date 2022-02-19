package com.weather.app.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.data.remote.model.cities.Data
import com.weather.app.data.repository.MainRepository
import com.weather.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val responseCities = MutableLiveData<Resource<List<Data>>>()
    private val city = MutableLiveData<Resource<Any>>()

    fun fetchCities() {
        viewModelScope.launch {
            this@FavoritesViewModel.responseCities.postValue(Resource.loading(null))
            try {
                val weather = mainRepository.getCities()
                responseCities.postValue(Resource.success(weather))
            } catch (e: Exception) {
                responseCities.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun delete(data: Data) {
        viewModelScope.launch {
            city.postValue(Resource.loading(null))
            try {
                val result = mainRepository.delete(data)
                city.postValue(Resource.success(result))
            } catch (e: Exception) {
                city.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getCities(): LiveData<Resource<List<Data>>> {
        return responseCities
    }

    fun getCityDb(): LiveData<Resource<Any>> {
        return city
    }
}