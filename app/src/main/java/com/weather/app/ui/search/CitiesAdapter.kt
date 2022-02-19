package com.weather.app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weather.app.R
import com.weather.app.data.remote.model.cities.Data
import com.weather.app.databinding.ItemSearchCityBinding
import com.weather.app.utils.toDateFormat
import java.util.*

class CitiesAdapter :
    RecyclerView.Adapter<CitiesAdapter.DataAdapterViewHolder>() {

    private var listCities = listOf<Data>()
    private var isDB = false
    private lateinit var favClickListener: ItemClickListener
    private lateinit var itemClickListener: ItemClickListener

    inner class DataAdapterViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun bindContent(item: Data) {
            if (binding is ItemSearchCityBinding) {
                Date().toDateFormat("MMMM dd, yyyy")
                val name = item.name
                binding.tvCityName.text = name
                val state = "${item.country}, ${item.countryCode}"
                binding.tvState.text = state

                val resId =
                    if (isDB) R.drawable.ic_baseline_delete else R.drawable.ic_baseline_favorite_border
                binding.ivFavorite.setImageResource(resId)

                binding.ivFavorite.setOnClickListener {
                    favClickListener.onItemClick(it, item)
                }
                binding.root.setOnClickListener {
                    itemClickListener.onItemClick(it, item)
                }
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

    fun updateData(response: List<Data>, db: Boolean) {
        listCities = response
        isDB = db
        notifyDataSetChanged()
    }

    fun setFavoriteClickListener(listener: ItemClickListener) {
        favClickListener = listener
    }

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }
}

interface ItemClickListener {
    fun onItemClick(view: View, data: Data)
}