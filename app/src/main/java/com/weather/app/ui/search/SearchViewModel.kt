package com.weather.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.data.repository.MainRepository
import com.weather.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val responseCities = MutableLiveData<Resource<ResponseCities>>()

    fun fetchCities(
        prefix: String,
    ) {
        viewModelScope.launch {
            this@SearchViewModel.responseCities.postValue(Resource.loading(null))
            try {
                val weather = mainRepository.getCities(prefix)
                responseCities.postValue(Resource.success(weather))
            } catch (e: Exception) {
                responseCities.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getCities(): LiveData<Resource<ResponseCities>> {
        return responseCities
    }
}