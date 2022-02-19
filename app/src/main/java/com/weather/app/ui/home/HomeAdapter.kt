package com.weather.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weather.app.data.remote.model.forecast.Forecast
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.databinding.ItemHomeForecastBinding
import com.weather.app.utils.convertUnixTimeMillisToDate
import com.weather.app.utils.toDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.DataAdapterViewHolder>() {

    private var listForecast = listOf<Forecast>()

    inner class DataAdapterViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun bindContent(item: Forecast) {
            if (binding is ItemHomeForecastBinding) {
                Date().toDateFormat("MMMM dd, yyyy")
                val date = "${item.dt?.toLong()?.convertUnixTimeMillisToDate("MMM dd")}"
                binding.tvDate.text = date
                val avgTemp = "${item.temp?.average?.roundToInt()}º C"
                binding.tvTemp.text = avgTemp
                val avgTempMin = "Min: ${item.temp?.average_max?.roundToInt()}º C"
                binding.tvTempMin.text = avgTempMin
                val avgTempMax = "Max: ${item.temp?.average_min?.roundToInt()}º C"
                binding.tvTempMax.text = avgTempMax
                val humidity = "H: ${item.humidity?.roundToInt()}%"
                binding.tvHumidity.text = humidity
            }
        }

        fun bind(listForecast: List<Forecast>, position: Int) {
            bindContent(listForecast[position])
        }
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
        holder.bind(listForecast, position)
    }

    override fun getItemCount() = listForecast.size

    fun updateData(response: ResponseForecast) {
        listForecast = response.list!!
    }
}