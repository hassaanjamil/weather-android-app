package com.weather.app.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.weather.app.data.remote.model.Article
import com.weather.app.data.remote.model.ResponseArticles
import com.weather.app.databinding.ItemLayoutArticleBinding
import com.weather.app.utils.toDateFormat

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.DataAdapterViewHolder>() {

    private var adapterData = ResponseArticles()

    inner class DataAdapterViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun bindContent(item: Article) {
            if (binding is ItemLayoutArticleBinding) {
                item.let {
                    binding.title.text = it.title
                    binding.author.text = it.byline
                    binding.date.text = it.updated?.toDateFormat("yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd")
                    if(it.media?.size!! > 0) {
                        val url = it.media?.get(0)?.mediaMetaData?.get(0)?.url
                        if (url != null) {
                            Log.d("image url", url)
                        }
                    }
                }

            }
        }

        fun bind(response: ResponseArticles, position: Int) {
            response.results?.get(position)?.let { bindContent(it) }
        }
    }

    companion object {
        private const val TYPE_CONTENT = 0
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CONTENT         // We can handle view types conditionally here
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val binding = when (viewType) {
            TYPE_CONTENT -> ItemLayoutArticleBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false)
            else -> throw IllegalArgumentException("Invalid type")
        }
        return DataAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(adapterData, position)
    }

    override fun getItemCount() = adapterData.results?.size!!

    fun updateData(response: ResponseArticles) {
        adapterData = response
        //notifyDataSetChanged()
    }
}