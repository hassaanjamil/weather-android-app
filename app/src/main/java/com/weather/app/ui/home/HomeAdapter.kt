package com.weather.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.databinding.ItemHomeForecastBinding

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.DataAdapterViewHolder>() {

    private var adapterData = ResponseWeather()

    inner class DataAdapterViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /*private fun bindContent(item: ResponseWeather) {
            if (binding is ItemHomeWeatherBinding) {
                item.let {
                    binding..text = it.title
                    binding.author.text = it.byline
                    binding.date.text = it.updated?.toDateFormat("yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd")
                    if (it.media?.size!! > 0) {
                        val url = it.media?.get(0)?.mediaMetaData?.get(0)?.url
                        if (url != null) {
                            Log.d("image url", url)
                        }
                    }
                }
            }
        }

        fun bind(response: ResponseWeather, position: Int) {
            response.w?.get(position)?.let { bindContent(it) }
        }*/
    }

    companion object {
        private const val TYPE_FORECAST = 0
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_FORECAST
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val binding = when (viewType) {
            TYPE_FORECAST -> ItemHomeForecastBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false)
            else -> throw IllegalArgumentException("Invalid type")
        }
        return DataAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        //holder.bind(adapterData, position)
    }

    override fun getItemCount() = 1

    fun updateData(response: ResponseWeather) {
        adapterData = response
    }
}