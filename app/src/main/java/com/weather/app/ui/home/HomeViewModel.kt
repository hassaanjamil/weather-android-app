package com.weather.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.data.remote.model.ResponseArticles
import com.weather.app.data.repository.MainRepository
import com.weather.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val articles = MutableLiveData<Resource<ResponseArticles>>()

    init {
        fetchMostPopularArticles()
    }

    private fun fetchMostPopularArticles() {
        viewModelScope.launch {
            articles.postValue(Resource.loading(null))
            try {
                val articlesFromApi = mainRepository.getMostPopularArticles()
                articles.postValue(Resource.success(articlesFromApi))
            } catch (e: Exception) {
                articles.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMostPopularArticles(): LiveData<Resource<ResponseArticles>> {
        return articles
    }
}