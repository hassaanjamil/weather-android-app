package com.weather.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weather.app.data.remote.model.cities.Data
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.databinding.ItemSearchCityBinding
import com.weather.app.utils.toDateFormat
import java.util.*

class SearchAdapter :
    RecyclerView.Adapter<SearchAdapter.DataAdapterViewHolder>() {

    private var listCities = listOf<Data>()

    inner class DataAdapterViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun bindContent(item: Data) {
            if (binding is ItemSearchCityBinding) {
                Date().toDateFormat("MMMM dd, yyyy")
                val name = item.name
                binding.tvCityName.text = name
                val state = "${item.country}, ${item.countryCode}"
                binding.tvState.text = state
            }
        }

        fun bind(listForecast: List<Data>, position: Int) {
            bindContent(listForecast[position])
        }
    }

    companion object {
        private const val TYPE_ITEM = 0
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val binding = when (viewType) {
            TYPE_ITEM -> ItemSearchCityBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false)
            else -> throw IllegalArgumentException("Invalid type")
        }
        return DataAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(listCities, position)
    }

    override fun getItemCount() = listCities.size

    fun updateData(response: ResponseCities) {
        listCities = response.data
        notifyDataSetChanged()
    }
}